package edu.iu.nwb.converter.pajekmat.common;

import java.awt.GraphicsEnvironment;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MATArcs {
	private static Map Attributes = new LinkedHashMap();
	private Map Numeric_Parameters;
	private Map String_Parameters;
	private int source;
	private int target;
	private String comment = null;
	//private String label = null;


	private boolean valid = false;
	public MATArcs(){	
		this.Numeric_Parameters = new ConcurrentHashMap();
		this.String_Parameters = new ConcurrentHashMap();	
	}

	public MATArcs(String s) throws Exception{
		String[] properties = MATFileFunctions.processTokens(s);
		this.Numeric_Parameters = new ConcurrentHashMap();
		this.String_Parameters = new ConcurrentHashMap();
		this.valid = testArcsnEdges(properties);
	}

	public boolean testArcsnEdges(String[] strings) throws Exception{
		boolean value = false;
		Queue stringQueue = new ConcurrentLinkedQueue();
		for(int ii = 0; ii < strings.length; ii++){
			String s = strings[ii];
			stringQueue.add(s);
		}
		if(((String)stringQueue.peek()).startsWith(MATFileProperty.PREFIX_COMMENTS)){
			comment = "";
			for(int ii = 0; ii < strings.length; ii++){
				String s = strings[ii];
				comment += s;
			}
			return true;
		}

		try{

			this.testSourceTargetWeight(stringQueue);

			if(!stringQueue.isEmpty()){


				testParameters(stringQueue);

			}

		}catch(Exception ex){
			throw ex;
		}
		value = finalTest();
		return value;
	}

	public boolean testSourceTargetWeight(Queue qs) throws Exception{
		boolean value = true;
		int i = 0;
		try{
			while(!qs.isEmpty()){
				if((MATFileFunctions.isInList((String) qs.peek(),
						ARCEDGEParameter.ARCEDGE_NUMERIC_PARAMETER_LIST)) 
						|| (MATFileFunctions.isInList((String) qs.peek(),
								ARCEDGEParameter.ARCEDGE_STRING_PARAMETER_LIST))){
					break;
				}
					switch (i){
					case 0:
						this.setSource((String) qs.poll());
						break;
					case 1:
						this.setTarget((String) qs.poll());
						break;
					case 2:
						this.setWeight((String) qs.poll());
						break;
					default:
						throw new Exception("Unknown data found");
					}		
				i++;
			}
			switch(i){
			case 0:
			case 1:
				throw new Exception("Arcs and edges must contain both source and target values");
			case 2:
				this.setWeight("1");
				break;
			}
		}
		catch(NumberFormatException ex){
			throw new Exception("The file contains an invalid sequence in the positional data.");
		}

		return value;
	}
	

	public boolean testParameters(Queue qs) throws Exception{
		boolean value = false;
		while(!qs.isEmpty()){
			String s1 = (String) qs.poll();

			if(qs.isEmpty()){
				throw new Exception("Expected a value for parameter: " + s1);
			}
			String s2 = (String) qs.poll();


			if(s1.equalsIgnoreCase(ARCEDGEParameter.PARAMETER_WIDTH)){
				this.setWidth(s2);
			}
			else if(s1.equalsIgnoreCase(ARCEDGEParameter.PARAMETER_COLOR)){
				this.setColor(s2);
			}
			else if(s1.equalsIgnoreCase(ARCEDGEParameter.PARAMETER_PATTERN)){
				this.setPattern(s2);
			}
			else if(s1.equalsIgnoreCase(ARCEDGEParameter.PARAMETER_SIZE)){
				this.setSize(s2);
			}
			else if(s1.equalsIgnoreCase(ARCEDGEParameter.PARAMETER_ARROW_SHAPE)){
				this.setArrowShape(s2);
			}
			else if(s1.equalsIgnoreCase(ARCEDGEParameter.PARAMETER_ARROW_POSITION)){
				this.setArrowPosition(s2);
			}
			else if(s1.equalsIgnoreCase(ARCEDGEParameter.PARAMETER_LABEL)){
				this.setLabel(s2);
			}
			else if(s1.equalsIgnoreCase(ARCEDGEParameter.PARAMETER_LABEL_POSITION)){
				this.setLabelPosition(s2);
			}
			else if(s1.equalsIgnoreCase(ARCEDGEParameter.PARAMETER_LABEL_RADIUS)){
				this.setLabelRadius(s2);
			}
			else if(s1.equalsIgnoreCase(ARCEDGEParameter.PARAMETER_LABEL_PHI)){
				this.setLabelPhi(s2);
			}
			else if(s1.equalsIgnoreCase(ARCEDGEParameter.PARAMETER_LABEL_COLOR)){
				this.setLabelColor(s2);
			}
			else if(s1.equalsIgnoreCase(ARCEDGEParameter.PARAMETER_LABEL_ANGLE)){
				this.setLabelAngle(s2);	
			}
			else if(s1.equalsIgnoreCase(ARCEDGEParameter.PARAMETER_FONT_SIZE)){
				this.setFontSize(s2);
			}
			else if(s1.equalsIgnoreCase(ARCEDGEParameter.PARAMETER_FONT)){
				this.setFont(s2,qs);
			}
			else if(s1.equalsIgnoreCase(ARCEDGEParameter.PARAMETER_HOOK_ONE)){
				this.setHook(s2,1);
			}
			else if(s1.equalsIgnoreCase(ARCEDGEParameter.PARAMETER_HOOK_TWO)){
				this.setHook(s2,2);
			}
			else if(s1.equalsIgnoreCase(ARCEDGEParameter.PARAMETER_ANGLE_ONE)){
				this.setAngle(s2,1);
			}
			else if(s1.equalsIgnoreCase(ARCEDGEParameter.PARAMETER_ANGLE_TWO)){
				this.setAngle(s2,2);
			}
			else if(s1.equalsIgnoreCase(ARCEDGEParameter.PARAMETER_VELOCITY_ONE)){
				this.setVelocity(s2,1);
			}
			else if(s1.equalsIgnoreCase(ARCEDGEParameter.PARAMETER_VELOCITY_TWO)){
				this.setVelocity(s2,2);
			}
			else if(s1.startsWith(MATFileProperty.PREFIX_COMMENTS)){
				qs.clear();
				break;
			}

			else {
				throw new Exception("Unknown parameter: " + s1);
			}
		}
		value = true;
		return value;
	}

private boolean finalTest() throws Exception{
	if((this.getAttribute(ARCEDGEParameter.PARAMETER_LABEL_PHI) == null) && (this.getAttribute(ARCEDGEParameter.PARAMETER_LABEL_RADIUS) == null))
		return true;
	else if((this.getAttribute(ARCEDGEParameter.PARAMETER_LABEL_PHI) != null) && (this.getAttribute(ARCEDGEParameter.PARAMETER_LABEL_RADIUS) != null))
		return true;
	else
		throw new Exception("Only one of two polar coordinates has been set");
}
	
	/*************************
	 * 
	 * Setters
	 * 
	 *************************/

	public void setSource(String s) throws Exception {
		int i = MATFileFunctions.asAnInteger(s);
		MATArcs.Attributes.put(MATFileProperty.ATTRIBUTE_SOURCE, MATFileProperty.TYPE_INT);
		if(!(i > 0))
			throw new Exception("Source id must be greater than 0");
		this.source = i;
	}
	
	public void setTarget(String s) throws Exception{
		int i = MATFileFunctions.asAnInteger(s);
		MATArcs.Attributes.put(MATFileProperty.ATTRIBUTE_TARGET, MATFileProperty.TYPE_INT);
		if(!(i > 0))
			throw new Exception("Target id must be greater than 0");
		this.target = i;
	}

	public void setWeight(String s) throws Exception{
		float f = MATFileFunctions.asAFloat(s);
		MATArcs.Attributes.put(MATFileProperty.ATTRIBUTE_WEIGHT,MATFileProperty.TYPE_FLOAT);
		this.Numeric_Parameters.put(MATFileProperty.ATTRIBUTE_WEIGHT, new Float(f));
	}
	
	
	private void setWidth(float f){
		MATArcs.Attributes.put(ARCEDGEParameter.PARAMETER_WIDTH, MATFileProperty.TYPE_FLOAT);
		this.Numeric_Parameters.put(ARCEDGEParameter.PARAMETER_WIDTH, new Float(f));
	}
	public void setWidth(String s) throws Exception {
		float f = MATFileFunctions.asAFloat(s);
		if(f > 0)
			this.setWidth(f);
		else
			throw new Exception("Line width must be greater than 0.0");
	}
	
	public void setColor(String s) throws Exception {
		if(MATFileFunctions.isInList(s, MATFileColor.VERTEX_COLOR_LIST)){
			MATArcs.Attributes.put(ARCEDGEParameter.PARAMETER_COLOR, MATFileProperty.TYPE_STRING);
			this.String_Parameters.put(ARCEDGEParameter.PARAMETER_COLOR, s);
		}
		else
			throw new Exception(s + " is not a valid color selection.");
	}
	
	public void setPattern(String s) throws Exception {
		if(s.equals("Solid") || s.equals("Dots")){
			MATArcs.Attributes.put(ARCEDGEParameter.PARAMETER_PATTERN, MATFileProperty.TYPE_STRING);
			this.String_Parameters.put(ARCEDGEParameter.PARAMETER_PATTERN, s);
		}
		else
			throw new Exception(s + " is an unrecognized pattern.");
	}
	
	private void setSize(float f){
		MATArcs.Attributes.put(ARCEDGEParameter.PARAMETER_SIZE, MATFileProperty.TYPE_FLOAT);
		this.Numeric_Parameters.put(ARCEDGEParameter.PARAMETER_SIZE, new Float(f));
	}
	public void setSize(String s) throws Exception {
		float f = MATFileFunctions.asAFloat(s);
		if(!(f>0))
			throw new Exception("Arrow size must be greater than 0");
		this.setSize(f);
	}
	
	public void setArrowShape(String s) throws Exception {
		if(s.equals("A") || s.equals("B")){
			MATArcs.Attributes.put(ARCEDGEParameter.PARAMETER_ARROW_SHAPE, MATFileProperty.TYPE_STRING);
			this.String_Parameters.put(ARCEDGEParameter.PARAMETER_ARROW_SHAPE, s);
		}
		else
			throw new Exception(s + " is an unrecognized arrow shape");
	}
	
	private void setArrowPosition(float f){
		MATArcs.Attributes.put(ARCEDGEParameter.PARAMETER_ARROW_POSITION, MATFileProperty.TYPE_FLOAT);
		this.Numeric_Parameters.put(ARCEDGEParameter.PARAMETER_ARROW_POSITION, new Float(f));
	}
	public void setArrowPosition(String s) throws Exception {
		float f = MATFileFunctions.asAFloat(s);
		if(!(f > 0))
			throw new Exception("Arrow position must be greater than 0.");
		this.setArrowPosition(f);
	}
	
	public void setLabel(String s) {
		MATArcs.Attributes.put(ARCEDGEParameter.PARAMETER_LABEL, MATFileProperty.TYPE_STRING);
		this.String_Parameters.put(ARCEDGEParameter.PARAMETER_LABEL, s);
	}
	
	private void setLabelPosition(float f){
		MATArcs.Attributes.put(ARCEDGEParameter.PARAMETER_LABEL_POSITION, MATFileProperty.TYPE_STRING);
		this.Numeric_Parameters.put(ARCEDGEParameter.PARAMETER_LABEL_POSITION, new Float(f));
	}
	public void setLabelPosition(String s) throws Exception {
		float f = MATFileFunctions.asAFloat(s);
		if(!(f > 0))
			throw new Exception("Label position must be greater than 0");
		this.setLabelPosition(f);
	}
	
	private void setLabelRadius(float f){
		MATArcs.Attributes.put(ARCEDGEParameter.PARAMETER_LABEL_RADIUS, MATFileProperty.TYPE_STRING);
		this.Numeric_Parameters.put(ARCEDGEParameter.PARAMETER_LABEL_RADIUS, new Float(f));
	}
	public void setLabelRadius(String s) throws Exception {
		float f = MATFileFunctions.asAFloat(s);
		if(!(f > 0))
			throw new Exception("Label radius must be greater than 0.");
		this.setLabelRadius(f);
	}
	
	private void setLabelPhi(float f){
		MATArcs.Attributes.put(ARCEDGEParameter.PARAMETER_LABEL_PHI, MATFileProperty.TYPE_FLOAT);
		this.Numeric_Parameters.put(ARCEDGEParameter.PARAMETER_LABEL_PHI, new Float(f));
	}
	public void setLabelPhi(String s) throws Exception {
		float f = MATFileFunctions.asAFloat(s);
		if(!((f >= 0) && (f <= 360)))
			throw new Exception("The label phi must be between 0 and 360 inclusive");
		this.setLabelPhi(f);
	}
	
	public void setLabelColor(String s) throws Exception {
		if(MATFileFunctions.isInList(s, MATFileColor.VERTEX_COLOR_LIST)){
			MATArcs.Attributes.put(ARCEDGEParameter.PARAMETER_LABEL_COLOR, MATFileProperty.TYPE_STRING);
			this.String_Parameters.put(ARCEDGEParameter.PARAMETER_LABEL_COLOR, s);
		}
		else
			throw new Exception(s + " is not a valid color selection.");
	}

	private void setLabelAngle(float f){
		MATArcs.Attributes.put(ARCEDGEParameter.PARAMETER_LABEL_ANGLE, MATFileProperty.TYPE_STRING);
		this.Numeric_Parameters.put(ARCEDGEParameter.PARAMETER_LABEL_ANGLE, new Float(f));
	}
	public void setLabelAngle(String s) throws Exception {
		float f = MATFileFunctions.asAFloat(s);
		if(!((f >= 0) && (f <= 360)))
			throw new Exception("The label angle must be between 0 and 360 inclusive");
		this.setLabelAngle(f);
	}
	
	private void setFontSize(float f) throws Exception {
		MATArcs.Attributes.put(ARCEDGEParameter.PARAMETER_FONT_SIZE, MATFileProperty.TYPE_FLOAT);
		this.Numeric_Parameters.put(ARCEDGEParameter.PARAMETER_FONT_SIZE, new Float(f));
	}
	public void setFontSize(String s) throws Exception {
		float f = MATFileFunctions.asAFloat(s);
		if(!(f > 0))
			throw new Exception("Font size must be greater than 0");
		this.setFontSize(f);
	}
	
	private TreeSet setFontPrime(String s, TreeSet ss){
		String compare = s;
		TreeSet ts = new TreeSet();
		compare = compare.toLowerCase();
		for(Iterator ii = ss.iterator(); ii.hasNext();){
			String st = (String) ii.next();
			if(st.startsWith(s)){
				ts.add(st);
			}
		}
		return ts;
	}
	public void setFont(String s, Queue qs) throws Exception {
		String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		TreeSet compareList = new TreeSet();
		String compare = s;
		for(int ii = 0; ii < fonts.length; ii++){
			String st = fonts[ii];
			if(st.startsWith(compare)){
				compareList.add(st);		
			}
		}	

		while(!qs.isEmpty()){

			String peekValue = (String) qs.peek();
			if(compareList.isEmpty())
				throw new Exception(compare + " is not a recognized font on this system");
			else if(MATFileFunctions.isInList(peekValue, ARCEDGEParameter.ARCEDGE_NUMERIC_PARAMETER_LIST) || MATFileFunctions.isInList(peekValue, ARCEDGEParameter.ARCEDGE_STRING_PARAMETER_LIST))
				break;
			compare = compare + " " + qs.poll();
			compareList = this.setFontPrime(compare, compareList);
		}
		MATArcs.Attributes.put(ARCEDGEParameter.PARAMETER_FONT, MATFileProperty.TYPE_STRING);
		this.String_Parameters.put(MATFileParameter.PARAMETER_FONT, compare);
	}
	
	public void setHook(String s, int i) throws Exception {
		switch(i){
		case 1:
			MATArcs.Attributes.put(ARCEDGEParameter.PARAMETER_HOOK_ONE, MATFileProperty.TYPE_FLOAT);
			this.Numeric_Parameters.put(ARCEDGEParameter.PARAMETER_HOOK_ONE, new Float(MATFileFunctions.asAFloat(s)));
			break;
		case 2:
			MATArcs.Attributes.put(ARCEDGEParameter.PARAMETER_HOOK_TWO, MATFileProperty.TYPE_FLOAT);
			this.Numeric_Parameters.put(ARCEDGEParameter.PARAMETER_HOOK_TWO, new Float(MATFileFunctions.asAFloat(s)));
			default:
				throw new Exception("Unknown hook h"+i);
		}
	}
	
	public void setAngle(String s, int i) throws Exception {
		switch(i){
		case 1:
			MATArcs.Attributes.put(ARCEDGEParameter.PARAMETER_ANGLE_ONE, MATFileProperty.TYPE_FLOAT);
			this.Numeric_Parameters.put(ARCEDGEParameter.PARAMETER_ANGLE_ONE,
					new Float(MATFileFunctions.asAFloat(s)));
			break;
		case 2:
			MATArcs.Attributes.put(ARCEDGEParameter.PARAMETER_ANGLE_TWO, MATFileProperty.TYPE_FLOAT);
			this.Numeric_Parameters.put(ARCEDGEParameter.PARAMETER_ANGLE_TWO,
					new Float(MATFileFunctions.asAFloat(s)));
			default:
				throw new Exception("Unknown angle a"+i);
		}
	}
	
	public void setVelocity(String s, int i) throws Exception{
		switch(i){
		case 1:
			MATArcs.Attributes.put(ARCEDGEParameter.PARAMETER_VELOCITY_ONE, MATFileProperty.TYPE_FLOAT);
			this.Numeric_Parameters.put(ARCEDGEParameter.PARAMETER_VELOCITY_ONE,
					new Float(MATFileFunctions.asAFloat(s)));
			break;
		case 2:
			MATArcs.Attributes.put(ARCEDGEParameter.PARAMETER_VELOCITY_TWO, MATFileProperty.TYPE_FLOAT);
			this.Numeric_Parameters.put(ARCEDGEParameter.PARAMETER_VELOCITY_TWO,
					new Float(MATFileFunctions.asAFloat(s)));
			default:
				throw new Exception("Unknown velocity k"+i);
		}
	}
	
	/******
	 * Getters
	 ******/
	
	public Object getAttribute(String s){
		String st = (String) MATArcs.Attributes.get(s);
		if(st == null)
			return null;
		else if(s.equalsIgnoreCase(MATFileProperty.ATTRIBUTE_SOURCE))
			return new Integer(this.source);
		else if(s.equalsIgnoreCase(MATFileProperty.ATTRIBUTE_TARGET))
			return new Integer(this.target);
		else if(st.equalsIgnoreCase("float"))
			return this.Numeric_Parameters.get(s);
		else 
			return this.String_Parameters.get(s);
	}
	
	public boolean isValid(){
		return this.valid;
	}
	
	public static List getArcsnEdgesAttributes(){
		ArrayList attributeList = new ArrayList();
		for(Iterator ii = MATArcs.Attributes.keySet().iterator(); ii.hasNext();){
			String s = (String) ii.next();
			attributeList.add(new MATAttribute(s, (String) MATArcs.Attributes.get(s)));
		}
		return attributeList;

	}
	
	/*****
	 * 
	 * Output
	 * 
	 */
	
	public String toString(){
		String output = "";

		for(Iterator ii = MATArcs.Attributes.keySet().iterator(); ii.hasNext();){
			String s = (String) ii.next();
			output += s + ":"+this.getAttribute(s) + " ";
		}
		return output;
	}

}