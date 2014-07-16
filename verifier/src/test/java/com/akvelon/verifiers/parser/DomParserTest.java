package com.akvelon.verifiers.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.akvelon.verifiers.reports.V1HourReport;

@Ignore
public class DomParserTest {

	private static final String TEST_BEGIN_DATE_XML_FILE = "test_begin_date.xml";
	private static final String ACTUAL_HOURS_REPORT_XML_FILE = "actual_hours_report.xml";

	private V1Parser beginDateParser;

	@Before
	public void setUp() throws Exception {
		beginDateParser = new DomParser();
	}

	@Test
	public void testParseSprintStartDate() throws Exception {
		Calendar parseSprintStartDate = beginDateParser.parseSprintStartDate(this.getClass().getClassLoader()
				.getResourceAsStream(TEST_BEGIN_DATE_XML_FILE));
		assertEquals(2014, parseSprintStartDate.get(Calendar.YEAR));
		assertEquals(3, parseSprintStartDate.get(Calendar.MONTH));
		assertEquals(2, parseSprintStartDate.get(Calendar.DATE));
	}

	@Test
	public void testParseReportedHours() throws Exception {
		List<V1HourReport> hourReports = beginDateParser.parseReportedHours(this.getClass().getClassLoader()
				.getResourceAsStream(ACTUAL_HOURS_REPORT_XML_FILE));
		assertNotNull(hourReports);
		assertEquals(7, hourReports.size());
		Collections.sort(hourReports, new Comparator<V1HourReport>() {
			public int compare(V1HourReport o1, V1HourReport o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		V1HourReport hourReport = hourReports.get(0);
		assertEquals(Double.valueOf(8.0), Double.valueOf(hourReport.getReportedHours()));
		hourReport = hourReports.get(1);
		assertEquals(Double.valueOf(6.0), Double.valueOf(hourReport.getReportedHours()));
		hourReport = hourReports.get(2);
		assertEquals(Double.valueOf(6.0), Double.valueOf(hourReport.getReportedHours()));
		hourReport = hourReports.get(3);
		assertEquals(Double.valueOf(7.0), Double.valueOf(hourReport.getReportedHours()));
		hourReport = hourReports.get(4);
		assertEquals(Double.valueOf(13.0), Double.valueOf(hourReport.getReportedHours()));
		hourReport = hourReports.get(5);
		assertEquals(Double.valueOf(11.0), Double.valueOf(hourReport.getReportedHours()));
		hourReport = hourReports.get(5);
		assertEquals(Double.valueOf(11.0), Double.valueOf(hourReport.getReportedHours()));
	}

}
