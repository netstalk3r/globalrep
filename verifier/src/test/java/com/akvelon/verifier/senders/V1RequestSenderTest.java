package com.akvelon.verifier.senders;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.Test;

import com.akvelon.verifiers.senders.IV1RequestSender;
import com.akvelon.verifiers.senders.V1RequestSender;

public class V1RequestSenderTest {

	IV1RequestSender requestSender = new V1RequestSender();
	
	@Test
	public void testSendRequest() throws IOException {
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(requestSender.sendRequest("login", "password", "https://www3.v1host.com/Tideworks/VersionOne/rest-1.v1/Data/Timebox?sel=BeginDate&where=Team.Name='MS%20Gate','MS%20Vanguard';Timebox.Name='MS1410'")));
		String inputLine;
		StringBuffer response = new StringBuffer();
 
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		System.out.println(response.toString());
	}
	
}
