package com.akvelon.verifiers.senders;

import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.akvelon.verifiers.util.TemplateUtil;

public class MailSender implements IMailSender {

	private static final String PASSWORD = "TNC_Yfgjvbyfkrf";
	private static final String SENDER = "etsreminder@ua.akvelon.com";
	
	public static final String MAIL_SMTP_PORT = "mail.smtp.port";
	public static final String MAIL_SMTP_HOST = "mail.smtp.host";
	public static final String MAIL_TRANSPORT_PROTOCOL = "mail.transport.protocol";
	
	private static final String STMP_PORT = "25";
	private static final String STMP_HOST = "192.168.1.22";
	private static final String SMTP_PROTOCOL = "smtp";
	
	private static final String ETS_MISSED_HOUR_SUBJECT = "ETS missed hours";
	private static final String ETS_ALL_HOURS_SUBJECT = "ETS all hours report";

	public void sendAllHourReports(String to, String cc, Map<Integer, ?> params) {
		sendMessage(to, cc, ETS_ALL_HOURS_SUBJECT, TemplateUtil.getTemplateForAllReportedHours(params));
	}

	public void sendMissedHoursReport(String to, Map<Integer, ?> params) {
		sendMessage(to, null, ETS_MISSED_HOUR_SUBJECT, TemplateUtil.getTemplateForMissedHours(params));
	}

	private void sendMessage(String to, String cc, String subject, String text) {
		Properties properties = new Properties();
		properties.put(MAIL_TRANSPORT_PROTOCOL, SMTP_PROTOCOL);
		properties.put(MAIL_SMTP_HOST, STMP_HOST);
		properties.put(MAIL_SMTP_PORT, STMP_PORT);
		
		Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(SENDER,PASSWORD);
			}
		});
		
		try {
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(SENDER));
			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
			if (cc != null) {
				msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(to));
			}
			msg.setSubject(subject);
			msg.setContent(text, "text/html; charset=UTF8");

			Transport.send(msg);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

}
