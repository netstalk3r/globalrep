package com.akvelon.report;

import org.apache.log4j.Logger;

import com.akvelon.test.V1ReportParser;
import com.akvelon.test.V1SAXHourReportParser;
import com.akvelon.writer.reports.ReportWriter;
import com.akvelon.writer.reports.XLSHourReportWriter;

public class HourReportChecker extends ReportChecker {

	private static final Logger log = Logger.getLogger(HourReportChecker.class);
	
	public HourReportChecker(ReportWriter hRepWriter) {
		super();
		this.hRepWriter = hRepWriter;
		if (hRepWriter == null) { 
			hRepWriter = new XLSHourReportWriter(workbook);
		}
	}
	
	@Override
	protected void checkReport(String reportName) throws Exception {
		V1ReportParser reportParser = new V1SAXHourReportParser(reportName,hRepWriter);
		log.info("Checking " + reportName);
		String content = reportParser.readXmlData(reportName);
		log.info(content);
	}

	public XLSHourReportWriter getWriter() {
		return (XLSHourReportWriter) super.hRepWriter;
	}
}
