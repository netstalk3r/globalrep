package com.akvelon.report;

import java.io.IOException;
import java.util.Map;

import com.akvelon.test.CMDOptionReader;
import com.akvelon.test.FileOptionsReader;
import com.akvelon.writer.reports.ReportWriter;

public abstract class ReportChecker {

	protected ReportWriter repWriter;
	
	public void checkSingleReport(String reportsStorage) throws Exception {
		FileOptionsReader optionsReader = new FileOptionsReader(reportsStorage);
		Map<String, String> options = optionsReader.readOptions();
		CMDOptionReader optionReader = new CMDOptionReader("exit");
		String choice = optionReader.readOption(options);

		checkReport(options.get(choice));
		write();
	}

	public void checkBatchReport(String reportsStorage) throws Exception {
		System.out.println("Check report from folder: " + reportsStorage);

		FileOptionsReader optionsReader = new FileOptionsReader(reportsStorage);
		Map<String, String> options = optionsReader.readOptions();

		for (String choice : options.keySet()) {
			checkReport(options.get(choice));
		}
		write();
	}

	private void write() {
		if (repWriter == null) return;
		try {
			repWriter.writeReport();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected abstract void checkReport(String reportName) throws Exception;
}
