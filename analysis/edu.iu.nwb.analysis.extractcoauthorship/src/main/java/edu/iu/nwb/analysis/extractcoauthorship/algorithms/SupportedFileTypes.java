package edu.iu.nwb.analysis.extractcoauthorship.algorithms;

public interface SupportedFileTypes {
	static final CitationFormat bibtex = new CitationFormat("bibtex","author");
	static final CitationFormat isi = new CitationFormat("isi","Authors");
	static final CitationFormat scopus = new CitationFormat("scopus","Authors");
	static final CitationFormat endnote = new CitationFormat("endnote","Authors");
	
	static final String[] supportedFormats = CitationFormat.getSupportedFormats();
}

