package edu.iu.sci2.visualization.geomaps.geo.shapefiles;

import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.TransformException;

import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.io.Resources;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;

import edu.iu.sci2.visualization.geomaps.geo.projection.GeometryProjector;
import edu.iu.sci2.visualization.geomaps.geo.projection.KnownProjectedCRSDescriptor;
import edu.iu.sci2.visualization.geomaps.utility.NicelyNamedEnums.NicelyNamed;
import edu.iu.sci2.visualization.geomaps.viz.model.RegionAnnotationMode;

public enum Shapefile implements NicelyNamed {
	UNITED_STATES(Resources.getResource(Shapefile.class, "st99_d00.shp"), "United States",
			"%d U.S. states and other jurisdictions", "U.S. State", "U.S. state",
			"NAME",
			KnownProjectedCRSDescriptor.ALBERS,
			ImmutableSet.of(Inset.Request.ALASKA, Inset.Request.HAWAII, Inset.Request.PUERTO_RICO),
			ImmutableSet.of(AnchorPoint.NEAR_WASHINGTON, AnchorPoint.NEAR_FLORIDA)),
	WORLD(
			Resources.getResource(Shapefile.class, "countries.shp"), "World",
			"%d countries of the world", "Country", "country", "NAME",
			KnownProjectedCRSDescriptor.ECKERT_IV,
			ImmutableSet.<Inset.Request> of(), ImmutableSet.of(AnchorPoint.NEAR_ALASKA,
					AnchorPoint.NEAR_ANTARCTICA));

	public static final DefaultGeographicCRS FALLBACK_SOURCE_CRS = DefaultGeographicCRS.WGS84;

	private final String niceName;
	private final String mapDescriptionFormat;
	private final String componentDescriptionTitleCase;
	private final String componentDescriptionPlain;
	private final String featureAttributeName;
	private final KnownProjectedCRSDescriptor defaultProjectedCrs;
	private final FeatureSource<SimpleFeatureType, SimpleFeature> featureSource;
	private final ImmutableCollection<Inset.Request> insetRequests;
	private final ImmutableCollection<AnchorPoint> anchorPoints;

	private final String shapefileTypeName;

	private Shapefile(URL url, String niceName, String mapDescriptionFormat,
			String componentDescriptionTitleCase, String componentDescriptionPlain,
			String featureAttributeName, KnownProjectedCRSDescriptor defaultProjectedCrs,
			Collection<Inset.Request> insetRequests, Collection<AnchorPoint> anchorPoints)
			throws ShapefileException {
		this.niceName = niceName;
		this.mapDescriptionFormat = mapDescriptionFormat;
		this.componentDescriptionTitleCase = componentDescriptionTitleCase;
		this.componentDescriptionPlain = componentDescriptionPlain;
		this.featureAttributeName = featureAttributeName;
		this.defaultProjectedCrs = defaultProjectedCrs;
		this.anchorPoints = ImmutableSet.copyOf(anchorPoints);
		this.insetRequests = ImmutableSet.copyOf(insetRequests);

		try {
			ShapefileDataStore dataStore = new ShapefileDataStore(url);

			this.shapefileTypeName = dataStore.getTypeNames()[0];
			this.featureSource = dataStore.getFeatureSource(shapefileTypeName);
		} catch (IOException e) {
			throw new ShapefileException("The shapefile data store could not be opened.", e);
		}
	}

	public ReferencedEnvelope boundsForFeatureName(String featureName)
			throws FeatureBoundsDetectionException {
		try {
			Query query = new Query(shapefileTypeName, CQL.toFilter(String.format(
					"NAME = '%s'", featureName)));

			return featureSource.getFeatures(query).getBounds();
		} catch (CQLException e) {
			throw new FeatureBoundsDetectionException(String.format(
					"Failed to create feature filter for inset \"%s\".", featureName), e);
		} catch (IOException e) {
			throw new FeatureBoundsDetectionException(String.format(
					"Failed to read features for inset \"%s\".", featureName), e);
		}
	}

	/**
	 * If this feature source schema has no associated coordinate reference
	 * system, {@link #FALLBACK_SOURCE_CRS} is used instead.
	 */
	public CoordinateReferenceSystem detectNativeCRS() {
		return Objects.firstNonNull(featureSource.getSchema().getCoordinateReferenceSystem(),
				FALLBACK_SOURCE_CRS);
	}

	public String extractFeatureName(SimpleFeature feature) {
		Object featureName = feature.getAttribute(featureAttributeName);

		if (featureName != null) {
			return RegionAnnotationMode.normalizeFeatureName(String.valueOf(featureName));
		} else {
			String message = String.format("Feature %s has no \"%s\" property.  ", feature,
					featureAttributeName);
	
			message += "Consider using one of these properties: ";
			List<String> attributeDescriptorNames = Lists.newArrayList();
			for (AttributeDescriptor attributeDescriptor : viewOfFeatureCollection().getSchema()
					.getAttributeDescriptors()) {
				attributeDescriptorNames.add(attributeDescriptor.getName().toString());
			}
			message += Joiner.on(",").join(attributeDescriptorNames);
	
			throw new FeatureAttributeAbsentException(message);
		}
	}

	public static class FeatureAttributeAbsentException extends RuntimeException {
		private static final long serialVersionUID = -6714673548077059632L;

		public FeatureAttributeAbsentException(String message) {
			super(message);
		}
	}

	@Override
	public String toString() {
		return niceName;
	}

	public boolean hasInsets() {
		return !insetRequests.isEmpty();
	}

	@Override
	public String getNiceName() {
		return niceName;
	}

	public String makeMapDescription() {
		Set<String> uniqueFeatureNames = Sets.newHashSet();

		FeatureIterator<SimpleFeature> it = viewOfFeatureCollection().features();
		try {
			while (it.hasNext()) {
				uniqueFeatureNames.add(extractFeatureName(it.next()));
			}
		} finally {
			it.close();
		}

		return String.format(mapDescriptionFormat, uniqueFeatureNames.size());
	}

	public String getComponentDescriptionTitleCase() {
		return componentDescriptionTitleCase;
	}

	public String getComponentDescriptionPlain() {
		return componentDescriptionPlain;
	}

	public String getFeatureAttributeName() {
		return featureAttributeName;
	}

	public KnownProjectedCRSDescriptor getDefaultProjectedCrs() {
		return defaultProjectedCrs;
	}

	public ImmutableCollection<AnchorPoint> getAnchorPoints() {
		return anchorPoints;
	}

	public ImmutableCollection<Inset.Request> getInsetRequests() {
		return insetRequests;
	}

	public FeatureCollection<SimpleFeatureType, SimpleFeature> viewOfFeatureCollection()
			throws ShapefileFeatureRetrievalException {
		try {
			return featureSource.getFeatures();
		} catch (IOException e) {
			throw new ShapefileFeatureRetrievalException("Error accessing shapefile: "
					+ e.getMessage(), e);
		}
	}

	/**
	 * A special region to inset when projecting the features in this shapefile.
	 * A given InsetRequest dictates a feature name, an area scaling, a "natural"
	 * anchor coordinate, and an "inset" anchor coordinate. The inset calculates
	 * the bounding box for all features having the given name. Then all offered
	 * geometries, when they fall in this latlong region, are inset by (1)
	 * translating the "natural" anchor coordinate to the latlong space origin,
	 * (2) applying the area scaling factor, and (3) translating everything such that
	 * the "natural" anchor coordinate is now position at the "inset" anchor
	 * coordinate. When an offered coordinate does not fall into the box it is
	 * returned unchanged.
	 * 
	 * <p>The question of <em>whether</em> to inset is decided in latlong space,
	 * and in that case, the inset operation is <em>performed</em> in the
	 * projected xy space.
	 */
	public static class Inset {
		private final Request request;
		private final ReferencedEnvelope bounds;
		private final AffineTransform2D transform;

		private Inset(Request request, ReferencedEnvelope bounds, AffineTransform2D transform) {
			this.request = request;
			this.bounds = bounds;
			this.transform = transform;
		}

		public static Inset forRequest(Request request, Shapefile shapefile,
				GeometryProjector projector) throws InsetCreationException {
			try {
				return new Inset(
						request,
						shapefile.boundsForFeatureName(request.getFeatureName()),
						createTransform(request, projector));
			} catch (FeatureBoundsDetectionException e) {
				throw new InsetCreationException(String.format(
						"Failed to detect bounding box of the \"%s\" inset.",
						request.getFeatureName()), e);
			} catch (TransformException e) {
				throw new InsetCreationException(
						String.format("Failed to create transform for inset of \"%s\".",
								request.getFeatureName()), e);
			}
		}

		private static AffineTransform2D createTransform(Request request,
				GeometryProjector projector) throws TransformException {
			// First, re-center at origin so the scale works right
			Point naturalPoint = projector.transformCoordinate(request.getNaturalAnchor());
			AffineTransform preScale = AffineTransform.getTranslateInstance(-naturalPoint.getX(),
					-naturalPoint.getY());

			// Second, scale
			AffineTransform scale = AffineTransform.getScaleInstance(
					request.calculateDimensionScaling(), request.calculateDimensionScaling());

			// Third, move so that the natural anchor is now at the inset anchor
			Point insetPoint = projector.transformCoordinate(request.getInsetAnchor());
			AffineTransform postScale = AffineTransform.getTranslateInstance(insetPoint.getX(),
					insetPoint.getY());

			return new AffineTransform2D(preConcatenate(preScale, scale, postScale));
		}
		
		public boolean shouldInsetCoordinate(Coordinate coordinate) {
			return bounds.contains(coordinate);
		}

		public boolean shouldInset(Geometry geometry) {
			return bounds.contains(geometry.getEnvelopeInternal());
		}
		
		public Point insetPoint(Point projectedPoint) {
			try {
				Coordinate coordinate = new Coordinate();
				JTS.transform(projectedPoint.getCoordinate(), coordinate, transform);
				
				return JTSFactoryFinder.getGeometryFactory(null).createPoint(coordinate);
			} catch (TransformException e) {
				throw new RuntimeException("Inset transform failed.", e);
			}
		}

		public Geometry inset(Geometry projectedGeometry) {
			try {
				return JTS.transform(projectedGeometry, transform);
			} catch (TransformException e) {
				throw new RuntimeException("Inset transform failed.", e);
			}
		}
		
		public String makeLabel() {
			if (Math.abs(request.getAreaScaling() - 1.0) < 1E-3) {
				// Showing at 100% actual area so just report name.
				return request.getFeatureName();
			} else {
				return String.format("%s (%d%% actual area)",
						request.getFeatureName(),
						(int) (100 * request.getAreaScaling()));
			}
		}

		public Coordinate getLabelLowerLeft() {
			return request.getLabelLowerLeft();
		}

		private static AffineTransform preConcatenate(AffineTransform first,
				AffineTransform... rest) {
			AffineTransform concat = (AffineTransform) first.clone();
			for (AffineTransform subsequent : rest) {
				concat.preConcatenate(subsequent);
			}

			return concat;
		}

		public static class InsetCreationException extends Exception {
			private static final long serialVersionUID = 5491622047581675511L;

			public InsetCreationException(String message, Throwable cause) {
				super(message, cause);
			}
		}

		/**
		 * A request with which we can create an {@link Inset}.
		 * 
		 * @see Inset#forRequest(Request, Shapefile, GeometryProjector)
		 */
		public static class Request {
			public static final Request ALASKA = new Request("Alaska", new Coordinate(-160, 41),
					0.1, new Coordinate(-131, 55), new Coordinate(-127, 42));
			public static final Request HAWAII = new Request("Hawaii", new Coordinate(-159, 14),
					0.5, new Coordinate(-155, 20), new Coordinate(-126, 30));
			public static final Request PUERTO_RICO = new Request("Puerto Rico", new Coordinate(
					-68.7, 17), 1, new Coordinate(-66, 18), new Coordinate(-129, 37));

			private final String featureName;
			private final Coordinate labelLowerLeft;
			private final double areaScaling;
			private final Coordinate naturalAnchor;
			private final Coordinate insetAnchor;

			public Request(String featureName, Coordinate labelLowerLeft, double areaScaling,
					Coordinate naturalAnchor, Coordinate insetAnchor) {
				this.featureName = featureName;
				this.labelLowerLeft = labelLowerLeft;
				this.areaScaling = areaScaling;
				this.naturalAnchor = naturalAnchor;
				this.insetAnchor = insetAnchor;
			}

			public String getFeatureName() {
				return featureName;
			}			

			public Coordinate getLabelLowerLeft() {
				return labelLowerLeft;
			}

			public double getAreaScaling() {
				return areaScaling;
			}
			
			public double calculateDimensionScaling() {
				return Math.sqrt(areaScaling);
			}

			public Coordinate getNaturalAnchor() {
				return naturalAnchor;
			}

			public Coordinate getInsetAnchor() {
				return insetAnchor;
			}
		}
	}

	public static class AnchorPoint {
		public static final Shapefile.AnchorPoint NEAR_ALASKA = AnchorPoint.at("Near Alaska",
				new Coordinate(-179, 89 - GeometryProjector.NORTH_POLE_CROP_HEIGHT_IN_DEGREES));
		public static final Shapefile.AnchorPoint NEAR_WASHINGTON = AnchorPoint.at(
				"Near Aleutian Islands", new Coordinate(-127, 49));
		public static final Shapefile.AnchorPoint NEAR_ANTARCTICA = AnchorPoint.at(
				"Near Antarctica", new Coordinate(179, -89
						+ GeometryProjector.SOUTH_POLE_CROP_HEIGHT_IN_DEGREES));
		public static final Shapefile.AnchorPoint NEAR_FLORIDA = AnchorPoint.at(
				"Near Puerto Rico", new Coordinate(-79, 24));

		private final String displayName;
		private final Coordinate coordinate;

		private AnchorPoint(String displayName, Coordinate coordinate) {
			this.displayName = displayName;
			this.coordinate = coordinate;
		}

		public static AnchorPoint at(String displayName, Coordinate coordinate) {
			return new AnchorPoint(displayName, coordinate);
		}

		public String getDisplayName() {
			return displayName;
		}

		public Coordinate getCoordinate() {
			return coordinate;
		}

		@Override
		public boolean equals(Object thatObject) {
			if (this == thatObject) {
				return true;
			}
			if (thatObject == null) {
				return false;
			}
			if (!(thatObject instanceof AnchorPoint)) {
				return false;
			}
			AnchorPoint that = (AnchorPoint) thatObject;

			return Objects.equal(this.displayName, that.displayName)
					&& Objects.equal(this.coordinate, that.coordinate);
		}

		@Override
		public int hashCode() {
			return Objects.hashCode(displayName, coordinate);
		}
	}

	public static class ShapefileFeatureRetrievalException extends RuntimeException {
		private static final long serialVersionUID = -5812550122559595790L;

		public ShapefileFeatureRetrievalException(String message, Throwable cause) {
			super(message, cause);
		}
	}

	public static class ShapefileException extends RuntimeException {
		private static final long serialVersionUID = -9175935612884445370L;

		public ShapefileException(String message, Throwable cause) {
			super(message, cause);
		}
	}

	public static class FeatureBoundsDetectionException extends Exception {
		private static final long serialVersionUID = -2452894524543314017L;

		public FeatureBoundsDetectionException(String message, Throwable cause) {
			super(message, cause);
		}
	}
}
