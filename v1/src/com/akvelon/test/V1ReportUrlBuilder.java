package com.akvelon.test;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.commons.codec.binary.Base64;


public class V1ReportUrlBuilder {

	private Properties connectionProps = new Properties();
	private Properties teamProps = new Properties();
	private final String reportStoragePath = "reports/";
	private String delim = ";";
	
	public V1ReportUrlBuilder() throws IOException {
		readProps();
	}
	
	public URLConnection buildSecuredReportUrl(String reportName) throws IOException {

		String authString = connectionProps.getProperty("user") + ":" + connectionProps.getProperty("pass");
		byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
		String authStringEnc = new String(authEncBytes);

		String stringurl = this.buildReportUrl(reportName);
		stringurl = stringurl.replace(" ", "%20").replace(">", "%3E").replace("<", "%3C");
		if (reportName.endsWith("all_done.properties")) {
			stringurl = stringurl.replace("Owners", "Member");
		}
//		System.out.println(stringurl);
		URL url = new URL(stringurl);
		URLConnection urlConnection = url.openConnection();
		urlConnection.setRequestProperty("Authorization", "Basic " + authStringEnc);
		// add parameters
		
		return urlConnection;
	}
	/**
	 * Builds URL to get xml data from V1 restricted by properties.
	 * @param reportPropsPath name of the file where the report properties are stored
	 * @return url in format http://host:port/VersionOne?sel=...&where=...
	 * @throws IOException 
	 */
	public String buildReportUrl(String reportName) throws IOException {
		String url = "";
		String urlTemplate = "%s://%s/%s/%s?sel=%s&where=%s";
		Properties reportProps = new Properties();
		reportProps.load(this.getClass().getClassLoader().getResourceAsStream(this.reportStoragePath + reportName));
		
		url = String.format(urlTemplate,
				connectionProps.getProperty("protocol"),
				connectionProps.getProperty("host"),
				connectionProps.getProperty("app"),
				reportProps.getProperty("AssetType"),
				reportProps.getProperty("sel"),
				this.buildReportRestriction(reportProps));
	
		return url;
	}

	private void readProps() throws IOException {
		connectionProps.load(this.getClass().getClassLoader().getResourceAsStream("connection.properties"));
		teamProps.load(this.getClass().getClassLoader().getResourceAsStream("team.properties"));
	}
	
	private String buildReportRestriction(Properties reportProps) {
		StringBuilder sb = new StringBuilder();
		sb.append(this.restrictTeam(reportProps.getProperty("teamOwnership"))).append(delim);
		
		Enumeration<?> keys = reportProps.propertyNames();
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			if(!Arrays.asList("sel","AssetType","teamOwnership").contains(key)) {
	            //get the property value and print it out
	            sb.append(key);
	            //if (!"ToDo".equals(key)){
	            if (!Arrays.asList("<",">","!").contains(reportProps.get(key).toString().substring(0, 1))){
	            	sb.append("=");
	            }
	            sb.append(reportProps.getProperty(key));
	            if(keys.hasMoreElements()) {
	            	sb.append(delim);
	            }
			}
		}
		if (sb.toString().lastIndexOf(delim) == sb.toString().length()-1) {
			sb.deleteCharAt(sb.length()-1);
		}
		return sb.toString();
	}
	
	private String restrictTeam(String teamOwnership) {
		StringBuilder sb = new StringBuilder();
		 
		if (teamOwnership != null && !"".equals(teamOwnership)) {
			sb.append(teamOwnership).append("=").append(teamProps.getProperty("team")).append(delim);
		}
		
		Enumeration<?> keys = teamProps.propertyNames();
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
            //get the property value and print it out
			if (!"team".equals(key)) {
	            sb.append(key).append("=").append(teamProps.getProperty(key));
	            if(keys.hasMoreElements()) {
	            	sb.append(delim);
	            }
			}
		}
    		
    	return sb.toString();
	}
}
