package com.test.parseXML.util;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.test.parseXML.report.Report;

public class SAXUtil extends DefaultHandler {

	private InputStream inputStream;
	private Report rep; 

	private String assets = "%s,%s,%s";

	public SAXUtil(Report rep) {
		this.rep = rep;
	}

	public Report parse(InputStream is) throws SAXException, IOException, ParserConfigurationException {
		this.inputStream = is;
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		parser.parse(inputStream, this);
		return rep;
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

		if (qName.equalsIgnoreCase("Assets")) {
			String asset = String.format(assets, attributes.getValue(0), attributes.getValue(1), attributes.getValue(2));
			rep.setLine(asset);
		}
	}
}
