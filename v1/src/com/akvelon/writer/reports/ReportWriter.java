package com.akvelon.writer.reports;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.akvelon.report.Report;

/**
 * Interface for writing reporst 
 */
public abstract class ReportWriter {
	
	protected List<List<Report>> reports;
	
	protected SimpleDateFormat formatter;
	protected String fileName;
	
	protected static final String ENCODING = "UTF-8";
	protected static final String DATE_FORMAT = "dd.MM.yyyy HH-mm";
	protected static final String HEAD = "BLI ID,BLI NAME,BLI OWNER,TASK OWNER,DESCRIPTION";
	
	public ReportWriter() {
		formatter = new SimpleDateFormat(DATE_FORMAT);
		fileName = formatter.format(new Date());
		reports = new ArrayList<List<Report>>();
	}
	
	public abstract void writeReport() throws IOException;
	
	public void addReports(List<Report> reports) {
		this.reports.add(reports);
	}
	
	public List<List<Report>> getReports() {
		return reports;
	}
}
	