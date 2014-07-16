package com.akvelon.verifiers;

import java.util.Properties;

import org.apache.log4j.Logger;

import com.akvelon.verifier.util.Constants;
import com.akvelon.verifier.util.Util;

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

		ETSVerifier verifier = new ETSVerifier();
		verifier.verify(credentialProperties.getProperty(Constants.LOGIN).trim(), credentialProperties.getProperty(Constants.PASSWORD)
				.trim());
	}
}
