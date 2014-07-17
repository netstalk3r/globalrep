package com.akvelon.verifiers;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.akvelon.verifiers.parser.ETSParser;
import com.akvelon.verifiers.parser.HtmlParser;
import com.akvelon.verifiers.reports.ETSHourReport;
import com.akvelon.verifiers.senders.ETSRequestSender;
import com.akvelon.verifiers.senders.IETSRequestSender;
import com.akvelon.verifiers.util.Constants;
import com.akvelon.verifiers.util.Util;

public class ETSVerifier {

	private static final Logger log = Logger.getLogger(ETSVerifier.class);

	private static final String FILE_WITH_ACCOUNTS = "accounts.properties";
	

	private IETSRequestSender requestSender;
	private ETSParser parser;

	private int requiredWorkingHours = 0;
	
	public ETSVerifier() throws IOException {
		requestSender = new ETSRequestSender();
		parser = new HtmlParser();
	}

	public int getRequiredWorkingHours() {
		return requiredWorkingHours;
	}



	@SuppressWarnings("unchecked")
	public List<ETSHourReport> verify(String username, String password) throws IOException {

		log.info("Loading accounts...");
		Map<String, String> accounts = this.loadAccounts4Verify();
		
		if (this.isEmptyMap(accounts,"accounts")) {
			return null;
		}
		
		log.info("Accounts: " + accounts);
		
		boolean isLogin = false;
		
		List<ETSHourReport> reports = null;
		
		try {
			if (Constants.RESPONSE_CODE_OK != requestSender.openSession()) {
				log.error("Server response code is not OK!");
				log.error("Exit...");
				return null;
			}

			InputStream is = requestSender.login(username, password);
			try {
				isLogin = parser.isLogin(is);
				if (!isLogin) {
					log.error("Login failed...");
					log.error("Exit...");
					return null;
				}
				log.info("Login succeed...");
			} finally {
				is.close();
			}
			
			log.info("Get notified hours");
			Map<String, String> params = Util.getDefaultRequestParams();
			List<ETSHourReport> notifiedHours = this.getReportedHours(accounts, params);

			log.info("Get accepted hours");
			params.put(Constants.STATUS, Constants.STATUS_ACCEPTED_OPTION);
			List<ETSHourReport> acceptedHours = this.getReportedHours(accounts, params);

			reports = this.getAllHours(Arrays.asList(notifiedHours, acceptedHours));
		} finally {
			if (isLogin) {
				requestSender.closeSession();
			}
		}
		
		requiredWorkingHours = Util.calculateWorkingHoursBetweenDates(Util.getBeginDateOfMonth(), Util.getToday(),Constants.ETS_HOURS_PER_DAY);
		
		return reports;
	}

	private List<ETSHourReport> getReportedHours(Map<String, String> accounts, Map<String, String> params) throws IOException {
		List<ETSHourReport> reports = new ArrayList<ETSHourReport>();
		InputStream html = null;
		ETSHourReport report;
		for (Entry<String, String> account : accounts.entrySet()) {
			params.put(Constants.ACCOUNT, account.getKey());
			html = requestSender.sendRequest(Constants.REPORTED_HOURS_URL, params);
			report = new ETSHourReport();
			report.setName(account.getValue());
			report.setEmail(Util.convertToEmailAddress(account.getKey()));
			report.setHours(parser.parseReportedHours(html));
			reports.add(report);
			html.close();
			log.info(report.getName() + " - " + report.getHours());
		}
		return reports;
	}

	private List<ETSHourReport> getAllHours(List<List<ETSHourReport>> allReports) {
		if (allReports.size() == 0)
			throw new IllegalStateException("No reports to connect!");
		if (allReports.size() == 1)
			return allReports.get(0);
		List<ETSHourReport> first = allReports.get(0);
		for (int i = 1; i < allReports.size(); i++) {
			for (ETSHourReport hourReport : allReports.get(i)) {
				int index = first.indexOf(hourReport);
				ETSHourReport personalHourReport = first.get(index);
				personalHourReport.setHours(personalHourReport.getHours() + hourReport.getHours());
			}
		}
		return first;
	}

	private Map<String, String> loadAccounts4Verify() throws IOException {
		Properties accountsProps = Util.loadProperties(FILE_WITH_ACCOUNTS);
		Map<String, String> accounts = new HashMap<String, String>(accountsProps.size());
		for (Entry<Object, Object> entry : accountsProps.entrySet()) {
			accounts.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()).trim());
		}
		return accounts;
	}

	private boolean isEmptyMap(Map<?,?> map, String message) {
		boolean isEmpty = map.isEmpty();
		if (isEmpty) {
			log.info("No " + message + " available....");
			log.info("Exit...");
		}
		return isEmpty;
	}
}
