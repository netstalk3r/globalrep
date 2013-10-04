package com.akvelon.test;

import java.io.IOException;
import java.net.URLConnection;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.akvelon.report.Report;
import com.akvelon.util.SAXUtil;
import com.akvelon.writer.reports.CSVReportWriter;
import com.akvelon.writer.reports.ReportWriter;

public class V1SAXReportParser extends V1ReportParser {

	private SAXUtil saxUtil;
	private ReportWriter repWriter;
	
	private String reportLine = "\nBLI ID: %s;\n BLI Name: %s;\n Owner: %s;\n Dscription: %s";
	
	public V1SAXReportParser(ReportWriter reportWriter) throws IOException {
		super();
		this.repWriter = reportWriter;
	}
	
	@Override
	public String readXmlData(String reportName) {
		String result = new String();
		try {
			URLConnection urlConnection = this.reportUrlBuilder.buildSecuredReportUrl(reportName);
			saxUtil = new SAXUtil(reportName);
			List<Report> reports = saxUtil.parse(urlConnection.getInputStream());
			repWriter.addReports(reports);
			StringBuilder sb = new StringBuilder();
			for (Report rep : reports) {
				sb.append(String.format(reportLine, rep.getBliID(), rep.getBliName(), rep.getOwnerTaskName(), rep.getReportName()));
			}
			result = sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		return result;
	}
	
}
