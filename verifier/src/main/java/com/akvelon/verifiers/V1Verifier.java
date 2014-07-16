package com.akvelon.verifiers;

import java.io.InputStream;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.akvelon.verifiers.parser.DomParser;
import com.akvelon.verifiers.parser.V1Parser;
import com.akvelon.verifiers.reports.V1HourReport;
import com.akvelon.verifiers.senders.IV1RequestSender;
import com.akvelon.verifiers.senders.V1RequestSender;
import com.akvelon.verifiers.util.Util;
import com.akvelon.verifiers.util.V1UrlBuilder;

public class V1Verifier {
	
	private static final Logger log = Logger.getLogger(V1Verifier.class);

	private static final String ACTUAL_FILE = "actual.properties";
	private static final String SPRINT_FILE = "sprint_start_date.properties";
	
	private V1Parser parser;
	private IV1RequestSender requestSender;
	
	private Calendar sprintBeginDate;
	
	public V1Verifier() throws Exception {
		super();
		parser = new DomParser();
		requestSender = new V1RequestSender();
	}
	
	public Calendar getSprintBeginDate() {
		return sprintBeginDate;
	}

	public List<V1HourReport> verify(String login, String password) throws Exception {
		
		log.info("Load actual properties...");
		Properties actualProperties = Util.loadProperties(ACTUAL_FILE);

		if (actualProperties.isEmpty()) {
			log.error("Actual properties can not be loaded");
			log.error("Exit...");
			return null;
		}
		
		log.info("Load sprint properties...");
		Properties sprintProperties = Util.loadProperties(SPRINT_FILE);
		
		if (sprintProperties.isEmpty()) {
			log.error("Sprint properties can not be loaded");
			log.error("Exit...");
			return null;
		}
		
		InputStream response = null;
		try {
			response = requestSender.sendRequest(login, password, V1UrlBuilder.buildUrl(sprintProperties));
			sprintBeginDate = parser.parseSprintStartDate(response);
		} finally {
			response.close();
		}
		
		List<V1HourReport> reports = null;
		
		try {
			response = requestSender.sendRequest(login, password, V1UrlBuilder.buildUrl(actualProperties));
			reports = parser.parseReportedHours(response);
		} finally {
			response.close();
		}
		
		return reports;
	}
	
}
