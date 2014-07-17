package com.akvelon.verifiers.parser;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.lang.CharEncoding;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.akvelon.verifiers.util.Constants;

public class HtmlParser implements ETSParser {

	private static final Logger log = Logger.getLogger(HtmlParser.class);

	private static final String LOGIN_FORM = "form[name=loginform]";
	private static final String TABLE_ROW_TOTAL_TD = ".TableRowTotal>td";

	public boolean isLogin(InputStream is) throws IOException {
		Document document = Jsoup.parse(is, CharEncoding.UTF_8, Constants.REPORTED_HOURS_URL);
		// after successful login you should not see login form
		return document.select(LOGIN_FORM).size() == 0;
	}

	public double parseReportedHours(InputStream is) throws IOException {
		Document document = Jsoup.parse(is, CharEncoding.UTF_8, Constants.REPORTED_HOURS_URL);
		try {
			// get value from the second column in the row with total values
			return Double.parseDouble(document.select(TABLE_ROW_TOTAL_TD).get(1).text());
		} catch (NumberFormatException ex) {
			log.error("Number Format Exception for " + ex.getMessage() + "; -1 will be returned");
			return -1;
		}
	}

}
