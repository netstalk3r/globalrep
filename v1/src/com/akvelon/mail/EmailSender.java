package com.akvelon.mail;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang3.StringUtils;

import com.akvelon.report.Report;

public class EmailSender {

	private Properties props;
	private Session session;

	private String subject = "TWVG > V1 status";

	private static final String DOMEN = "@akvelon.com";

	private static final String MAIL_FILE_CONFIG = "src/mail_conf.properties";

	private String noReport = "<h2>No issues were found</h2>";
	private String reportLine = "BLI ID: %s;<br/> BLI Name: %s;<br/> BLI Owner: %s;<br/> Task Name: %s;<br/> Task Owner: %s;<br/> Description: %s;<br/>";

	public EmailSender() throws FileNotFoundException, IOException {
		props = new Properties();
		props.load(new BufferedInputStream(new FileInputStream(new File(MAIL_FILE_CONFIG))));
	}

	public void sendNotifications(List<List<Report>> reports) {
		if (reports.isEmpty()) {
			sendMessage("maria.serichenko@akvelon.com", null, noReport);
			return;
		}
		StringBuilder wholeRep = new StringBuilder();
		StringBuilder message = new StringBuilder();
		for (List<Report> reps : reports) {
			for (Report rep : reps) {
				message.append(String.format(reportLine, rep.getBliID(), rep.getBliName(), rep.getTaskName(), rep.getTaskOwner(),
						rep.getReportName()));
			}
			sendMessage(createEmail(reps.get(0).getTaskOwner()), null, message.toString());
			wholeRep.append(message);
			message.setLength(0);
		}
		sendMessage("maria.serichenko@akvelon.com", null, wholeRep.toString());
	}

	private String createEmail(String owner) {
		return owner.trim().replace("\\s+", ".").toLowerCase() + DOMEN;
	}

	private void sendMessage(String to, String cc, String text) {
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
			if (cc != null) {
				msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc));
			}
			msg.setSubject(StringUtils.isBlank(props.getProperty("mail.subject")) ? props.getProperty("mail.subject") : subject);
			msg.setContent(text, "text/html; charset=UTF8");

			Transport.send(msg);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}
