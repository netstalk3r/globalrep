package com.akvelon.test;

import java.util.Map;

public class ReportChecker {

	public void checkSingleReport(String reportsStorage) throws Exception {
		FileOptionsReader optionsReader = new FileOptionsReader(reportsStorage);
		Map<String, String> options = optionsReader.readOptions();
		CMDOptionReader optionReader = new CMDOptionReader("exit");
		String choice = optionReader.readOption(options);
		
		checkReport(options.get(choice));
	}
	
	public void checkBatchReport(String reportsStorage) throws Exception {
		System.out.println("Check report from folder: " + reportsStorage);
		
		FileOptionsReader optionsReader = new FileOptionsReader(reportsStorage);
		Map<String, String> options = optionsReader.readOptions();

		for (String choice : options.keySet()) {
			checkReport(options.get(choice));
		}
	}
	
	private void checkReport(String reportName) throws Exception {
		V1ReportParser reportParser = new V1ReportParser();
		System.out.println("Checking " + reportName);
		boolean isValid = reportParser.isReportValid(reportName);
		if (!isValid) {
			System.out.println("Report failed.");
			String content = reportParser.readXmlData(reportName);
			System.out.println(content);
			System.out.println("URL: " + reportParser.getReportUrlBuilder().buildReportUrl(reportName));
		} else {
			System.out.println("Passed.");
		}
	}
}
