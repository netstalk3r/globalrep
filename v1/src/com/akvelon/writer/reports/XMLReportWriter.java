package com.akvelon.writer.reports;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;

import com.akvelon.report.Report;

public class XMLReportWriter implements ReportWriter {

	private String fileName;
	private SimpleDateFormat formatter;

	private HSSFWorkbook workbook;

	private List<List<Report>> reports;

	private String EXTENSION = ".xls";
	
	private CellStyle style;

	private int rowNum = 0;

	public XMLReportWriter() {
		formatter = new SimpleDateFormat(DATE_FORMAT);
		fileName = formatter.format(new Date());
		workbook = new HSSFWorkbook();
		reports = new ArrayList<List<Report>>();
		style = workbook.createCellStyle();
		style.setFillForegroundColor(IndexedColors.AQUA.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
	}

	@Override
	public void writeReport() throws IOException {
		if (reports.isEmpty()) {
			System.out.println("No reports to write");
			return;
		}
		for (List<Report> reps : reports) {
			HSSFSheet sheet = workbook.createSheet(reps.get(0).getReportName());
			rowNum = 0;
			createReportHead(sheet);
			for (Report rep : reps) {
				createReportRow(sheet, rep);
			}
			autofitColumsSize(sheet);
		}
		FileOutputStream out = new FileOutputStream(new File(fileName + EXTENSION));
		workbook.write(out);
		out.close();
	}

	@Override
	public void addReports(List<Report> reports) {
		this.reports.add(reports);
	}

	private void createReportRow(HSSFSheet sheet, Report report) {
		int cellNum = 0;
		Row row = sheet.createRow(rowNum++);
		row.createCell(cellNum++).setCellValue(report.getBliID());
		row.createCell(cellNum++).setCellValue(report.getBliName());
		row.createCell(cellNum++).setCellValue(report.getOwnerTaskName());
		row.createCell(cellNum++).setCellValue(report.getReportName());
		cellNum = 0;
	}

	private void createReportHead(HSSFSheet sheet) {
		int cellNum = 0;
		Row row = sheet.createRow(rowNum++);
		StringTokenizer st = new StringTokenizer(HEAD, ",");
		while (st.hasMoreElements()) {
			row.createCell(cellNum).setCellValue(st.nextToken());
			row.getCell(cellNum++).setCellStyle(style);
		}
	}

	private void autofitColumsSize(HSSFSheet sheet) {
		Iterator<Row> rowIterator = sheet.iterator();
		Iterator<Cell> cellIterator = rowIterator.next().cellIterator();
		while (cellIterator.hasNext()) {
			sheet.autoSizeColumn(cellIterator.next().getColumnIndex());
		}
	}
}
