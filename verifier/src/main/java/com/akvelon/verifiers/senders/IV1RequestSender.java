package com.akvelon.verifiers.senders;

import java.io.IOException;
import java.io.InputStream;

public interface IV1RequestSender {
	
	InputStream sendRequest(String urlWithParams) throws IOException;

}
