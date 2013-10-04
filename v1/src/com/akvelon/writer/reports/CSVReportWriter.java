package com.akvelon.writer.reports;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.akvelon.report.Report;

/**
 * Implementing {@link ReportWriter} to write csv report
 */
public class CSVReportWriter implements ReportWriter {

	private String fileName;
	private Writer writer;
	private SimpleDateFormat formatter;

	private String EXTENSION = ".csv";

	private String line = "%s,%s,%s,%s\n";

	private List<List<Report>> reports;

	public CSVReportWriter() {
		formatter = new SimpleDateFormat(DATE_FORMAT);
		fileName = formatter.format(new Date());
		reports = new ArrayList<List<Report>>();
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
			writer.write(HEAD);
			for (List<Report> reps : reports) {
				for (Report rep : reps) {
					writer.write(String.format(line, rep.getBliID(), rep.getBliName(), rep.getOwnerTaskName(), rep.getReportName()));
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

	@Override
	public void addReports(List<Report> reports) {
		this.reports.add(reports);
	}

}
