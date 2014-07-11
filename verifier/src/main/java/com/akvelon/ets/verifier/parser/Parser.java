package com.akvelon.ets.verifier.parser;

import java.io.IOException;
import java.io.InputStream;

public interface Parser {

	public boolean isLogin(InputStream is) throws IOException;

	public double parseReportedHours(InputStream is) throws IOException;

}
