package com.akvelon.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.akvelon.report.Report;

/**
 * Class parser XML response 
 */
public class SAXUtil extends DefaultHandler {

	private InputStream inputStream;
	private Report report;
	
	private List<Report> reports;
	
	private String reportName;
	
	private Boolean nbr = Boolean.FALSE;
	private Boolean name = Boolean.FALSE;
	private Boolean bliOwner = Boolean.FALSE;
	private Boolean taskName = Boolean.FALSE;
	private Boolean taskOwner = Boolean.FALSE;
	private Boolean assType = Boolean.FALSE;
	
	private AssetType assetType = null;

	public SAXUtil(String taskName) {
		this.reportName = taskName;
		reports = new ArrayList<Report>();
	}

	public List<Report> parse(InputStream is) throws SAXException, IOException, ParserConfigurationException {
		this.inputStream = is;
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		parser.parse(inputStream, this);
		return reports;
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (qName.equalsIgnoreCase("Asset")) {
			report = new Report();
			report.setReportName(reportName.split("\\.")[0].replace("_", " "));
			return;
		}
		if (qName.equalsIgnoreCase("Attribute") && attributes.getValue(0).equalsIgnoreCase("AssetType")) {
			assType = Boolean.TRUE;
			return;
		}
		if (assetType != null) {
			switch (assetType) {
			case Story:
				storyElement(qName, attributes);
				return;
			case Task:
			case Test:
			default:
				taskElement(qName, attributes);
				return;
			}
		}
	}

	@Override
	public void characters(char ch[], int start, int length) throws SAXException {
		if (assType) {
			assetType = AssetType.valueOf(new String(ch, start, length));
			assType = Boolean.FALSE;
			return;
		}
		if (nbr) {
			report.setBliID(new String(ch, start, length));
			nbr = Boolean.FALSE;
			return;
		}
		if (name) {
			report.setBliName(new String(ch, start, length));
			name = Boolean.FALSE;
			return;
		}
		if (bliOwner) {
			report.setBliOwner(new String(ch, start, length));
			bliOwner = Boolean.FALSE;
			return;
		}
		if (taskName) {
			report.setTaskName(new String(ch, start, length));
			taskName = Boolean.FALSE;
			return;
		}
		if (taskOwner) {
			report.setTaskOwner(new String(ch, start, length));
			taskOwner = Boolean.FALSE;
			return;
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (qName.equalsIgnoreCase("Asset")) {
			reports.add(report);
		}
	}
	
	private void storyElement(String qName, Attributes attributes) {
		if (qName.equalsIgnoreCase("Attribute") && attributes.getValue(0).equalsIgnoreCase("Number")) {
			nbr = Boolean.TRUE;
			return;
		}
		if (qName.equalsIgnoreCase("Attribute") && attributes.getValue(0).equalsIgnoreCase("Name")) {
			name = Boolean.TRUE;
			return;
		}
		if (qName.equalsIgnoreCase("Attribute") && attributes.getValue(0).equalsIgnoreCase("Owners.Name")) {
			bliOwner = Boolean.TRUE;
			return;
		}
	}
	
	private void taskElement(String qName, Attributes attributes) {
		if (qName.equalsIgnoreCase("Attribute") && attributes.getValue(0).equalsIgnoreCase("Parent.Number")) {
			nbr = Boolean.TRUE;
			return;
		}
		if (qName.equalsIgnoreCase("Attribute") && attributes.getValue(0).equalsIgnoreCase("Parent.Name")) {
			name = Boolean.TRUE;
			return;
		}
		if (qName.equalsIgnoreCase("Attribute") && attributes.getValue(0).equalsIgnoreCase("Parent.Owners.Name")) {
			bliOwner = Boolean.TRUE;
			return;
		}
		if (qName.equalsIgnoreCase("Attribute") && attributes.getValue(0).equalsIgnoreCase("Name")) {
			taskName = Boolean.TRUE;
			return;
		}
		if (qName.equalsIgnoreCase("Attribute") && attributes.getValue(0).equalsIgnoreCase("Owners.Name")) {
			taskOwner = Boolean.TRUE;
			return;
		}
	}
}
