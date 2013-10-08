package com.akvelon.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.akvelon.report.Report;

public class ReportUtilTest {

	@Test
	public void testSortReportsByOwner() {

		// given
		Report repM1 = new Report();
		repM1.setOwnerTaskName("Maria");
		Report repM2 = new Report();
		repM2.setOwnerTaskName("Maria");
		Report repA1 = new Report();
		repA1.setOwnerTaskName("Anton");
		Report repA2 = new Report();
		repA2.setOwnerTaskName("Anton");
		Report repO1 = new Report();
		repO1.setOwnerTaskName("Oleg");
		List<Report> listTasks1 = Arrays.asList(repM1, repA1, repO1);
		List<Report> listTasks2 = Arrays.asList(repM2, repA2);

		List<List<Report>> reports = new ArrayList<List<Report>>(); 
		reports.add(listTasks1);
		reports.add(listTasks2);

		// do
		List<List<Report>> result = ReportUtil.sortReportsByOwner(reports);
		
		// verify
		assertNotNull(result);
		assertFalse(result.isEmpty());
		
		assertEquals(Integer.parseInt("3"), result.size());
	}

}
