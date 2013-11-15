package com.akvelon.report;

import org.apache.log4j.Logger;

import com.akvelon.test.V1ReportParser;
import com.akvelon.test.V1SAXHourReportParser;
import com.akvelon.writer.reports.XLSHourReportWriter;

public class HourReportChecker extends ReportChecker {

	private static final Logger log = Logger.getLogger(HourReportChecker.class);
	
	public HourReportChecker() {
		super();
		if (hRepWriter == null) { 
			hRepWriter = new XLSHourReportWriter(workbook);
		}
	}
	
	@Override
	protected void checkReport(String reportName) throws Exception {
		V1ReportParser reportParser = new V1SAXHourReportParser(reportName);
		log.info("Checking " + reportName);
		String content = reportParser.readXmlData(reportName);
		log.info(content);
	}

	public XLSHourReportWriter getWriter() {
		return (XLSHourReportWriter) super.hRepWriter;
	}
}
