package com.akvelon.report;

import com.akvelon.test.V1ReportParser;
import com.akvelon.test.V1SAXReportParser;
import com.akvelon.writer.reports.CSVReportWriter;
import com.akvelon.writer.reports.XMLReportWriter;

/**
 * Report checker with writing to file
 */
public class ParsedReplyReportChecker extends ReportChecker {
	
	public ParsedReplyReportChecker() {
		super();
		repWriter = new XMLReportWriter();
//		repWriter = new CSVReportWriter();
	}
	
	protected void checkReport(String reportName) throws Exception {
		V1ReportParser reportParser = new V1SAXReportParser(repWriter);
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
