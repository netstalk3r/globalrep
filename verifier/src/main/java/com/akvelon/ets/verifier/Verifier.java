package com.akvelon.ets.verifier;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import javax.mail.Message.RecipientType;

import org.apache.commons.lang.StringUtils;
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
	private static final String FILE_WITH_RECIPIENTS = "recipients.properties";

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
		int requiredWorkingHours = calculateWorkingHoursBetweenDates(Util.getBeginDateOfMonth(), Util.getToday());

		log.info("Send all hours report...");
		mailSender.sendAllHourReports(recipients.get(RecipientType.TO), recipients.get(RecipientType.CC), requiredWorkingHours, reports);

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
	
	private int calculateWorkingHoursBetweenDates(Calendar startDate, Calendar endDate) {

		boolean includeToday = this.includeToday(endDate);
		
		log.info("Calculate working hours between " + Util.dateToString(startDate.getTime()) + " and "
				+ Util.dateToString(endDate.getTime()));

		int daysQuantity = 0;
		
		if (holidays.hasMonthHolidays(startDate)) {
			daysQuantity -= holidays.getAmountOfHolidaysBetweenDatesWithinMonth(startDate, endDate);
		}
		
		endDate.add(Calendar.DATE, 1);

		while (startDate.before(endDate)) {
			if (!isWeekend(startDate.get(Calendar.DAY_OF_WEEK))) {
				daysQuantity++;
			}
			startDate.add(Calendar.DATE, 1);
		}

		log.info("This day is" + (includeToday ? "" : " not") + " included.");
		if (!includeToday) {
			daysQuantity--;
		}
		

		log.info("Amount of working days: " + daysQuantity);

		int amountOfWorkingHours = daysQuantity * 8;

		log.info("Amount of working hours: " + amountOfWorkingHours);

		return amountOfWorkingHours;
	}

	private boolean isWeekend(int date) {
		return (date == Calendar.SATURDAY) || (date == Calendar.SUNDAY);
	}
	
	// if the current time is 7 PM or later  this day is included to count of working hours
	private boolean includeToday(Calendar today) {
		return today.get(Calendar.AM_PM) == Calendar.PM ? today.get(Calendar.HOUR) >= 7 : false;
	}
}
