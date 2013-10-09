package com.akvelon.job.work;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class MorningReportJob implements Job {

	private static final Logger log = Logger.getLogger(MorningReportJob.class);
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		new JobExecutor().execute(context, log);
	}

}
