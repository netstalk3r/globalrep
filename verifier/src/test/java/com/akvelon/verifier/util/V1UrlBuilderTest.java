package com.akvelon.verifier.util;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.akvelon.verifiers.util.Util;
import com.akvelon.verifiers.util.V1UrlBuilder;

@Ignore
public class V1UrlBuilderTest {

	private V1UrlBuilder urlBuilder;
	
	@Before
	public void setUp() throws IOException {
		urlBuilder = new V1UrlBuilder();
	}
	
	@Test
	public void testBuildUrlSprintStart() throws IOException {
		String expected = "https://www3.v1host.com/Tideworks/VersionOne/rest-1.v1/Data/Timebox?sel=BeginDate&where=Team.Name='MS%20Gate','MS%20Vanguard';Timebox.Name='MS1409'";
		String actual = urlBuilder.buildUrlTimebox(Util.loadProperties("v1.properties"));
		System.out.println(actual);
		assertEquals(expected, actual);
	}
	
	@Test
	public void testBuildUrlActual() throws IOException {
		String expected = "https://www3.v1host.com/Tideworks/VersionOne/rest-1.v1/Data/Actual?sel=Value,Member.Name&sort=Member.Nickname&where=Member.Nickname='mseriche','aarnauto','ATimofeev','DVolkov','OKrupenya','OlegZ','SergiiI','MaksimK';Team.Name='MS%20Gate','MS%20Vanguard';Timebox.Name='MS1409'";
		String actual = urlBuilder.buildUrlActual(Util.loadProperties("v1.properties"));
		System.out.println(actual);
		assertEquals(expected, actual);
	}
	
}
