package com.akvelon.verifiers.senders;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.apache.log4j.Logger;

import com.akvelon.verifiers.util.Constants;

public class V1RequestSender implements IV1RequestSender {

	private static final Logger log = Logger.getLogger(V1RequestSender.class);

	@Override
	public InputStream sendRequest(String urlWithParams) throws IOException {

		log.debug("Send request -> Url: " + urlWithParams);

		URL obj = new URL(urlWithParams);
		HttpURLConnection con = (HttpsURLConnection) obj.openConnection();
		con.setUseCaches(false);
		con.setRequestMethod(Constants.GET_REQUEST);

		log.info("Server response code - " + con.getResponseCode());

		return con.getInputStream();
	}

}
