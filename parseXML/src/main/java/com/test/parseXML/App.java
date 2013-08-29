package com.test.parseXML;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.xml.sax.SAXException;

public class App {

	private static final String REQUEST = "https://www3.v1host.com/Tideworks/VersionOne/rest-1.v1/Data/Task?sel=Parent.Number,Parent.Name,Parent.Owners.Name,Team.Name,Name,Owners.Name&where=Owners.Nickname='mseriche','aarnauto','atetyush','ATimofeev','DVolkov','OKrupenya','OlegZ','SergiiI';Team.Name='MS%20Gate','MS%20Vanguard';Timebox.Name='MS1308';Parent.Status.Name='Completed','Ready%20For%20QA';Status.Name='','In%20Progress','Impeded'";
	private static String usr = "anagorny";
	private static String pwd = "anagorny";

	public static void main(String[] args) {
		UsernamePasswordCredentials creds = new UsernamePasswordCredentials(usr, pwd);
		HttpClient client = new HttpClient();
		GetMethod method = new GetMethod(REQUEST);
		client.getState().setCredentials(AuthScope.ANY, creds);
		try {
			int statusCode = client.executeMethod(method);
			if (statusCode == HttpStatus.SC_OK) {
				new SAXUtil(method.getResponseBodyAsStream()).parse();
			} else {
				System.err.println("Request fail. Status code: " + statusCode);
			}
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}

}