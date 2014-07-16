package com.akvelon.verifier.util;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import com.akvelon.verifiers.util.Util;
import com.akvelon.verifiers.util.V1UrlBuilder;

public class V1UrlBuilderTest {

	@Test
	public void testBuildUrlSprintStart() throws IOException {
		String expected = "https://www3.v1host.com/Tideworks/VersionOne/rest-1.v1/Data/Timebox?sel=BeginDate&where=Team.Name='MS%20Gate','MS%20Vanguard';Timebox.Name='MS1410'";
		String actual = V1UrlBuilder.buildUrl(Util.loadProperties("sprint_start_date.properties"));
		System.out.println(actual);
		assertEquals(expected, actual);
	}
	
	@Test
	public void testBuildUrlActual() throws IOException {
		String expected = "https://www3.v1host.com/Tideworks/VersionOne/rest-1.v1/Data/Actual?sel=Value,Member.Name&sort=Member.Nickname&where=Member.Nickname='mseriche','aarnauto','ATimofeev','DVolkov','OKrupenya','OlegZ','SergiiI','MaksimK';Team.Name='MS%20Gate','MS%20Vanguard';Timebox.Name='MS1410'";
		String actual = V1UrlBuilder.buildUrl(Util.loadProperties("actual.properties"));
		System.out.println(actual);
		assertEquals(expected, actual);
	}
	
}
