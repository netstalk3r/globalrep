package com.akvelon.verifiers.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

public class Util {

	private static final Logger log = Logger.getLogger(Util.class);
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
	private static final Calendar today = Calendar.getInstance();
	private static Calendar beginDateOfMonth = null;
	private static Calendar endDateOfMonth = null;

	static {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		beginDateOfMonth = calendar;
		calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		endDateOfMonth = calendar;
	}

	public static Calendar getBeginDateOfMonth() {
		return beginDateOfMonth;
	}

	public static Calendar getToday() {
		return today;
	}

	public static Calendar getEndDateOfTheMonth() {
		return endDateOfMonth;
	}

	public static String getToDayForRequest() {
		return sdf.format(today.getTime());
	}

	public static String getBeginOfMonthForRequest() {
		return sdf.format(beginDateOfMonth.getTime());
	}

	public static String getEndOfMonthForRequest() {
		return sdf.format(endDateOfMonth.getTime());
	}

	public static int getCurrentYear() {
		return today.get(Calendar.YEAR);
	}
	
	public static String dateToString(Date date) {
		return sdf.format(date);
	}
	
	public static Map<String, String> getDefaultRequestParams() {
		Map<String, String> params = new HashMap<String, String>();
		params.put(Constants.ORDER_BY, Constants.EMPTY_PARAM);
		params.put(Constants.ORDER, Constants.EMPTY_PARAM);
		params.put(Constants.CONTROL, Constants.CONTROL_DEFAULT_OPTION);
		params.put(Constants.ACCOUNT, Constants.EMPTY_PARAM);
		params.put(Constants.PROJECT, Constants.PROJECT_DEFAULT_OPTION);
		params.put(Constants.STATUS, Constants.STATUS_DEFAULT_OPTION);
		params.put(Constants.TYPE, Constants.TYPE_DEFAULT_OPTION);
		params.put(Constants.DATE_FROM, getBeginOfMonthForRequest());
		params.put(Constants.DATE_TO, getEndOfMonthForRequest());
		params.put(Constants.CHANGE_PERIOD, Constants.CHANGE_PERIOD_DEFAULT_OPTION);
		return params;
	}

	public static String convertToEmailAddress(String account) {
		return account.replace("ua.", Constants.EMPTY_PARAM);
	}
	
	public static Properties loadProperties(String fileName) throws IOException {
		BufferedReader reader = null;
		Properties props = new Properties();
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
			props.load(reader);
		} finally {
			reader.close();
		}
		return props;
	}
	
	public static Properties loadInternalProperties(String fileName) throws IOException {
		BufferedInputStream is = null;
		Properties props = new Properties();
		try {
			is = new BufferedInputStream(Util.class.getClassLoader().getResourceAsStream(fileName));
			props.load(is);
		} finally {
			is.close();
		}
		return props;
	}

	
	public static int calculateWorkingHoursBetweenDates(Calendar startDate, Calendar endDate) {

		boolean includeToday = includeToday(endDate);
		
		log.info("Calculate working hours between " + Util.dateToString(startDate.getTime()) + " and "
				+ Util.dateToString(endDate.getTime()));

		int daysQuantity = 0;
		
		if (Holidays.hasMonthHolidays(startDate)) {
			daysQuantity -= Holidays.getAmountOfHolidaysBetweenDatesWithinMonth(startDate, endDate);
		}
		
		endDate.add(Calendar.DATE, 1);

		while (startDate.before(endDate)) {
			if (!isWeekend(startDate.get(Calendar.DAY_OF_WEEK))) {
				daysQuantity++;
			}
			startDate.add(Calendar.DATE, 1);
		}

		log.info("This day is" + (includeToday ? "" : " not") + " included.");
		if (!includeToday) {
			daysQuantity--;
		}
		
		log.info("Amount of working days: " + daysQuantity);

		int amountOfWorkingHours = daysQuantity * 8;

		log.info("Amount of working hours: " + amountOfWorkingHours);

		return amountOfWorkingHours;
	}

	private static boolean isWeekend(int date) {
		return (date == Calendar.SATURDAY) || (date == Calendar.SUNDAY);
	}
	
	// if the current time is 7 PM or later  this day is included to count of working hours
	private static boolean includeToday(Calendar today) {
		return today.get(Calendar.AM_PM) == Calendar.PM ? today.get(Calendar.HOUR) >= 7 : false;
	}
}
