package com.akvelon.verifiers;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Properties;

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

	public static void main(String[] args) throws Exception {

		Properties versionProps = Util.loadInternalProperties(VERSION_FILE);

		log.info("ETS verifier v" + versionProps.getProperty(Constants.PROJECT_VERSION));
		log.info("ETS verifier build number " + versionProps.getProperty(Constants.BUILD_NUMBER));

		log.info("Load credentials...");
		Properties credentialProperties = Util.loadProperties(CREDENTIAL_FILE);

		IMailSender mailSender = new MailSender();

		ETSVerifier etsVerifier = new ETSVerifier();
//		List<ETSHourReport> etsReports = etsVerifier.verify(credentialProperties.getProperty(Constants.ETS_LOGIN).trim(),
//				credentialProperties.getProperty(Constants.ETS_PASSWORD).trim());

		V1Verifier v1Verifier = new V1Verifier();
//		List<V1HourReport> v1Reports = v1Verifier.verify(credentialProperties.getProperty(Constants.V1_LOGIN).trim(),
//				credentialProperties.getProperty(Constants.V1_PASSWORD).trim());
		List<V1HourReport> v1Reports = v1Verifier.verify("mseriche","maria");
		
		System.out.println(v1Reports);
		System.out.println(new SimpleDateFormat("yyyy.MM.dd").format(v1Verifier.getSprintBeginDate().getTime()));
	}
}
