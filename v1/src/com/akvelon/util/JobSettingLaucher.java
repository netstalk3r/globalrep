package com.akvelon.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

public class JobSettingLaucher {

	private static Properties timeProps;
	private static final String TIME_PROP_FILE = "job_time_config.properties";
	private static final String CORN_EXPRESSION = "0 %s,%s %s,%s ? * %s";
	private static final String HOURS_PATTERN = "((1\\d)|(2[0-3]))|(\\d)";
	private static final String MINUTES_PATTERN = "([1-5]\\d)|(\\d)";
	private static final String DAYS_PATTERN = "(SUN)|(MON)|(TUE)|(WED)|(FRI)|(THU)|(SAT)";

	private static String mornHours;
	private static String evenHours;
	private static String mornMin;
	private static String evenMin;
	private static String days;

	private static String timeExpr;

	private static final Logger log;

	static {
		log = Logger.getLogger(JobSettingLaucher.class);
		try {
			loadProps();
			List<String> errors = validateProps();
			if (!errors.isEmpty()) {
				for (String error : errors) {
					log.error(error);
				}
				System.exit(0);
			}
			generateTimeExpr();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	public static String getTimeExpr() {
		return timeExpr;
	}

	private static void generateTimeExpr() {
		String time = "%s:%s";
		String daysPat = "Days - %s";
		log.info("Morning time - " + String.format(time, mornHours, mornMin));
		log.info("Evening time - " + String.format(time, evenHours, evenMin));
		log.info(String.format(daysPat, days));
		timeExpr = String.format(CORN_EXPRESSION, mornMin, evenMin, mornHours, evenHours, days);
		log.info("Time expression: " + timeExpr);
	}

	private static void loadProps() throws FileNotFoundException, IOException {
		timeProps = new Properties();
		timeProps.load(new BufferedInputStream(new FileInputStream(new File(TIME_PROP_FILE))));
	}

	private static List<String> validateProps() {
		List<String> errors = new ArrayList<String>();
		String incorrect = "Incorect ";

		mornHours = removeZeroIfStartsWithZero(timeProps.getProperty("morning.hour"));
		evenHours = removeZeroIfStartsWithZero(timeProps.getProperty("evening.hour"));

		mornMin = removeZeroIfStartsWithZero(timeProps.getProperty("morning.minutes"));
		evenMin = removeZeroIfStartsWithZero(timeProps.getProperty("evening.minutes"));

		if (!mornHours.matches(HOURS_PATTERN)) {
			errors.add(incorrect + "morning.hour");
		}
		if (!evenHours.matches(HOURS_PATTERN)) {
			errors.add(incorrect + "evening.hour");
		}
		if (!mornMin.matches(MINUTES_PATTERN)) {
			errors.add(incorrect + "morning.minutes");
		}
		if (!evenMin.matches(MINUTES_PATTERN)) {
			errors.add(incorrect + "evening.minutes");
		}

		String[] dayss = timeProps.getProperty("week.days").split("-");
		if (dayss.length == 2) {
			if (!dayss[0].matches(DAYS_PATTERN) || !dayss[1].matches(DAYS_PATTERN)) {
				errors.add(incorrect + "week.days");
			} else {
				days = timeProps.getProperty("week.days");
			}
		} else {
			errors.add(incorrect + "week.days");
		}

		return errors;
	}

	private static String removeZeroIfStartsWithZero(String time) {
		return (time.length() > 1 && time.startsWith("0")) ? time.substring(1) : time;
	}
}
