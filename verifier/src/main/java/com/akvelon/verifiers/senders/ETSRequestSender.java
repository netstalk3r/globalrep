package com.akvelon.verifiers.senders;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.lang.CharEncoding;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.log4j.Logger;

import com.akvelon.verifiers.util.Constants;


public class ETSRequestSender implements IETSRequestSender {

	private static final Logger log = Logger.getLogger(ETSRequestSender.class);

	private static final String CONNECTION = "keep-alive";
	private static final String CONTENT_TYPE = "application/x-www-form-urlencoded";
	private static final String ACCEPT = "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8";
	private static final String HOST = "ua.ets.akvelon.com";
	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:30.0) Gecko/20100101 Firefox/30.0";
	private static final String ACCEPT_LANGUAGE = "en-US,en;q=0.5";

	private static final String URL_MAIN = "https://ua.ets.akvelon.com";
	private static final String URL_LOGON = "https://ua.ets.akvelon.com/logon.ets";
	private static final String URL_LOGOUT = "https://ua.ets.akvelon.com/logout.ets";

	private static String LOGON_REFER = null;
	private static final String ACCOUNT_MANAGE_REFER = "https://ua.ets.akvelon.com/accountreport.ets";

	private String jSessionId = null;

	private String username;
	
	private static final int OK_RESPONSE_CODE = 200;

	public ETSRequestSender() {
		CookieHandler.setDefault(new CookieManager());
	}

	public InputStream sendRequest(String url, Map<String, String> params) throws IOException {

		log.debug("Send request -> Url: " + url);

		HttpsURLConnection con = this.createConnection(url, Constants.POST_REQUEST);
		con.setRequestProperty("Referer", ACCOUNT_MANAGE_REFER);
		con.setRequestProperty("Content-Type", CONTENT_TYPE);

		StringBuilder parameters = new StringBuilder();
		for (Entry<String, String> param : params.entrySet()) {
			parameters.append(param.getKey()).append("=").append(param.getValue()).append("&");
		}
		// removes last "&"
		parameters.deleteCharAt(parameters.length() - 1);
		
		con.setRequestProperty("Content-Lenght", String.valueOf(parameters.length()));

		log.debug("Parameters: " + parameters);

		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(parameters.toString());
		wr.flush();
		wr.close();

		log.debug("Response Code : " + con.getResponseCode() + " Response Message : " + con.getResponseMessage());

		// do not forger to close input stream
		return con.getInputStream();
	}

	public int openSession() throws IOException {
		log.info("Openning new session...");
		HttpsURLConnection con = this.createConnection(URL_MAIN, Constants.GET_REQUEST);
		con.setRequestProperty("Content-Type", CharEncoding.UTF_8);

		log.debug("Response Code : " + con.getResponseCode() + " Response Message : " + con.getResponseMessage());

		jSessionId = this.getJSessionID(con.getHeaderFields().get("Set-Cookie"));
		log.info(jSessionId);

		if (OK_RESPONSE_CODE == con.getResponseCode()) {
			this.createLogonRefer();
		}
		
		if (log.isDebugEnabled()) {
			this.logResponse(con.getInputStream());
		}
		return con.getResponseCode();
	}

	public InputStream login(String username, String password) throws IOException {
		log.info("Logining for user: " + username);
		HttpsURLConnection con = this.createConnection(URL_LOGON, Constants.POST_REQUEST);
		con.setRequestProperty("Referer", LOGON_REFER);
		con.setRequestProperty("Content-Type", CONTENT_TYPE);

		StringBuilder credentials = new StringBuilder("username=").append(username).append("&").append("password=").append(password);

		con.setRequestProperty("Content-Lenght", String.valueOf(credentials.length()));

		// Send post request
		con.setDoOutput(true);
		con.setDoInput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(credentials.toString());
		wr.flush();
		wr.close();

		log.debug("Response Code : " + con.getResponseCode() + " Response Message : " + con.getResponseMessage());
		
		this.username = username;
		return con.getInputStream();
	}

	public int closeSession() throws IOException {
		log.info("Logout for user " + this.username);
		HttpsURLConnection con = this.createConnection(URL_LOGOUT, Constants.GET_REQUEST);
		con.setRequestProperty("Referer", ACCOUNT_MANAGE_REFER);
		
		log.debug("Response Code : " + con.getResponseCode() + " Response Message : " + con.getResponseMessage());

		if (log.isDebugEnabled()) {
			this.logResponse(con.getInputStream());
		}
		return con.getResponseCode();
	}

	private String getJSessionID(List<String> cookies) {
		for (String cookie : cookies) {
			String jsessionId = cookie.split(";")[0];
			if (jsessionId.matches("^[jsessionid|JSESSIONID].*")) {
				return jsessionId;
			}
		}
		return null;
	}

	private HttpsURLConnection createConnection(String url, String requestMethod) throws IOException {
		URL obj = new URL(url);
		HttpsURLConnection.setDefaultHostnameVerifier(new AllowAllHostnameVerifier());
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
		con.setUseCaches(false);
		con.setRequestMethod(requestMethod);
		con.setRequestProperty("Host", HOST);
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept", ACCEPT);
		con.setRequestProperty("Accept-Language", ACCEPT_LANGUAGE);
		con.setRequestProperty("Connection", CONNECTION);
		if (jSessionId != null) {
			con.setRequestProperty("Cookie", jSessionId);
		}
		return con;
	}

	private void createLogonRefer() {
		LOGON_REFER = new StringBuilder(URL_LOGON).append(";").append(this.jSessionId).toString();
	}
	
	private void logResponse(InputStream is) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		// print result
		log.debug(response.toString());
	}
}
