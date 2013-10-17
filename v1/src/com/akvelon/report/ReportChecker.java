package com.akvelon.report;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

import com.akvelon.mail.EmailSender;
import com.akvelon.test.CMDOptionReader;
import com.akvelon.test.FileOptionsReader;
import com.akvelon.util.ReportUtil;
import com.akvelon.writer.reports.ReportWriter;

/**
 * This class was made abstract because this logic is common for all report
 * checkers
 */
public abstract class ReportChecker {

	private static final Logger log = Logger.getLogger(ReportChecker.class);
	
	protected ReportWriter repWriter;
	protected EmailSender emailSender;

	public ReportChecker() {
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
		writeAndSend();
	}

	private void writeAndSend() {
		if (repWriter == null)
			return;
//		 emailSender.sendNotifications(ReportUtil.sortReportsByBliOwner(repWriter.getReports()));
		emailSender.sendTestNotificationsByRepType(repWriter.getReports());
		emailSender.sendTestNotificationsByBliOwner(repWriter.getReports());
//		emailSender.sendTestNotifications(ReportUtil.sortReportsByBliOwner(repWriter.getReports()));
		if (CollectionUtils.isEmpty(repWriter.getReports()))
			return;
		try {
			repWriter.writeReport();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected abstract void checkReport(String reportName) throws Exception;
}
