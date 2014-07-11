package com.akvelon.ets.verifier.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Calendar;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class HolidaysTest {

	private Holidays holidays;
	
	@Before
	public void setUp() throws IOException {
		holidays = new Holidays();
	}
	
	@Test
	public void testGetAllHolidays() {
		System.out.println(holidays.getHolidays());
	}
	
	@Test
	public void testIsHoliday() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, Calendar.MAY);
		assertTrue(holidays.hasMonthHolidays(cal));
		Calendar cal1 = Calendar.getInstance();
		cal1.set(Calendar.MONTH, Calendar.AUGUST);
		assertTrue(holidays.hasMonthHolidays(cal1));
	}
	
	@Test
	public void testGetHolidaysForMonth() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, Calendar.MAY);
		assertEquals(3, holidays.getAmountOfHolidaysForMonth(cal));
	}
}
