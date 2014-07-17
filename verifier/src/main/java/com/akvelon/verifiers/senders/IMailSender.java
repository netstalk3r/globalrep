package com.akvelon.verifiers.senders;

import java.util.Map;

public interface IMailSender {

	void sendAllHourReports(String to, String cc, Map<Integer, ?> params);

	void sendMissedHoursReport(String to, Map<Integer, ?> params);

}
