package com.akvelon.writer.reports;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;

import com.akvelon.report.HourReport;

public class XLSHourReportWriter extends ReportWriter {

	private Logger log = Logger.getLogger(XLSHourReportWriter.class);

	private static final String HEAD = "NAME, REPORTED HOURS, REQUIRED HOURS";

	public XLSHourReportWriter(HSSFWorkbook workbook) {
		super(workbook);
	}

	@Override
	public void writeReport() throws IOException {
		if (CollectionUtils.isEmpty(hReports)) {
			log.info("No hour reports to write");
			return;
		}
		HSSFSheet sheet = workbook.createSheet("reported hours");
		rowNum = 0;
		createReportHead(sheet);
		for (HourReport hRep : hReports) {
			createReportRow(sheet, hRep);
		}
		autofitColumsSize(sheet);
	}

	private void createReportRow(HSSFSheet sheet, HourReport hReport) {
		int cellNum = 0;
		Row row = sheet.createRow(rowNum++);
		row.createCell(cellNum++).setCellValue(hReport.getTeamMember());
		row.createCell(cellNum++).setCellValue(hReport.getReportedHours());
		row.createCell(cellNum++).setCellValue(hReport.getRequiredHours());
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
