package com.akvelon.verifier;

import java.io.IOException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.akvelon.verifiers.ETSVerifier;
import com.akvelon.verifiers.util.Util;

@Ignore
public class VerifierTest {

	ETSVerifier verifier;

	@Before
	public void setUp() throws IOException {
		verifier = new ETSVerifier();
	}

	@Test
	public void testCalculateWorkingHoursForMonth() {
		// now private
//		 System.out.println(verifier.calculateWorkingHoursBetweenDates(Util.getBeginDateOfMonth(), Util.getToday()));
	}

}
