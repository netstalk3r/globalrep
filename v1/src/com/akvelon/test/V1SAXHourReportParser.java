package com.akvelon.test;

import java.io.IOException;
import java.net.URLConnection;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.akvelon.report.HourReport;
import com.akvelon.util.SAXBeginDateUtil;
import com.akvelon.util.SAXHourUtil;
import com.akvelon.writer.reports.ReportWriter;
import com.akvelon.writer.reports.XLSHourReportWriter;

public class V1SAXHourReportParser extends V1ReportParser {

	private static List<HourReport> hReports;
	private static int hours;
	private static Date sprintStartDate;
	
	ReportWriter hRepWriter;

	private String hReportLine = "\nName: %s;\n Reported Hours: %s;\n Required hours: %s;";
	
	public V1SAXHourReportParser(XLSHourReportWriter hRepWriter) throws IOException {
		super();
		this.hRepWriter = hRepWriter;
	}
	
	public V1SAXHourReportParser(String reportName, ReportWriter hRepWriter) throws IOException {
		super();
		this.hRepWriter = hRepWriter;
	}

	@Override
	public String readXmlData(String reportName) {
		String result = new String();
		try {
			URLConnection urlConnection = this.reportUrlBuilder.buildSecuredReportUrl(reportName);
			if (reportName.endsWith("all_done.properties")) {
				SAXHourUtil saxHour = new SAXHourUtil();
				hReports = saxHour.parse(urlConnection.getInputStream());
				hRepWriter.addHourReports(hReports);
			} else if (reportName.endsWith("sprint_start_date.properties")) {
//				if (hReports == null)
//					throw new IllegalStateException("Actuals can not be null at this place!");
				SAXBeginDateUtil saxBDate = new SAXBeginDateUtil();
				hours = saxBDate.parse(urlConnection.getInputStream());
				sprintStartDate = saxBDate.getSprintStartDate();
				result = "Spring Start Date - " + sprintStartDate + "; Required hours before today - " + hours;
			}
			if (hours != 0 && hReports != null) {
				StringBuilder sb = new StringBuilder();
				for (HourReport hRep : hReports) {
					hRep.setRequiredHours(hours);
					hRep.setSprintStartDate(sprintStartDate);
					sb.append(String.format(hReportLine, hRep.getTeamMember(), hRep.getReportedHours(), hRep.getRequiredHours()));
				}
				result = sb.toString();
			}
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		return result;
	}
}
