package com.akvelon.writer.reports;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;

import com.akvelon.report.HourReport;
import com.akvelon.report.Report;

/**
 * Interface for writing reporst 
 */
public abstract class ReportWriter {
	
	protected HSSFWorkbook workbook;
	
	protected List<List<Report>> reports;
	protected List<HourReport> hReports;
	
	protected SimpleDateFormat formatter;
	protected String fileName;
	
	protected String EXTENSION = ".xls";
	
	protected CellStyle style;
	
	protected int rowNum = 0;

	protected static final String ENCODING = "UTF-8";
	protected static final String DATE_FORMAT = "dd.MM.yyyy HH-mm";
	protected static final String HEAD = "BLI ID,BLI NAME,BLI OWNER,TASK NAME,TASK OWNER,DESCRIPTION";
	
	public ReportWriter(HSSFWorkbook workbook) {
		this.workbook = workbook;
		formatter = new SimpleDateFormat(DATE_FORMAT);
		fileName = formatter.format(new Date());
		reports = new ArrayList<List<Report>>();
		hReports = new ArrayList<HourReport>();
		style = workbook.createCellStyle();
		style.setFillForegroundColor(IndexedColors.AQUA.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
	}
	
	public abstract void writeReport() throws IOException;
	
	public void addReports(List<Report> reports) {
		this.reports.add(reports);
	}
	
	public List<List<Report>> getReports() {
		return reports;
	}
	 
	public void addHourReports(List<HourReport> hReps) {
		hReports.addAll(hReps);
	}
	
	public List<HourReport> getHourReports() {
		return hReports;
	}
	
	public String getFileName() {
		return fileName + EXTENSION;
	}
	
	protected void autofitColumsSize(HSSFSheet sheet) {
		Iterator<Row> rowIterator = sheet.iterator();
		Iterator<Cell> cellIterator = rowIterator.next().cellIterator();
		while (cellIterator.hasNext()) {
			sheet.autoSizeColumn(cellIterator.next().getColumnIndex());
		}
	}
	
}
	