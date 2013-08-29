package com.test.parseXML;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SAXUtil extends DefaultHandler {

	private InputStream inputStream;
	private Writer writer;
	private String fileName="assets.txt";
	private static final String ENCODING = "UTF-8"; 

	private String assets = "Assets: total=%s, pageSize=%s, pageStart=%s.";

	public SAXUtil(InputStream is) {
		this.inputStream = is;
	}

	public void parse() throws SAXException, IOException, ParserConfigurationException {
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		parser.parse(inputStream, this);
	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
		try {
			writer = new BufferedWriter(new OutputStreamWriter(
			          new FileOutputStream(fileName), ENCODING));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

		if (qName.equalsIgnoreCase("Assets")) {
			String asset = String.format(assets, attributes.getValue(0), attributes.getValue(1), attributes.getValue(2));
			System.out.println(asset);
			try {
				writer.write(asset + "\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void endDocument() throws SAXException {
		super.endDocument();
		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
