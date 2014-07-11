package com.akvelon.ets.verifier.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class Holidays {

	private Map<Integer, Integer> holidays;
	
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
		holidays = new HashMap<Integer, Integer>();
		String[] dates = out.toString().split(DELIMETER_FOR_DATES);
		for (String date : dates) {
			String[] splittedDate = date.split(DELIMETER_FOR_DATE);
			int month = Integer.parseInt(splittedDate[1]);
			if (holidays.get(month) == null) {
				holidays.put(month, 1);
			} else {
				Integer increment = holidays.get(month);
				holidays.put(month, ++increment);
			}
		}
	}
	
	public boolean hasMonthHolidays(Calendar month) {
		Integer monthNumber = Integer.valueOf(month.get(Calendar.MONTH));
		return holidays.keySet().contains(monthNumber);
	}
	
	public int getAmountOfHolidaysForMonth(Calendar month) {
		Integer monthNumber = Integer.valueOf(month.get(Calendar.MONTH));
		return holidays.get(monthNumber);
	}
	
	public Map<Integer,Integer> getHolidays() {
		return holidays;
	}
}
