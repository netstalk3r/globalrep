package com.akvelon.verifier.util;
import static org.junit.Assert.*;

import java.util.Calendar;

import org.junit.Ignore;
import org.junit.Test;

import com.akvelon.verifiers.util.Util;

@Ignore
public class UtilTest {

	@Test
	public void testStartAndEndDateOfCurrentMonth() {
		System.out.println(Util.getBeginOfMonthForRequest());
		System.out.println(Util.getToDayForRequest());
		System.out.println(Util.getEndOfMonthForRequest());
	}
	
	@Test
	public void testConvertToEmailAddress() {
		assertEquals("anton.nagorny@akvelon.com", Util.convertToEmailAddress("anton.nagorny@ua.akvelon.com"));
	}
	
	@Test
	public void testGetTime() {
		Calendar today = Util.getToday();
		System.out.println(today.get(Calendar.HOUR));
		System.out.println(today.get(Calendar.AM_PM) == Calendar.AM);
	}
	
}
