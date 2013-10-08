package com.akvelon.writer.reports;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.List;

import com.akvelon.report.Report;

/**
 * Implementing {@link ReportWriter} to write csv report
 */
public class CSVReportWriter extends ReportWriter {

	private Writer writer;

	private String EXTENSION = ".csv";

	private String line = "%s,%s,%s,%s,%s\n";

	public CSVReportWriter() {
		super();
	}

	private void openFile() throws UnsupportedEncodingException, FileNotFoundException {
		writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName + EXTENSION), ENCODING));
	}

	@Override
	public void writeReport() throws IOException {
		if (reports.isEmpty()) {
			System.out.println("No reports to write");
			return;
		}
		try {
			openFile();
			writer.write(HEAD + "\n");
			for (List<Report> reps : reports) {
				for (Report rep : reps) {
					writer.write(String.format(line, rep.getBliID(), rep.getBliName(), rep.getBliOwner(), rep.getTaskOwner(),
							rep.getReportName()));
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			closeFile();
		}
	}

	private void closeFile() throws IOException {
		writer.close();
	}

}
