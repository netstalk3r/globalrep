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

import com.akvelon.report.HourReport;

public class SAXHourUtil extends DefaultHandler {

	private InputStream inputStream;
	private HourReport hReport;

	List<HourReport> hReports;

	private boolean value = false;
	private boolean name = false;

	public SAXHourUtil() {
		hReports = new ArrayList<HourReport>();
	}

	public List<HourReport> parse(InputStream is) throws SAXException, IOException, ParserConfigurationException {
		this.inputStream = is;
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		parser.parse(inputStream, this);
		return ReportUtil.countActuals(hReports);
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (qName.equalsIgnoreCase("Asset")) {
			hReport = new HourReport();
			return;
		}
		if (qName.equalsIgnoreCase("Attribute") && attributes.getValue(0).equalsIgnoreCase("Value")) {
			value = true;
			return;
		}
		if (qName.equalsIgnoreCase("Attribute") && attributes.getValue(0).equalsIgnoreCase("Member.Name")) {
			name = true;
			return;
		}
	}

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		if (name) {
			hReport.setTeamMember(new String(ch, start, length));
			name= false;
			return;
		}
		if (value) {
			hReport.setReportedHours(Double.valueOf(new String(ch, start, length)));
			value = false;
			return;
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (qName.equalsIgnoreCase("Asset")) {
			hReports.add(hReport);
		}
	}

}
