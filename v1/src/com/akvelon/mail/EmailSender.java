package com.akvelon.mail;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.akvelon.report.Report;

public class EmailSender {

	private Properties props;
	private Session session;

	private String subject = "Invent subject";

	private static final String DOMEN = "@akvelon.com";

	private static final String MAIL_FILE_CONFIG = "mail_conf.properties";

	private String reportLine = "BLI ID: %s;\n BLI Name: %s;\n Description: %s\n\n";
	
	public EmailSender() throws FileNotFoundException, IOException {
		props.load(new BufferedInputStream(new FileInputStream(new File(MAIL_FILE_CONFIG))));
	}

	public void sendNotifications(Map<String, List<Report>> reports) {
		StringBuilder message = new StringBuilder();
		for (String owner : reports.keySet()) {
			List<Report> reps = reports.get(owner);
			for (Report rep : reps) {
				message.append(String.format(reportLine, rep.getBliID(), rep.getBliName(), rep.getReportName()));
			}
			sendMessage(createEmail(owner), message.toString());
		}
	}

	private String createEmail(String owner) {
		return owner.trim().replace("\\s+", ".").toLowerCase() + DOMEN;
	}

	private void sendMessage(String to, String text) {
		Properties properties = new Properties();
		properties.put("mail.transport.protocol", props.getProperty("mail.transport.protocol"));
		properties.put("mail.smtp.host", props.getProperty("mail.smtp.host"));
		properties.put("mail.smtp.port", props.getProperty("mail.smtp.port"));

		session = Session.getInstance(properties, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(props.getProperty("mail.user"), props.getProperty("mail.password"));
			}
		});

		try {
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(props.getProperty("mail.user")));
			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
			msg.setSubject(subject);
			msg.setText(text);

			Transport.send(msg);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}
