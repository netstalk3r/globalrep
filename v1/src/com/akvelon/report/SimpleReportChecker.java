package com.akvelon.report;

import com.akvelon.test.V1ReportParser;

/**
 * Simple report checker with out writing report to file
 */
public class SimpleReportChecker extends ReportChecker {

	protected void checkReport(String reportName) throws Exception {
		V1ReportParser reportParser = new V1ReportParser();
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
