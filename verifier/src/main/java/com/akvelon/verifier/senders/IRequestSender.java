package com.akvelon.verifier.senders;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public interface IRequestSender {
	
	String UTF8 = "UTF-8";

	int openSession() throws IOException;
	
	InputStream login(String username, String password) throws IOException;
	
	int closeSession() throws IOException;
	
	InputStream sendRequest(String url, Map<String,String> params) throws IOException;
	
}
