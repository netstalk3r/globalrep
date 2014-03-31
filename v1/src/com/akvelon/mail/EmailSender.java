package com.akvelon.mail;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

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

	private static final String MAIL_FILE_CONFIG = "mail_conf.properties";

	public EmailSender() throws FileNotFoundException, IOException {
		props = new Properties();
		InputStream is = new BufferedInputStream(new FileInputStream(new File(MAIL_FILE_CONFIG)));
		props.load(is);
		templateConv = new TemplateConverter();
	}

	public void sendTestNotificationsByTaskOwner(List<List<Report>> reports, List<HourReport> hReports) {
		if (CollectionUtils.isEmpty(reports)) {
			sendMessage(getRequiredProp("mail.sent.to"), props.getProperty("mail.sent.cc"), templateConv.convertToHTMLNoRepotrs(hReports));
			return;
		}
		sendMessage(getRequiredProp("mail.sent.to"), props.getProperty("mail.sent.cc"), templateConv.convertToHTMLByTaskOwner(reports));
	}

	public void sendTestNotificationsByRepType(List<List<Report>> reports, List<HourReport> hReports) {
		if (CollectionUtils.isEmpty(reports)) {
			sendMessage(getRequiredProp("mail.sent.to"), props.getProperty("mail.sent.cc"), templateConv.convertToHTMLNoRepotrs(hReports));
			return;
		}
		sendMessage(getRequiredProp("mail.sent.to"), props.getProperty("mail.sent.cc"), templateConv.convertToHTMLByRepTypeAndHourReps(reports, hReports));
//		sendMessage("anton.nagorny@akvelon.com", null, templateConv.convertToHTMLByRepTypeAndHourReps(reports, hReports));
	}

	private String getRequiredProp(String propName) {
		String prop = props.getProperty(propName);
		if (prop == null) throw new IllegalArgumentException("Mail property " + propName + " cannot be null");
		return prop;
	}

	private void sendMessage(String to, String cc, String text) {
		Properties properties = new Properties();
		properties.put("mail.transport.protocol", getRequiredProp("mail.transport.protocol"));
		properties.put("mail.smtp.host", getRequiredProp("mail.smtp.host"));
		properties.put("mail.smtp.port", getRequiredProp("mail.smtp.port"));

		session = Session.getInstance(properties, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(getRequiredProp("mail.user"), getRequiredProp("mail.password"));
			}
		});

		try {
			Message msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(props.getProperty("mail.user")));
			msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
			if (!StringUtils.isBlank(cc)) {
				StringTokenizer tokenizer = new StringTokenizer(",");
				while (tokenizer.hasMoreTokens()) {
					msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(tokenizer.nextToken()));
				}
			}
			msg.setSubject(StringUtils.isBlank(props.getProperty("mail.subject")) ? props.getProperty("mail.subject") : subject);
			msg.setContent(text, "text/html; charset=UTF8");

			Transport.send(msg);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}
