package com.akvelon.verifier.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Holidays {

	private Map<Integer, List<Integer>> holidays;
	
	private static final String FILE_WITH_HOLIDAYS = "holidays";

	private static final String DELIMETER_FOR_DATES = "\\+";
	private static final String DELIMETER_FOR_DATE = "\\.";

	public Holidays() throws IOException {
		BufferedReader reader = null;
		StringBuilder out = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(FILE_WITH_HOLIDAYS)));
			out = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				out.append(line);
			}
		} finally {
			reader.close();
		}
		holidays = new HashMap<Integer, List<Integer>>();
		String[] dates = out.toString().split(DELIMETER_FOR_DATES);
		for (String date : dates) {
			String[] splittedDate = date.split(DELIMETER_FOR_DATE);
			int month = Integer.parseInt(splittedDate[1].trim()) - 1;
			if (holidays.get(month) == null) {
				List<Integer> days = new ArrayList<Integer>(3);
				days.add(Integer.parseInt(splittedDate[0].trim()));
				holidays.put(month, days);
			} else {
				holidays.get(month).add(Integer.parseInt(splittedDate[0].trim()));
			}
		}
	}
	
	public boolean hasMonthHolidays(Calendar month) {
		return holidays.keySet().contains(Integer.valueOf(month.get(Calendar.MONTH)));
	}
	
	public int getAmountOfHolidaysBetweenDatesWithinMonth(Calendar from, Calendar to) {
		List<Integer> holidayDates = holidays.get(Integer.valueOf(from.get(Calendar.MONTH)));
		int amountOfDaysOff = 0;
		for (Integer dayOff : holidayDates) {
			if (from.get(Calendar.DATE) <= dayOff && dayOff <= to.get(Calendar.DATE)) {
				amountOfDaysOff++;
			}
		}
		return amountOfDaysOff;
	}
	
	public Map<Integer,List<Integer>> getHolidays() {
		return holidays;
	}
}
