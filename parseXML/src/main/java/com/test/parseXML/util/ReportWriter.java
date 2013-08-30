package com.test.parseXML.util;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import com.test.parseXML.App;
import com.test.parseXML.report.Report;

public class ReportWriter {

	private String fileName;
	private static final String ENCODING = "UTF-8";
	private Writer writer;

	public ReportWriter() {
		fileName = App.setting.getProperty("result.file.name");
	}

	private void openFile() throws UnsupportedEncodingException, FileNotFoundException {
		writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), ENCODING));
	}

	public void writeReport(Report rep) throws IOException {
		if (rep.isEmpty()) {
			System.out.println("Report is empty.");
			return;
		}
		try {
			openFile();
			for (String asset : rep.getLines()) {
				writer.write(asset + "\n");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			closeFile();
		}
	}

	private void closeFile() throws IOException {
		writer.close();
	}
}
