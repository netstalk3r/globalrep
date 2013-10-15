package com.akvelon.report;

import org.apache.log4j.Logger;

import com.akvelon.test.V1ReportParser;
import com.akvelon.test.V1SAXReportParser;
import com.akvelon.writer.reports.CSVReportWriter;
import com.akvelon.writer.reports.XLSReportWriter;

/**
 * Report checker with writing to file
 */
public class ParsedReplyReportChecker extends ReportChecker {
	
	private static final Logger log = Logger.getLogger(ParsedReplyReportChecker.class);
	
	public ParsedReplyReportChecker() {
		super();
		repWriter = new XLSReportWriter();
//		repWriter = new CSVReportWriter();
	}
	
	protected void checkReport(String reportName) throws Exception {
		V1ReportParser reportParser = new V1SAXReportParser(repWriter);
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
