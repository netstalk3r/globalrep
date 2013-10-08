package com.akvelon.test;

import com.akvelon.report.ParsedReplyReportChecker;
import com.akvelon.report.ReportChecker;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//ReportChecker checker = new ReportChecker();
		ReportChecker checker = new ParsedReplyReportChecker();
		try {
//			checker.checkBatchReport("./src/reports/daily/");
			checker.checkSingleReport("./src/reports/daily/");
		} catch (Exception e) {
			System.out.println("failed to read report properties.");
			e.printStackTrace();
		}
	}
}
