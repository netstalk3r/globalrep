package com.akvelon.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
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

	private boolean isNbr = false;
	private boolean isName = false;
	private boolean isBliOwner = false;
	private boolean isTaskName = false;
	private boolean isTaskOwner = false;
	
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
		if (qName.equalsIgnoreCase("Asset") && attributes.getIndex("id") == 1) {
			report = new Report();
			report.setReportName(ReportUtil.normalizeName(reportName));
			String[] arrt = attributes.getValue(1).split(":");
			assetType = AssetType.valueOf(arrt[0]);
			makeLink(arrt[1]);
			return;
		}
		if (assetType != null) {
			switch (assetType) {
			case Defect:
			case Story:
				storyElement(qName, attributes);
				break;
			case Task:
			case Test:
			default:
				taskElement(qName, attributes);
				break;
			}
		}
	}

	@Override
	public void characters(char ch[], int start, int length) throws SAXException {
		if (isNbr) {
			report.setBliID(new String(ch, start, length));
			isNbr = false;
			return;
		}
		if (isName) {
			report.setBliName(new String(ch, start, length));
			isName = false;
			return;
		}
		if (isBliOwner) {
			report.setBliOwner(new String(ch, start, length));
			isBliOwner = false;
			return;
		}
		if (isTaskName) {
			report.setTaskName(new String(ch, start, length));
			isTaskName = false;
			return;
		}
		if (isTaskOwner) {
			report.setTaskOwner(new String(ch, start, length));
			isTaskOwner = false;
			return;
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (qName.equalsIgnoreCase("Asset")) {
			if (!reports.contains(report))
				reports.add(report);
		}
	}
	
	private void storyElement(String qName, Attributes attributes) {
		if (qName.equalsIgnoreCase("Attribute") && attributes.getValue(0).equalsIgnoreCase("Number")) {
			isNbr = true;
			return;
		}
		if (qName.equalsIgnoreCase("Attribute") && attributes.getValue(0).equalsIgnoreCase("Name")) {
			isName = true;
			return;
		}
		if (qName.equalsIgnoreCase("Attribute") && attributes.getValue(0).equalsIgnoreCase("Owners.Name")) {
			isBliOwner = true;
			return;
		}
	}
	
	private void taskElement(String qName, Attributes attributes) {
		if (qName.equalsIgnoreCase("Attribute") && attributes.getValue(0).equalsIgnoreCase("Parent.Number")) {
			isNbr = true;
			return;
		}
		if (qName.equalsIgnoreCase("Attribute") && attributes.getValue(0).equalsIgnoreCase("Parent.Name")) {
			isName = true;
			return;
		}
		if (qName.equalsIgnoreCase("Attribute") && attributes.getValue(0).equalsIgnoreCase("Parent.Owners.Name")) {
			isBliOwner = true;
			return;
		}
		if (qName.equalsIgnoreCase("Attribute") && attributes.getValue(0).equalsIgnoreCase("Name")) {
			isTaskName = true;
			return;
		}
		if (qName.equalsIgnoreCase("Attribute") && attributes.getValue(0).equalsIgnoreCase("Owners.Name")) {
			isTaskOwner = true;
			return;
		}
		if (qName.equalsIgnoreCase("Asset") && attributes.getIndex("idref") == 1) {
			String[] attr = attributes.getValue("idref").split(":");
			AssetType asset = AssetType.valueOf(attr[0]);
			report.setBliLink(ReportUtil.createLink(asset, attr[1]));
		}
	}
	
	private void makeLink(String id) {
		String link = ReportUtil.createLink(assetType, id);
		if (Arrays.asList(AssetType.Defect, AssetType.Story).contains(assetType)) {
			report.setBliLink(link.toString());
		} else if (Arrays.asList(AssetType.Test, AssetType.Task).contains(assetType)) {
			report.setTaskLink(link.toString());
		}
	}
}
