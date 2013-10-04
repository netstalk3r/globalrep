package com.akvelon.report;

import com.akvelon.test.V1SAXReportParser;
import com.akvelon.writer.reports.CSVReportWriter;

public class ParsedReplyReportChecker extends ReportChecker {
	
	
	public ParsedReplyReportChecker() {
		super();
		repWriter = new CSVReportWriter();
	}
	
	protected void checkReport(String reportName) throws Exception {
		V1SAXReportParser reportParser = new V1SAXReportParser(repWriter);
		System.out.println("Checking " + reportName);
		boolean isValid = reportParser.isReportValid(reportName);
		if (!isValid) {
			System.out.println("Report failed.");
			String content = reportParser.readXmlData(reportName);
			System.out.println(content);
			System.out.println("URL: " + reportParser.getReportUrlBuilder().buildReportUrl(reportName));
		} else {
			System.out.println("Passed.");
		}
	}
}
