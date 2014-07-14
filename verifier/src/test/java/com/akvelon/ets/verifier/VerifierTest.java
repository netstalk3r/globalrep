package com.akvelon.ets.verifier;

import java.io.IOException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.akvelon.ets.verifier.util.Util;

@Ignore
public class VerifierTest {

	Verifier verifier;

	@Before
	public void setUp() throws IOException {
		verifier = new Verifier();
	}

	@Test
	public void testCalculateWorkingHoursForMonth() {
		// now private
//		 System.out.println(verifier.calculateWorkingHoursBetweenDates(Util.getBeginDateOfMonth(), Util.getToday()));
	}

}
