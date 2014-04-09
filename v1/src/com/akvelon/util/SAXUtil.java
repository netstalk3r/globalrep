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
	private boolean isEstimate = false;
	
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
		if (qName.equals("Asset") && attributes.getIndex("id") == 1) {
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
			case TestSet:
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
			return;
		}
		if (isName) {
			report.setBliName(new String(ch, start, length));
			return;
		}
		if (isBliOwner) {
			report.setBliOwner(new String(ch, start, length));
			return;
		}
		if (isTaskName) {
			report.setTaskName(new String(ch, start, length));
			return;
		}
		if (isTaskOwner) {
			report.setTaskOwner(new String(ch, start, length));
			return;
		}
		if (isEstimate) {
			report.setStoryPoints(new String(ch, start, length));
			return;
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (qName.equals("Asset")) {
			if (!reports.contains(report))
				reports.add(report);
		}
		if (isNbr) {
			isNbr = false;
			return;
		}
		if (isName) {
			isName = false;
			return;
		}
		if (isBliOwner) {
			isBliOwner = false;
			return;
		}
		if (isTaskName) {
			isTaskName = false;
			return;
		}
		if (isTaskOwner) {
			isTaskOwner = false;
			return;
		}
		if (isEstimate) {
			isEstimate = false;
			return;
		}
	}
	
	private void storyElement(String qName, Attributes attributes) {
		if (qName.equals("Attribute") && attributes.getValue(0).equals("Number")) {
			isNbr = true;
			return;
		}
		if (qName.equals("Attribute") && attributes.getValue(0).equals("Name")) {
			isName = true;
			return;
		}
		if (qName.equals("Attribute") && attributes.getValue(0).equals("Owners.Name")) {
			isBliOwner = true;
			return;
		}
		if (qName.equals("Attribute") && attributes.getValue(0).equals("Estimate")) {
			isEstimate = true;
			return;
		} 
	}
	
	private void taskElement(String qName, Attributes attributes) {
		if (qName.equals("Attribute") && attributes.getValue(0).equals("Parent.Number")) {
			isNbr = true;
			return;
		}
		if (qName.equals("Attribute") && attributes.getValue(0).equals("Parent.Name")) {
			isName = true;
			return;
		}
		if (qName.equals("Attribute") && attributes.getValue(0).equals("Parent.Owners.Name")) {
			isBliOwner = true;
			return;
		}
		if (qName.equals("Attribute") && attributes.getValue(0).equals("Name")) {
			isTaskName = true;
			return;
		}
		if (qName.equals("Attribute") && attributes.getValue(0).equals("Owners.Name")) {
			isTaskOwner = true;
			return;
		}
		if (qName.equals("Asset") && attributes.getIndex("idref") == 1) {
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
