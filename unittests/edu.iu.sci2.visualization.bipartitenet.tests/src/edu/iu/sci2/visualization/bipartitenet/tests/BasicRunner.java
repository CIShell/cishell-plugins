package edu.iu.sci2.visualization.bipartitenet.tests;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import org.freehep.graphics2d.VectorGraphics;
import org.freehep.graphicsio.ps.PSGraphics2D;
import org.freehep.util.UserProperties;

import edu.iu.nwb.util.nwbfile.ParsingException;
import edu.iu.sci2.visualization.bipartitenet.PageDirector;
import edu.iu.sci2.visualization.bipartitenet.PageDirector.Layout;
import edu.iu.sci2.visualization.bipartitenet.algorithm.NodeOrderingOption;
import edu.iu.sci2.visualization.bipartitenet.component.CanvasContainer;
import edu.iu.sci2.visualization.bipartitenet.model.BipartiteGraphDataModel;
import edu.iu.sci2.visualization.bipartitenet.model.NWBDataImporter;
import edu.iu.sci2.visualization.bipartitenet.model.NodeType;

public class BasicRunner {

	private static final String SUBTITLE = "Excepteur sint occaecat cupidatat non proident";
	private static final NodeOrderingOption ORDERING = NodeOrderingOption.LABEL_NUMERIC_ASC;

	public static void main(String[] args) throws IOException, ParsingException {
		// NWBDataImporter importer = new NWBDataImporter("bipartitetype",
		// "Who", "Desirability");
		// BipartiteGraphDataModel model =
		// importer.constructModelFromFile(BasicRunner.class.getResourceAsStream("test-network.nwb"));
//
//		NWBDataImporter importer = new NWBDataImporter("bipartitetype",
//				"Authors", null, null);
//		BipartiteGraphDataModel model = importer
//				.constructModelFromFile(BasicRunner.class
//						.getResourceAsStream("Bipartite network from Authors and Title.nwb"));
//		NWBDataImporter importer = new NWBDataImporter("bipartitetype",
//				"APPL_ID", "totalawarded", null);
//		BipartiteGraphDataModel model =
//				importer.constructModelFromFile(BasicRunner.class.getResourceAsStream("big-boring-network.nwb"));

		NWBDataImporter importer = NWBDataImporter.create("bipartitetype", "Vegan", "weight", "weight", ORDERING.getOrdering(),
				ORDERING.getOrdering());
		BipartiteGraphDataModel model =
				importer.constructModelFromFile(BasicRunner.class.getResourceAsStream("vegan-ipsum.nwb"));

		PageDirector.Layout layout = PageDirector.Layout.PRINT;

		renderOnScreen(model, layout);
		renderToPNG(model, layout);

		long now = System.currentTimeMillis();
		renderToPsWithFreeHep(model, layout);
		long later = System.currentTimeMillis();
		System.err.println(String.format("%d millis to render.", later - now));

		System.out.println("Done rendering");
	}

	private static void renderToPsWithFreeHep(BipartiteGraphDataModel model,
			Layout layout) throws IOException {
		UserProperties p = new UserProperties();
		// p.setProperty(PSGraphics2D.PAGE_SIZE,PageConstants.INTERNATIONAL);
		p.setProperty(PSGraphics2D.EMBED_FONTS, false);
		p.setProperty(PSGraphics2D.TEXT_AS_SHAPES, false);
		VectorGraphics g = new PSGraphics2D(new File("BLAH-freehep.ps"),
				new Dimension(layout.getWidth(), layout.getHeight()));
		g.setProperties(p);
		g.startExport();
		NodeType left = NodeType.createWithDefaultDisplayName("Vegan", ORDERING, "weight");
		NodeType right = NodeType.createWithDefaultDisplayName("Ipsum", ORDERING, "weight");
		
		PageDirector r = new PageDirector(layout, SUBTITLE, model, left, right);
		g.setClip(0, 0, layout.getWidth(), layout.getHeight());
		r.paint(g);
		g.endExport();
		g.dispose();
	}

	private static void renderToPNG(BipartiteGraphDataModel model, Layout layout)
			throws IOException {
		BufferedImage img = new BufferedImage(layout.getWidth(),
				layout.getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics2D g = img.createGraphics();
		g.setPaint(Color.white);
		g.fillRect(0, 0, img.getWidth(), img.getHeight());
		g.setPaint(Color.black);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		NodeType left = NodeType.createWithDefaultDisplayName("Vegan", ORDERING, "weight");
		NodeType right = NodeType.createWithDefaultDisplayName("Ipsum", ORDERING, "weight");
		
		PageDirector r = new PageDirector(layout, SUBTITLE, model, left, right);
		r.paint(g);
		ImageIO.write(img, "PNG", new File("BLAH.png"));
	}

	private static void renderOnScreen(BipartiteGraphDataModel model,
			Layout layout) {
		JFrame f = new JFrame("Application Review");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		CanvasContainer cc = new CanvasContainer();
		NodeType left = NodeType.createWithDefaultDisplayName("Vegan", ORDERING, "weight");
		NodeType right = NodeType.createWithDefaultDisplayName("Ipsum", ORDERING, "weight");
		
		PageDirector r = new PageDirector(layout, SUBTITLE, model, left, right);
		cc.add(r);
		f.getContentPane().add(cc);
		cc.setPreferredSize(new Dimension(layout.getWidth(), layout.getHeight()));
		f.pack();
		f.setVisible(true);
	}

}
