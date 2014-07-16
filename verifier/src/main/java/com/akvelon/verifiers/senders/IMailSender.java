package com.akvelon.verifiers.senders;

import java.util.List;

import com.akvelon.verifiers.reports.ETSHourReport;

public interface IMailSender {
	
	void sendAllHourReports(String to, String cc, int requiredHours, List<ETSHourReport> reports);
	
	void sendMissedHoursReport(String to, int requiredHours, ETSHourReport report);

}
