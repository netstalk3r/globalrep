package com.akvelon.ets.verifier.senders;

import java.util.List;

import com.akvelon.ets.verifier.reports.PersonalHourReport;

public interface IMailSender {
	
	void sendAllHourReports(String to, String cc, int requiredHours, List<PersonalHourReport> reports);
	
	void sendMissedHoursReport(String to, int requiredHours, PersonalHourReport report);

}
