package com.akvelon.verifiers.util;

import java.io.IOException;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

public class V1UrlBuilder {

	private static final String V1_INTERNAL_PROPERTIES = "v1_internal.properties";
	private String url;

	private static final String ASSET_ACTUAL = "Actual";
	private static final String ASSET_TIMEBOX = "Timebox";

	private static final String SELECT_ACTUAL = "Value,Member.Name&sort=Member.Nickname";
	private static final String SELECT_TIMEBOX = "BeginDate";

	public V1UrlBuilder() throws IOException {
		super();
		Properties internalProperties = Util.loadInternalProperties(V1_INTERNAL_PROPERTIES);
		url = internalProperties.getProperty(Constants.V1_URL);
	}

	public String buildUrlActual(Properties urlProps) throws IOException {
		StringBuilder parameters = new StringBuilder();

		for (Entry<Object, Object> entry : urlProps.entrySet()) {
			parameters.append(String.valueOf(entry.getKey())).append("=").append(decode(String.valueOf(entry.getValue()))).append(";");
		}
		// removes last ";"
		parameters.deleteCharAt(parameters.length() - 1);

		return String.format(url, ASSET_ACTUAL, SELECT_ACTUAL, parameters.toString());
	}

	public String buildUrlTimebox(Properties urlProps) throws IOException {
		StringBuilder parameters = new StringBuilder();
		for (Entry<Object, Object> entry : urlProps.entrySet()) {
			if (!StringUtils.equals("Member.Nickname", String.valueOf(entry.getKey()))) {
				parameters.append(String.valueOf(entry.getKey())).append("=").append(decode(String.valueOf(entry.getValue()))).append(";");
			}
		}
		// removes last ";"
		parameters.deleteCharAt(parameters.length() - 1);
		
		return String.format(url, ASSET_TIMEBOX, SELECT_TIMEBOX, parameters.toString());
	}

	private String decode(String str) {
		return str.replace(" ", "%20").replace(">", "%3E").replace("<", "%3C");
	}

}
