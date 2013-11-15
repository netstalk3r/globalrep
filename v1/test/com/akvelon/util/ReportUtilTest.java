package com.akvelon.util;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.akvelon.report.HourReport;
import com.akvelon.report.Report;

public class ReportUtilTest {

	@Test
	public void testSortReportsByTaskOwner() {

		// given
		Report repM1 = new Report();
		repM1.setTaskOwner("Maria");
		Report repM2 = new Report();
		repM2.setTaskOwner("Maria");
		Report repA1 = new Report();
		repA1.setTaskOwner("Anton");
		Report repA2 = new Report();
		repA2.setTaskOwner("Anton");
		Report repO1 = new Report();
		repO1.setTaskOwner("Oleg");
		List<Report> listTasks1 = Arrays.asList(repM1, repA1, repO1);
		List<Report> listTasks2 = Arrays.asList(repM2, repA2);

		List<List<Report>> reports = new ArrayList<List<Report>>(); 
		reports.add(listTasks1);
		reports.add(listTasks2);

		// do
		List<List<Report>> result = ReportUtil.sortReportsByTaskOwner(reports);
		
		// verify
		assertNotNull(result);
		assertFalse(result.isEmpty());
		
		assertEquals(Integer.parseInt("3"), result.size());
	}

	@Test
	public void testCountActuals() {
		
		// given
		HourReport hRepA1 = new HourReport();
		hRepA1.setTeamMember("Member1");
		hRepA1.setReportedHours(1.5D);
		HourReport hRepA2 = new HourReport();
		hRepA2.setTeamMember("Member1");
		hRepA2.setReportedHours(2.0D);
		HourReport hRepA3 = new HourReport();
		hRepA3.setTeamMember("Member1");
		hRepA3.setReportedHours(0.5D);
		
		HourReport hRepB1 = new HourReport();
		hRepB1.setTeamMember("Member2");
		hRepB1.setReportedHours(2.0D);
		HourReport hRepB2 = new HourReport();
		hRepB2.setTeamMember("Member2");
		hRepB2.setReportedHours(3.5D);
		
		HourReport hRepC1 = new HourReport();
		hRepC1.setTeamMember("Member3");
		hRepC1.setReportedHours(4.0D);
		HourReport hRepC2 = new HourReport();
		hRepC2.setTeamMember("Member3");
		hRepC2.setReportedHours(2.5D);
		
		List<HourReport> hReports = Arrays.asList(hRepA1, hRepA2, hRepA3, hRepB1, hRepB2, hRepC1, hRepC2);
		
		// do
		List<HourReport> res = ReportUtil.countActuals(hReports);
		
		// verify
		assertNotNull(res);
		assertEquals(3, res.size());
		
		BigDecimal req1 = new BigDecimal(4.0D);
		BigDecimal req2 = new BigDecimal(5.5D);
		BigDecimal req3 = new BigDecimal(6.5D);
		
		BigDecimal rep1 = BigDecimal.valueOf(res.get(0).getReportedHours());
		BigDecimal rep2 = BigDecimal.valueOf(res.get(2).getReportedHours());
		BigDecimal rep3 = BigDecimal.valueOf(res.get(1).getReportedHours());
		assertTrue(req1.subtract(rep1).intValue() == 0);
		assertTrue(req2.subtract(rep2).intValue() == 0);
		assertTrue(req3.subtract(rep3).intValue() == 0);
	}
	
}
