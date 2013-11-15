package com.akvelon.util;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class SAXBeginDateUtil extends DefaultHandler {

	Logger log = Logger.getLogger(SAXBeginDateUtil.class);
	
	private InputStream inputStream;
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	Calendar date;
	
	private boolean bDate = false;
	
	public SAXBeginDateUtil() {
		date = Calendar.getInstance();
	}
	
	public int parse(InputStream is) throws SAXException, IOException, ParserConfigurationException {
		this.inputStream = is;
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser parser = factory.newSAXParser();
		parser.parse(inputStream, this);
		return countDays();
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if (qName.equalsIgnoreCase("Attribute") && attributes.getValue(0).equalsIgnoreCase("BeginDate")) {
			bDate = true;
			return;
		}
	}
	
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		if (bDate) {
			try {
				Date d = sdf.parse(new String(ch, start, length));
				date.setTime(d);
				log.info("Spring begin date - " + date.getTime());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			bDate = false;
		}
	}
	
	private int countDays() {
		int daysQuantity = 0;
		
		Calendar today = Calendar.getInstance();
		today.setTime(new Date());
		today.set(Calendar.HOUR_OF_DAY, 0);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);
		today.set(Calendar.MILLISECOND, 0);
		
		log.info("Today - " + today.getTime());
		
		List<Integer> weekends = Arrays.asList(Calendar.SATURDAY, Calendar.SUNDAY);
		
		while (date.before(today)) {
			if (!weekends.contains(date.get(Calendar.DAY_OF_WEEK))) {
				daysQuantity++;
			}
			date.add(Calendar.DATE, 1);
		}
		int hoursPerDay = 7;
		return daysQuantity * hoursPerDay;
	}
}
