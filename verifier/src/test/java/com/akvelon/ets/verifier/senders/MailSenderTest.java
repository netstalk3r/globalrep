package com.akvelon.ets.verifier.senders;

import java.util.Arrays;

import org.junit.Ignore;
import org.junit.Test;

import com.akvelon.ets.verifier.reports.PersonalHourReport;

@Ignore
public class MailSenderTest {

	private IMailSender mailSender = new MailSender();
	
	@Test
	public void testSendAllHourReports_hp(){
		PersonalHourReport rep1 = new PersonalHourReport();
		rep1.setName("person1");
		rep1.setEmail("email@asd");
		rep1.setHours(12.5);
		PersonalHourReport rep2 = new PersonalHourReport();
		rep2.setName("person2");
		rep2.setEmail("email2@asd");
		rep2.setHours(2.5);
		mailSender.sendAllHourReports("anton.nagorny@akvelon.com", 23, Arrays.asList(rep1,rep2));
//		mailSender.sendAllHourReports("maria.serichenko@akvelon.com", 22, Arrays.asList(rep1,rep2));
	}
	
	@Test
	public void testSendMissedHoursReport_hp() {
		PersonalHourReport rep1 = new PersonalHourReport();
		rep1.setName("person1");
		rep1.setEmail("email@asd");
		rep1.setHours(12.5);
		mailSender.sendMissedHoursReport("anton.nagorny@akvelon.com", 12, rep1);
//		mailSender.sendMissedHoursReport("maria.serichenko@akvelon.com", 13, rep1);
	}
}
