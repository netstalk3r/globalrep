package com.akvelon.verifiers.parser;

import java.io.InputStream;
import java.util.Calendar;
import java.util.List;

import com.akvelon.verifiers.reports.V1HourReport;

public interface V1Parser {

	Calendar parseSprintStartDate(InputStream is) throws Exception;
	
	List<V1HourReport> parseReportedHours(InputStream is) throws Exception;
	
}
