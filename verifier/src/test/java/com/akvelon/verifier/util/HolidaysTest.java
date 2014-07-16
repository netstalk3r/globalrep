package com.akvelon.verifier.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Calendar;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.akvelon.verifiers.util.Holidays;
import com.akvelon.verifiers.util.Util;

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
		assertFalse(holidays.hasMonthHolidays(Util.getToday()));
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MONTH, Calendar.MAY);
		assertTrue(holidays.hasMonthHolidays(cal));
		Calendar cal1 = Calendar.getInstance();
		cal1.set(Calendar.MONTH, Calendar.AUGUST);
		assertTrue(holidays.hasMonthHolidays(cal1));
	}
	
}
