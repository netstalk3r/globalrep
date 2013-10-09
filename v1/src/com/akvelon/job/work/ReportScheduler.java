package com.akvelon.job.work;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import com.akvelon.util.JobSettingLaucher;

public class ReportScheduler {

	public void runJob() throws SchedulerException {

		JobKey morningJobKey = new JobKey("morningJob", "reportGroup");
		JobDetail morningJob = JobBuilder.newJob(MorningReportJob.class).withIdentity(morningJobKey).build();

		JobKey eveningJobKey = new JobKey("eveningJob", "reportGroup");
		JobDetail eveningJob = JobBuilder.newJob(EveningReportJob.class).withIdentity(eveningJobKey).build();

		Trigger morningTrigger = TriggerBuilder.newTrigger().withIdentity("morningTrigger", "reportGroup")
				.withSchedule(CronScheduleBuilder.cronSchedule(JobSettingLaucher.getMorningExpr())).build();

		Trigger eveningTrigger = TriggerBuilder.newTrigger().withIdentity("eveningTrigger", "reportGroup")
				.withSchedule(CronScheduleBuilder.cronSchedule(JobSettingLaucher.getEveningExpr())).build();

		Scheduler scheduler = new StdSchedulerFactory("src/quartz.properties").getScheduler();
		scheduler.start();
		scheduler.scheduleJob(morningJob, morningTrigger);
		scheduler.scheduleJob(eveningJob, eveningTrigger);
	}

}
