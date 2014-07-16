package com.akvelon.verifiers.senders;

import java.io.IOException;
import java.io.InputStream;

public interface IV1RequestSender {
	
	InputStream sendRequest(String login, String password, String urlWithParams) throws IOException;

}
