package com.akvelon.verifiers.parser;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.akvelon.verifiers.reports.V1HourReport;

public class DomParser implements V1Parser {

	private static final Logger log = Logger.getLogger(DomParser.class);

	private DocumentBuilder db;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	public DomParser() throws ParserConfigurationException {
		super();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		db = dbf.newDocumentBuilder();
	}

	@Override
	public Calendar parseSprintStartDate(InputStream is) throws Exception {
		Document doc = db.parse(is);
		is.close();
		NodeList nodes = doc.getElementsByTagName("Attribute");

		if (nodes.getLength() == 0) {
			log.error("Cannot parse sprint start date");
			return null;
		}

		Date date = sdf.parse(nodes.item(0).getTextContent());
		Calendar sprintBeginDate = Calendar.getInstance();
		sprintBeginDate.setTime(date);
		return sprintBeginDate;

	}

	@Override
	public List<V1HourReport> parseReportedHours(InputStream is) throws Exception {
		Document doc = db.parse(is);
		is.close();
		NodeList nodes = doc.getElementsByTagName("Asset");

		if (nodes.getLength() == 0) {
			log.error("No report hours aviable.");
			return null;
		}

		Map<String, V1HourReport> hours = new HashMap<String, V1HourReport>();
		String teamMember = null;
		V1HourReport report = null;
		NodeList attributes = null;
		double hour = 0;

		for (int i = 0; i < nodes.getLength(); i++) {
			attributes = ((Element) nodes.item(i)).getElementsByTagName("Attribute");

			for (int j = 0; j < attributes.getLength(); j++) {
				Element item = (Element) attributes.item(j);
				if ("Value".equals(item.getAttribute("name"))) {
					hour = Double.parseDouble(item.getTextContent());
				} else {
					teamMember = item.getTextContent();
				}
			}

			if (hours.containsKey(teamMember)) {
				hours.get(teamMember).setReportedHours(hours.get(teamMember).getReportedHours() + hour);
			} else {
				report = new V1HourReport();
				report.setName(teamMember);
				report.setReportedHours(hour);
				hours.put(teamMember, report);
			}
		}

		return new ArrayList<V1HourReport>(hours.values());
	}

}
