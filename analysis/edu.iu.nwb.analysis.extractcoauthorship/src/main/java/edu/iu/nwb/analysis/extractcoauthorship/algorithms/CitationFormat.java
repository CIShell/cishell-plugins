package edu.iu.nwb.analysis.extractcoauthorship.algorithms;

import java.util.HashMap;

public class CitationFormat {
    private static HashMap nameToColumn = new HashMap();
    
    public CitationFormat(String name, String authorColumn){
        CitationFormat.nameToColumn.put(name, authorColumn);
    }

    public static String getAuthorColumnByName(String name){

        if(name.equals("isi") || name.equals("scopus") || name.equals("endnote")){
            return "Authors";
        } else {
            return "author";
        }
        
    }
    
    public static String[] getSupportedFormats(){
        String[] supportedFormats = new String[CitationFormat.nameToColumn.size()];
        
        supportedFormats = (String[])CitationFormat.nameToColumn.keySet().toArray(supportedFormats);
        
        return supportedFormats;
    }
    
    
}