package com.akvelon.verifiers.senders;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import com.akvelon.verifiers.util.Constants;

public class V1RequestSender implements IV1RequestSender {

	private static final Logger log = Logger.getLogger(V1RequestSender.class);

	@Override
	public InputStream sendRequest(String login, String password, String urlWithParams) throws IOException {
		log.debug("Send request -> Url: " + urlWithParams);

		String authString = login + ":" + password;
		byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
		String authStringEnc = new String(authEncBytes);
		
		URL obj = new URL(urlWithParams);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
		con.setUseCaches(false);
		con.setRequestMethod(Constants.GET_REQUEST);
		con.setRequestProperty("Authorization", "Basic " + authStringEnc);

		log.debug("Response Code : " + con.getResponseCode() + " Response Message : " + con.getResponseMessage());

		return con.getInputStream();
	}

}
