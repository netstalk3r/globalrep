package com.akvelon.report;

import org.apache.log4j.Logger;

import com.akvelon.test.V1ReportParser;

/**
 * Simple report checker with out writing report to file
 */
public class SimpleReportChecker extends ReportChecker {

	private static final Logger log = Logger.getLogger(SimpleReportChecker.class);
	
	protected void checkReport(String reportName) throws Exception {
		V1ReportParser reportParser = new V1ReportParser();
		log.info("Checking " + reportName);
		boolean isValid = reportParser.isReportValid(reportName);
		if (!isValid) {
			log.info("Report failed.");
			String content = reportParser.readXmlData(reportName);
			log.info(content);
			log.info("URL: " + reportParser.getReportUrlBuilder().buildReportUrl(reportName));
		} else {
			log.info("Passed.");
		}
	}
}
