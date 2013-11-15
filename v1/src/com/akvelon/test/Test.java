package com.akvelon.test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import com.akvelon.job.work.ReportScheduler;
import com.akvelon.report.HourReportChecker;
import com.akvelon.report.ParsedReplyReportChecker;
import com.akvelon.report.ReportChecker;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		ReportChecker checker = new ReportChecker();
		ReportChecker checker = new ParsedReplyReportChecker();
//		ReportScheduler scheduler = new ReportScheduler();
//		ReportChecker checker = new HourReportChecker();
		try {
//			Class.forName("com.akvelon.util.JobSettingLaucher");
//			scheduler.runJob();
			checker.checkBatchReport("./src/reports/daily/");
//			checker.checkSingleReport("./src/reports/daily/");
//			checker.checkReportHours("./src/reports/hours/");
		} catch (Exception e) {
			System.out.println("failed to read report properties.");
			e.printStackTrace();
		}
	}
}
