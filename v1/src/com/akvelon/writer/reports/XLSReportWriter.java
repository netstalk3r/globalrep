package com.akvelon.writer.reports;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

import com.akvelon.report.Report;

public class XLSReportWriter extends ReportWriter {

	private static final Logger log = Logger.getLogger(XLSReportWriter.class);

	public XLSReportWriter(HSSFWorkbook workbook) {
		super(workbook);
	}

	@Override
	public void writeReport() throws IOException {
		if (CollectionUtils.isEmpty(reports)) {
			log.info("No reports to write");
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
	}

	private void createReportRow(HSSFSheet sheet, Report report) {
		int cellNum = 0;
		Row row = sheet.createRow(rowNum++);
		row.createCell(cellNum++).setCellValue(report.getBliID());
		row.createCell(cellNum++).setCellValue(report.getBliName());
		row.createCell(cellNum++).setCellValue(report.getBliOwner());
		row.createCell(cellNum++).setCellValue(report.getTaskName());
		row.createCell(cellNum++).setCellValue(report.getTaskOwner());
		row.createCell(cellNum++).setCellValue(report.getReportName());
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

}
