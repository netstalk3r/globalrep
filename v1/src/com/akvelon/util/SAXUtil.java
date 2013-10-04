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

public class SAXUtil extends DefaultHandler {

	private InputStream inputStream;
	private Report report;
	
	private List<Report> reports;
	
	private String reportName;
	
	private Boolean nbr = Boolean.FALSE;
	private Boolean name = Boolean.FALSE;
	private Boolean owner = Boolean.FALSE;

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
		if (qName.equalsIgnoreCase("Attribute") && attributes.getValue(0).equalsIgnoreCase("Parent.Number")) {
			nbr = Boolean.TRUE;
			return;
		}
		if (qName.equalsIgnoreCase("Attribute") && attributes.getValue(0).equalsIgnoreCase("Parent.Name")) {
			name = Boolean.TRUE;
			return;
		}
		if (qName.equalsIgnoreCase("Attribute") && attributes.getValue(0).equalsIgnoreCase("Owners.Name")) {
			owner = Boolean.TRUE;
			return;
		}
	}

	@Override
	public void characters(char ch[], int start, int length) throws SAXException {
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
		if (owner) {
			report.setOwnerTaskName(new String(ch, start, length));
			owner = Boolean.FALSE;
			return;
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (qName.equalsIgnoreCase("Asset")) {
			reports.add(report);
		}
	}
}
