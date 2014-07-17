package com.akvelon.verifier.senders;

import java.util.HashMap;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;

import com.akvelon.verifiers.reports.ETSHourReport;
import com.akvelon.verifiers.senders.IMailSender;
import com.akvelon.verifiers.senders.MailSender;
import com.akvelon.verifiers.util.Constants;

@Ignore
public class MailSenderTest {

	private IMailSender mailSender = new MailSender();
	
	@Test
	public void testSendAllHourReports_hp(){
		ETSHourReport rep1 = new ETSHourReport();
		rep1.setName("person1");
		rep1.setEmail("email@asd");
		rep1.setHours(12.5);
		ETSHourReport rep2 = new ETSHourReport();
		rep2.setName("person2");
		rep2.setEmail("email2@asd");
		rep2.setHours(2.5);
//		mailSender.sendAllHourReports("anton.nagorny@akvelon.com", null, 23, Arrays.asList(rep1,rep2));
//		mailSender.sendAllHourReports("maria.serichenko@akvelon.com", 22, Arrays.asList(rep1,rep2));
	}
	
	@Test
	public void testSendMissedHoursReport_hp() {
		Map<Integer, Object> params = new HashMap<Integer, Object>();
		params.put(Constants.V1_REQUIRED_HOURS, 10);
		params.put(Constants.ETS_REQUIRED_HOURS, 16);
		params.put(Constants.V1_REPORTED_HOURS, 8);
		params.put(Constants.ETS_REPORTED_HOURS, 20);
//		mailSender.sendMissedHoursReport("anton.nagorny@akvelon.com", params);
//		mailSender.sendMissedHoursReport("maria.serichenko@akvelon.com", 13, rep1);
	}
}
