package com.akvelon.ets.verifier;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.akvelon.ets.verifier.parser.HtmlParser;
import com.akvelon.ets.verifier.parser.Parser;
import com.akvelon.ets.verifier.reports.PersonalHourReport;
import com.akvelon.ets.verifier.senders.IMailSender;
import com.akvelon.ets.verifier.senders.IRequestSender;
import com.akvelon.ets.verifier.senders.MailSender;
import com.akvelon.ets.verifier.senders.RequestSender;
import com.akvelon.ets.verifier.util.Constants;
import com.akvelon.ets.verifier.util.Holidays;
import com.akvelon.ets.verifier.util.Util;

public class Verifier {

	private static final Logger log = Logger.getLogger(Verifier.class);
	
	private static final String FILE_WITH_ACCOUNTS = "accounts.properties";

	private IRequestSender requestSender;
	private Parser parser;
	private Holidays holidays;
	private IMailSender mailSender;

	public Verifier() throws IOException {
		requestSender = new RequestSender();
		parser = new HtmlParser();
		holidays = new Holidays();
		mailSender = new MailSender();
	}

	@SuppressWarnings("unchecked")
	public void verify(String username, String password) throws IOException {

		Map<String, String> accounts = null;
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
			
			log.info("Loading accounts...");
			accounts = this.loadAccounts4Verify();

			if (accounts.size() == 0) {
				log.info("No accounts available....");
				log.info("Exit...");
				return;
			}
			
			log.info("Accounts: " + accounts);
			
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
		int requiredWorkingHours = calculateWorkingHoursForMonth();

		log.info("Send all hours report to " + username);
		mailSender.sendAllHourReports(Util.convertToEmailAddress(username), requiredWorkingHours, reports);

		log.info("Send missed hours reports...");
		for (PersonalHourReport report : reports) {
			if (report.getHours() < requiredWorkingHours) {
				log.info("Send to " + report.getName());
				mailSender.sendMissedHoursReport(report.getEmail(), requiredWorkingHours, report);
			}
		}
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

	private Map<String,String> loadAccounts4Verify() throws IOException {
		BufferedReader reader = null;
		Properties accountsProps = new Properties();
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(FILE_WITH_ACCOUNTS)));
			accountsProps.load(reader);
		} finally {
			reader.close();
		}
		Map<String,String> accounts = new HashMap<String,String>(accountsProps.size());
		for (Entry<Object, Object> entry : accountsProps.entrySet()) {
			accounts.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
		}
		return accounts;
	}
	
	private int calculateWorkingHoursForMonth() {

		int daysQuantity = 0;
		Calendar startDate = Util.getBeginDateOfMonth();
		Calendar endDate = Util.getEndDateOfTheMonth();
		endDate.add(Calendar.DATE, 1);

		List<Integer> weekends = Arrays.asList(Calendar.SATURDAY, Calendar.SUNDAY);
		while (startDate.before(endDate)) {
			if (!weekends.contains(startDate.get(Calendar.DAY_OF_WEEK))) {
				daysQuantity++;
			}
			startDate.add(Calendar.DATE, 1);
		}

		if (holidays.hasMonthHolidays(startDate)) {
			daysQuantity -= holidays.getAmountOfHolidaysForMonth(startDate);
		}

		log.info("Amount of working days: " + daysQuantity);

		int amountOfWorkingHours = daysQuantity * 8;

		log.info("Amount of working hours: " + amountOfWorkingHours);

		return amountOfWorkingHours;
	}
}
