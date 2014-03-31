package com.akvelon.report;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.akvelon.mail.EmailSender;
import com.akvelon.test.CMDOptionReader;
import com.akvelon.test.FileOptionsReader;
import com.akvelon.util.ReportUtil;
import com.akvelon.writer.reports.ReportWriter;
import com.akvelon.writer.reports.XLSHourReportWriter;

/**
 * This class was made abstract because this logic is common for all report
 * checkers
 */
public abstract class ReportChecker {

	private static final Logger log = Logger.getLogger(ReportChecker.class);

	private String repDir = "XLSreports";
	
	protected ReportWriter repWriter;
	protected ReportWriter hRepWriter;
	protected EmailSender emailSender;
	
	protected HSSFWorkbook workbook;

	public ReportChecker() {
		File repFolfer = new File(repDir);
		repFolfer.mkdir();
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
		
		final String folder = "daily/";
		checkReport(folder + options.get(choice));
		writeAndSend();
	}

	public void checkBatchReport(String reportsStorage) throws Exception {
		log.info("Check report from folder: " + reportsStorage);

		FileOptionsReader optionsReader = new FileOptionsReader(reportsStorage);
		Map<String, String> options = optionsReader.readOptions();

		final String folder = "daily/";

		for (String choice : options.keySet()) {
			checkReport(folder + options.get(choice));
		}

		hRepWriter = new XLSHourReportWriter(workbook);
		HourReportChecker hRepChecker = new HourReportChecker(hRepWriter);
		hRepChecker.checkReportHours("./reports/hours/");
		writeAndSend();
	}

	public void checkReportHours(String reportsStorage) throws Exception {
		FileOptionsReader optionsReader = new FileOptionsReader(reportsStorage);
		Map<String, String> options = optionsReader.readOptions();

		final String folder = "hours/";
		for (String choice : options.keySet()) {
			checkReport(folder + options.get(choice));
		}
	}

	private void writeAndSend() {
		if (repWriter == null)
			return;
		if (hRepWriter != null) {
			// TODO add send with this type report
		}
		
		List<List<Report>> reps = repWriter != null ? repWriter.getReports() : null;
		List<HourReport> hReps = hRepWriter != null ? hRepWriter.getHourReports() : null;
		
		reps = ReportUtil.twoTaskInProgress(reps);
		reps = ReportUtil.findTaskTestInProgress(reps);
		reps = ReportUtil.findCodeReviewBLI(reps);
		
		emailSender.sendTestNotificationsByRepType(reps, hReps);
		// emailSender.sendTestNotificationsByRepType(repWriter.getReports());
		// emailSender.sendTestNotificationsByTaskOwner(repWriter.getReports());
		FileOutputStream out = null;
		try {
			if (!CollectionUtils.isEmpty(reps))
				repWriter.writeReport();
			if (!CollectionUtils.isEmpty(hReps))
				hRepWriter.writeReport();
			if (workbook.getNumberOfSheets() != 0) {
				out = new FileOutputStream(new File(repDir + File.separator + repWriter.getFileName()));
				workbook.write(out);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null)
					out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	protected abstract void checkReport(String reportName) throws Exception;
}
