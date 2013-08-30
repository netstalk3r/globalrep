package com.test.parseXML.http;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;

import com.test.parseXML.App;

public class Request {
	
	private Integer statusCode;

	private UsernamePasswordCredentials creds;
	private HttpClient client;
	private GetMethod method;

	public Request() {
		String usrPwd = App.setting.getProperty("usr.pwd");
		creds = new UsernamePasswordCredentials(usrPwd, usrPwd);
		client = new HttpClient();
		client.getState().setCredentials(AuthScope.ANY, creds);
		method = new GetMethod(App.setting.getProperty("request.url"));
	}

	public InputStream send() throws HttpException, IOException {
		statusCode = client.executeMethod(method);
		return (statusCode == HttpStatus.SC_OK) ? method.getResponseBodyAsStream() : null;
	}
	
	public Integer getStatusCode() {
		return statusCode;
	}
}
