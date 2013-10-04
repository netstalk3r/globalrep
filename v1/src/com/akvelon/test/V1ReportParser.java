package com.akvelon.test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URLConnection;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class V1ReportParser {
	protected V1ReportUrlBuilder reportUrlBuilder = null;
	
	public V1ReportParser () throws IOException {
		this.reportUrlBuilder = new V1ReportUrlBuilder();
	}
	
	public V1ReportUrlBuilder getReportUrlBuilder() {
		return this.reportUrlBuilder;
	}
	
	public boolean isReportValid(String reportName) throws Exception {
		Document doc = this.buildReportDocument(reportName);
		NodeList nodes = doc.getElementsByTagName("Asset");
		
		return nodes.getLength() == 0;
	}
	
	private Document buildReportDocument(String reportName) throws Exception{
		URLConnection urlConnection = this.reportUrlBuilder.buildSecuredReportUrl(reportName);
		
		InputStream is = urlConnection.getInputStream();
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(is);
		return doc;
	}
	
	public String readXmlData(String reportName) {
		String result = "";
		try {
			URLConnection urlConnection = this.reportUrlBuilder.buildSecuredReportUrl(reportName);
			
			InputStream is = urlConnection.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);

			int numCharsRead;
			char[] charArray = new char[1024];
			StringBuffer sb = new StringBuffer();
			while ((numCharsRead = isr.read(charArray)) > 0) {
				sb.append(charArray, 0, numCharsRead);
			}
			result = sb.toString();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
}
