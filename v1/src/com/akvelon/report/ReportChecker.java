package com.akvelon.report;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.akvelon.mail.EmailSender;
import com.akvelon.test.CMDOptionReader;
import com.akvelon.test.FileOptionsReader;
import com.akvelon.writer.reports.ReportWriter;
import com.akvelon.writer.reports.XLSHourReportWriter;

/**
 * This class was made abstract because this logic is common for all report
 * checkers
 */
public abstract class ReportChecker {

	private static final Logger log = Logger.getLogger(ReportChecker.class);
	
	protected ReportWriter repWriter;
	protected ReportWriter hRepWriter;
	protected EmailSender emailSender;
	
	protected HSSFWorkbook workbook;

	public ReportChecker() {
		workbook = new HSSFWorkbook();
		try {
			emailSender = new EmailSender();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void checkSingleReport(String reportsStorage) throws Exception {
		FileOptionsReader optionsReader = new FileOptionsReader(reportsStorage);
		Map<String, String> options = optionsReader.readOptions();
		CMDOptionReader optionReader = new CMDOptionReader("exit");
		String choice = optionReader.readOption(options);

		checkReport(options.get(choice));
		writeAndSend();
	}

	public void checkBatchReport(String reportsStorage) throws Exception {
		log.info("Check report from folder: " + reportsStorage);

		FileOptionsReader optionsReader = new FileOptionsReader(reportsStorage);
		Map<String, String> options = optionsReader.readOptions();

		for (String choice : options.keySet()) {
			checkReport(options.get(choice));
		}
		
		HourReportChecker hRepChecker = new HourReportChecker();
		hRepWriter = new XLSHourReportWriter(workbook);
		hRepChecker.checkReportHours("./src/reports/hours/");
		writeAndSend();
	}

	public void checkReportHours(String reportsStorage) throws Exception {
		FileOptionsReader optionsReader = new FileOptionsReader(reportsStorage);
		Map<String, String> options = optionsReader.readOptions();
		
		for (String choice : options.keySet()) {
			checkReport(options.get(choice));
		}
	}
	
	private void writeAndSend() {
		if (repWriter == null)
			return;
		if (hRepWriter != null) {
			// TODO add send with this type report
		}
//		emailSender.sendTestNotificationsByRepType(repWriter.getReports());
//		emailSender.sendTestNotificationsByTaskOwner(repWriter.getReports());
		if (CollectionUtils.isEmpty(repWriter.getReports()))
			return;
		try {
			repWriter.writeReport();
			hRepWriter.writeReport();
			FileOutputStream out = new FileOutputStream(new File(repWriter.getFileName()));
			workbook.write(out);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected abstract void checkReport(String reportName) throws Exception;
}
