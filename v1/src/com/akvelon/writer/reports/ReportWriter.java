package com.akvelon.writer.reports;

import java.io.IOException;
import java.util.List;

import com.akvelon.report.Report;

/**
 * Interface for writing reporst 
 */
public interface ReportWriter {
	
	static final String ENCODING = "UTF-8";
	static final String DATE_FORMAT = "dd.MM.yyyy HH-mm";
	static final String HEAD = "BLI ID,BLI NAME,OWNER,DESCRIPTION\n";
	
	void writeReport() throws IOException;
	
	void addReports(List<Report> reports);
	
}
	