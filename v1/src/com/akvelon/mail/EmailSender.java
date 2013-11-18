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

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.akvelon.report.HourReport;
import com.akvelon.report.Report;
import com.akvelon.util.TemplateConverter;

public class EmailSender {

//	private static final Logger log = Logger.getLogger(EmailSender.class);

	private Properties props;
	private Session session;

	private TemplateConverter templateConv;

	private String subject = "TWVG > V1 status";

//	private static final String DOMEN = "@akvelon.com";

	private static final String MAIL_FILE_CONFIG = "src/mail_conf.properties";

	public EmailSender() throws FileNotFoundException, IOException {
		props = new Properties();
		props.load(new BufferedInputStream(new FileInputStream(new File(MAIL_FILE_CONFIG))));
		templateConv = new TemplateConverter();
	}

	public void sendTestNotificationsByTaskOwner(List<List<Report>> reports, List<HourReport> hreports) {
		if (CollectionUtils.isEmpty(reports)) {
			sendMessage("maria.serichenko@akvelon.com", null, templateConv.convertToHTMLNoRepotrs());
			return;
		}
//		sendMessage("maria.serichenko@akvelon.com", "anton.nagorny@akvelon.com", templateConv.convertToHTMLByTaskOwner(reports));
	}

	public void sendTestNotificationsByRepType(List<List<Report>> reports, List<HourReport> hReports) {
		if (CollectionUtils.isEmpty(reports)) {
			sendMessage("maria.serichenko@akvelon.com", null, templateConv.convertToHTMLNoRepotrs());
			return;
		}
		sendMessage("anton.nagorny@akvelon.com", null, templateConv.convertToHTMLByRepTypeAndHourReps(reports, hReports));
//		sendMessage("maria.serichenko@akvelon.com", "anton.nagorny@akvelon.com", templateConv.convertToHTMLByRepType(reports));
	}

/*	private String createEmail(String owner) {
		return owner.trim().replace("\\s+", ".").toLowerCase() + DOMEN;
	}*/

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
			if (!StringUtils.isBlank(cc)) {
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
