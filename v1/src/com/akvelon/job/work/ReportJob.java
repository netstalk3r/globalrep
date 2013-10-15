package com.akvelon.job.work;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.akvelon.report.ParsedReplyReportChecker;
import com.akvelon.report.ReportChecker;

public class ReportJob implements Job {

	private static final Logger log = Logger.getLogger(ReportJob.class);
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		ReportChecker checker = new ParsedReplyReportChecker();
		try {
			log.info("Job started - " + context.getJobDetail().getKey().toString());
			checker.checkBatchReport("./src/reports/daily/");
			log.info("Job finished - " + context.getJobDetail().getKey().toString());
		} catch (Exception e) {
			log.error("failed to read report properties.");
			e.printStackTrace();
		}
	}

}
