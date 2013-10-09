package com.akvelon.job.work;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;

import com.akvelon.report.ParsedReplyReportChecker;
import com.akvelon.report.ReportChecker;

public class JobExecutor {

	public void execute(JobExecutionContext context, Logger log) {
		ReportChecker checker = new ParsedReplyReportChecker();
		try {
			log.info("Job started - " + context.getJobDetail().getKey().toString());
			checker.checkBatchReport("./src/reports/daily/");
			log.info("Job finished - " + context.getJobDetail().getKey().toString());
		} catch (Exception e) {
			System.out.println("failed to read report properties.");
			e.printStackTrace();
		}
	}
	
}
