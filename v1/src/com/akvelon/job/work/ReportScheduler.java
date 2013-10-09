package com.akvelon.job.work;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import com.akvelon.util.JobSettingLaucher;

public class ReportScheduler {

	public void runJob() throws SchedulerException {

		JobKey reportJobKey = new JobKey("reportJob", "reportGroup");
		JobDetail reportJob = JobBuilder.newJob(ReportJob.class).withIdentity(reportJobKey).build();

		CronTrigger reportTrigger = TriggerBuilder.newTrigger().withIdentity("reportTrigger", "reportGroup")
				.withSchedule(CronScheduleBuilder.cronSchedule(JobSettingLaucher.getTimeExpr())).build();

		Scheduler scheduler = new StdSchedulerFactory("src/quartz.properties").getScheduler();
		scheduler.start();
		scheduler.scheduleJob(reportJob, reportTrigger);
	}
	
}
