package com.akvelon.verifiers.parser;

import java.io.IOException;
import java.io.InputStream;

public interface ETSParser {

	public boolean isLogin(InputStream is) throws IOException;

	public double parseReportedHours(InputStream is) throws IOException;

}
