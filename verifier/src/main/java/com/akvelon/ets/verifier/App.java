package com.akvelon.ets.verifier;

import java.util.Properties;

import com.akvelon.ets.verifier.util.Constants;
import com.akvelon.ets.verifier.util.Util;

public class App {

	private static final String CREDENTIAL_FILE = "credentials.properties";

	public static void main(String[] args) throws Exception {

		Properties credentialProperties = Util.loadProperties(CREDENTIAL_FILE);

		Verifier verifier = new Verifier();
		verifier.verify(credentialProperties.getProperty(Constants.LOGIN).trim(), credentialProperties.getProperty(Constants.PASSWORD)
				.trim());
	}
}
