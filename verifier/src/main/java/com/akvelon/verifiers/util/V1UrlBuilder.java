package com.akvelon.verifiers.util;

import java.io.IOException;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

public class V1UrlBuilder {

	private static final String V1_INTERNAL_PROPERTIES = "v1_internal.properties";

	public static String buildUrl(Properties urlProps) throws IOException {

		Properties internalProperties = Util.loadInternalProperties(V1_INTERNAL_PROPERTIES);
		String url = internalProperties.getProperty(Constants.V1_URL);

		StringBuilder parameters = new StringBuilder();
		String key = null;
		for (Entry<Object, Object> entry : urlProps.entrySet()) {
			key = String.valueOf(entry.getKey());
			if (!StringUtils.equals("Asset", key) && !StringUtils.equals("sel", key)) {
				parameters.append(key).append("=").append(decode(String.valueOf(entry.getValue()))).append(";");
			}
		}
		// removes last ";"
		parameters.deleteCharAt(parameters.length() - 1);

		return String.format(url, urlProps.getProperty("Asset"), urlProps.getProperty("sel"), parameters.toString());
	}

	private static String decode(String str) {
		return str.replace(" ", "%20").replace(">", "%3E").replace("<", "%3C");
	}

}
