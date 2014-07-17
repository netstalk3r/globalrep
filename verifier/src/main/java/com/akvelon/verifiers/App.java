package com.akvelon.verifiers;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message.RecipientType;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.akvelon.verifiers.reports.ETSHourReport;
import com.akvelon.verifiers.reports.V1HourReport;
import com.akvelon.verifiers.senders.IMailSender;
import com.akvelon.verifiers.senders.MailSender;
import com.akvelon.verifiers.util.Constants;
import com.akvelon.verifiers.util.Util;

public class App {

	private static final Logger log = Logger.getLogger(App.class);

	private static final String CREDENTIAL_FILE = "credentials.properties";
	private static final String VERSION_FILE = "version.properties";
	private static final String FILE_WITH_RECIPIENTS = "recipients.properties";

	public static void main(String[] args) throws Exception {

		Properties versionProps = Util.loadInternalProperties(VERSION_FILE);

		log.info("ETS verifier v" + versionProps.getProperty(Constants.PROJECT_VERSION));
		log.info("ETS verifier build number " + versionProps.getProperty(Constants.BUILD_NUMBER));

		log.info("Load credentials...");
		Properties credentialProperties = Util.loadProperties(CREDENTIAL_FILE);
		
		log.info("Load recipients...");
		Map<RecipientType, String> recipients = loadRecipients();

		IMailSender mailSender = new MailSender();

		ETSVerifier etsVerifier = new ETSVerifier();
		log.info("Verify ETS...");
		List<ETSHourReport> etsReports = etsVerifier.verify(credentialProperties.getProperty(Constants.ETS_LOGIN).trim(),
				credentialProperties.getProperty(Constants.ETS_PASSWORD).trim());

		V1Verifier v1Verifier = new V1Verifier();
		log.info("Verify V1...");
		List<V1HourReport> v1Reports = v1Verifier.verify(credentialProperties.getProperty(Constants.V1_LOGIN).trim(),
				credentialProperties.getProperty(Constants.V1_PASSWORD).trim());
		
		Collections.sort(etsReports);
		Collections.sort(v1Reports);
		
		int etsRequiredWorkingHours = etsVerifier.getRequiredWorkingHours();
		int v1RequiredWorkingHours = v1Verifier.getRequiredWorkingHours();
		
		Map<Integer,Object> params = new HashMap<Integer, Object>();
		
		params.put(Constants.V1_REQUIRED_HOURS, Integer.valueOf(v1RequiredWorkingHours));
		params.put(Constants.V1_HOUR_REPORTS, v1Reports);
		params.put(Constants.ETS_REQUIRED_HOURS, Integer.valueOf(etsRequiredWorkingHours));
		params.put(Constants.ETS_HOUR_REPORTS, etsReports);
		
		log.info("Send all hours report...");
		mailSender.sendAllHourReports(recipients.get(RecipientType.TO), recipients.get(RecipientType.CC), params);
		
		log.info("Send missed hours reports...");
		V1HourReport v1Report = null;
		for (ETSHourReport etsReport : etsReports) {
			for (int i = 0; i < v1Reports.size(); i++) {
				if (etsReport.getName().equals(v1Reports.get(i).getName())) {
					v1Report = v1Reports.get(i);
					break;
				}
			}
			if (etsReport.getHours() < etsRequiredWorkingHours || (v1Report != null && v1Report.getReportedHours() < v1RequiredWorkingHours)) {
				params = new HashMap<Integer, Object>();
				params.put(Constants.V1_REQUIRED_HOURS, Integer.valueOf(v1RequiredWorkingHours));
				params.put(Constants.ETS_REQUIRED_HOURS, Integer.valueOf(etsRequiredWorkingHours));
				params.put(Constants.V1_REPORTED_HOURS, v1Report != null ? Double.valueOf(v1Report.getReportedHours()) : null);
				params.put(Constants.ETS_REPORTED_HOURS, Double.valueOf(etsReport.getHours()));
				log.info("Send message to " + etsReport.getName());
				mailSender.sendMissedHoursReport(etsReport.getEmail(), params);
				v1Report = null;
			}
		}
		
	}
	
	private static Map<RecipientType, String> loadRecipients() throws IOException {
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
}
