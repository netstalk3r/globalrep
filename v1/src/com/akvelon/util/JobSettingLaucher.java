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
	private static final String CORN_EXPRESSION = "%s %s %s ? * %s";
	private static final String HOURS_PATTERN = "((1\\d)|(2[0-3]))|(\\d)";
	private static final String MINUTES_SECONDS_PATTERN = "([1-5]\\d)|(\\d)";
	private static final String DAYS_PATTERN = "(SUN)|(MON)|(TUE)|(WED)|(FRI)|(THU)|(SAT)";

	private static String morningExpr;
	private static String eveningExpr;
	
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

	public static String getMorningExpr() {
		return morningExpr;
	}

	public static String getEveningExpr() {
		return eveningExpr;
	}

	private static void generateTimeExpr() {
		String time = "%s:%s:%s";
		String days = "Days - %s";
		log.info("Morning time - "
				+ String.format(time, timeProps.getProperty("morning.hour"), timeProps.getProperty("morning.minutes"),
						timeProps.getProperty("morning.seconds")));
		log.info("Evening time - "
				+ String.format(time, timeProps.getProperty("evening.hour"), timeProps.getProperty("evening.minutes"),
						timeProps.getProperty("evening.seconds")));
		log.info(String.format(days, timeProps.getProperty("week.days")));
		morningExpr = String.format(CORN_EXPRESSION, timeProps.getProperty("morning.seconds"), timeProps.getProperty("morning.minutes"),
				timeProps.getProperty("morning.hour"), timeProps.getProperty("week.days"));
		eveningExpr = String.format(CORN_EXPRESSION, timeProps.getProperty("evening.seconds"), timeProps.getProperty("evening.minutes"),
				timeProps.getProperty("evening.hour"), timeProps.getProperty("week.days"));
	}

	private static void loadProps() throws FileNotFoundException, IOException {
		timeProps = new Properties();
		timeProps.load(new BufferedInputStream(new FileInputStream(new File(TIME_PROP_FILE))));
	}

	private static List<String> validateProps() {
		List<String> errors = new ArrayList<String>();
		String incorrect = "Incorect ";

		String monHour = timeProps.getProperty("morning.hour");
		String evenHour = timeProps.getProperty("evening.hour");

		String monMin = timeProps.getProperty("morning.minutes");
		String evenMin = timeProps.getProperty("evening.minutes");

		String monSec = timeProps.getProperty("morning.seconds");
		String evenSec = timeProps.getProperty("evening.seconds");

		String weekDays = timeProps.getProperty("week.days");

		if (!monHour.matches(HOURS_PATTERN)) {
			errors.add(incorrect + "morning.hour");
		}
		if (!evenHour.matches(HOURS_PATTERN)) {
			errors.add(incorrect + "evening.hour");
		}
		if (!monMin.matches(MINUTES_SECONDS_PATTERN)) {
			errors.add(incorrect + "morning.minutes");
		}
		if (!evenMin.matches(MINUTES_SECONDS_PATTERN)) {
			errors.add(incorrect + "evening.minutes");
		}
		if (!monSec.matches(MINUTES_SECONDS_PATTERN)) {
			errors.add(incorrect + "morning.seconds");
		}
		if (!evenSec.matches(MINUTES_SECONDS_PATTERN)) {
			errors.add(incorrect + "evening.seconds");
		}

		String[] days = weekDays.split("-");
		if (days.length == 2) {
			if (!days[0].matches(DAYS_PATTERN) || !days[1].matches(DAYS_PATTERN)) {
				errors.add(incorrect + "week.days");
			}
		} else {
			errors.add(incorrect + "week.days");
		}

		return errors;
	}
}
