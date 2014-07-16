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

import javax.mail.Message.RecipientType;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.akvelon.verifier.parser.HtmlParser;
import com.akvelon.verifier.parser.Parser;
import com.akvelon.verifier.reports.PersonalHourReport;
import com.akvelon.verifier.senders.IMailSender;
import com.akvelon.verifier.senders.IRequestSender;
import com.akvelon.verifier.senders.RequestSender;
import com.akvelon.verifier.util.Constants;
import com.akvelon.verifier.util.Util;

public class ETSVerifier {

	private static final Logger log = Logger.getLogger(ETSVerifier.class);

	private static final String FILE_WITH_ACCOUNTS = "accounts.properties";
	private static final String FILE_WITH_RECIPIENTS = "recipients.properties";

	private IRequestSender requestSender;
	private Parser parser;
	private IMailSender mailSender;

	public ETSVerifier() throws IOException {
		requestSender = new RequestSender();
		parser = new HtmlParser();
	}

	public void setMailSender(IMailSender mailSender) {
		this.mailSender = mailSender;
	}

	@SuppressWarnings("unchecked")
	public void verify(String username, String password) throws IOException {

		log.info("Loading accounts...");
		Map<String, String> accounts = this.loadAccounts4Verify();
		
		if (this.isEmptyMap(accounts,"accounts")) {
			return;
		}
		
		log.info("Accounts: " + accounts);
		
		log.info("Load recipients...");
		Map<RecipientType, String> recipients = this.loadRecipients();
		
		if (this.isEmptyMap(recipients,"recipients")) {
			return;
		}
		
		List<PersonalHourReport> reports = null;

		try {
			if (Constants.RESPONSE_CODE_OK != requestSender.openSession()) {
				log.error("Server response code is not OK!");
				log.error("Exit...");
				return;
			}

			InputStream is = requestSender.login(username, password);
			try {
				if (!parser.isLogin(is)) {
					log.error("Login failed...");
					log.error("Exit...");
					return;
				}
				log.info("Login succeed...");
			} finally {
				is.close();
			}

			reports = new ArrayList<PersonalHourReport>();

			log.info("Get notified hours");
			Map<String, String> params = Util.getDefaultRequestParams();
			List<PersonalHourReport> notifiedHours = this.getReportedHours(accounts, params);

			log.info("Get accepted hours");
			params.put(Constants.STATUS, Constants.STATUS_ACCEPTED_OPTION);
			List<PersonalHourReport> acceptedHours = this.getReportedHours(accounts, params);

			reports = this.getAllHours(Arrays.asList(notifiedHours, acceptedHours));
		} finally {
			requestSender.closeSession();
		}
		int requiredWorkingHours = Util.calculateWorkingHoursBetweenDates(Util.getBeginDateOfMonth(), Util.getToday());

		log.info("Send all hours report...");
//		mailSender.sendAllHourReports(recipients.get(RecipientType.TO), recipients.get(RecipientType.CC), requiredWorkingHours, reports);
//
//		log.info("Send missed hours reports...");
//		for (PersonalHourReport report : reports) {
//			if (report.getHours() < requiredWorkingHours) {
//				log.info("Send to " + report.getName());
//				mailSender.sendMissedHoursReport(report.getEmail(), requiredWorkingHours, report);
//			}
//		}
	}

	private List<PersonalHourReport> getReportedHours(Map<String, String> accounts, Map<String, String> params) throws IOException {
		List<PersonalHourReport> reports = new ArrayList<PersonalHourReport>();
		InputStream html = null;
		PersonalHourReport report;
		for (Entry<String, String> account : accounts.entrySet()) {
			params.put(Constants.ACCOUNT, account.getKey());
			html = requestSender.sendRequest(Constants.REPORTED_HOURS_URL, params);
			report = new PersonalHourReport();
			report.setName(account.getValue());
			report.setEmail(Util.convertToEmailAddress(account.getKey()));
			report.setHours(parser.parseReportedHours(html));
			reports.add(report);
			html.close();
			log.info(report.getName() + " - " + report.getHours());
		}
		return reports;
	}

	private List<PersonalHourReport> getAllHours(List<List<PersonalHourReport>> allReports) {
		if (allReports.size() == 0)
			throw new IllegalStateException("No reports to connect!");
		if (allReports.size() == 1)
			return allReports.get(0);
		List<PersonalHourReport> first = allReports.get(0);
		for (int i = 1; i < allReports.size(); i++) {
			for (PersonalHourReport hourReport : allReports.get(i)) {
				int index = first.indexOf(hourReport);
				PersonalHourReport personalHourReport = first.get(index);
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

	private Map<RecipientType, String> loadRecipients() throws IOException {
		Properties recipientsProps = Util.loadProperties(FILE_WITH_RECIPIENTS);
		if (StringUtils.isBlank(recipientsProps.getProperty(Constants.RECIPIENT_TO))) {
			throw new IllegalArgumentException("Recipiet to cannot be null. Verify " + FILE_WITH_RECIPIENTS + "file");
		}
		Map<RecipientType, String> recipients = new HashMap<RecipientType, String>(2);
		log.info("TO : " + recipientsProps.getProperty(Constants.RECIPIENT_TO));
		recipients.put(RecipientType.TO, recipientsProps.getProperty(Constants.RECIPIENT_TO));
		if (StringUtils.isNotBlank(recipientsProps.getProperty(Constants.RECIPIENT_CC))) {
			log.info("CC : " + recipientsProps.getProperty(Constants.RECIPIENT_CC));
			recipients.put(RecipientType.CC, recipientsProps.getProperty(Constants.RECIPIENT_CC));
		}
		return recipients;
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
