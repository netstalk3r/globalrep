package com.test.parseXML;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.test.parseXML.http.Request;
import com.test.parseXML.report.Report;
import com.test.parseXML.util.ReportWriter;
import com.test.parseXML.util.SAXUtil;

public class App {

	public static Properties setting;
	private static Scanner scanIn;
	private Request req;
	private Report rep;
	private SAXUtil parser;
	private ReportWriter repWr;

	private static final Integer EXIT = 4;

	static {
		try {
			setting = new Properties();
			setting.load(new FileInputStream("src\\main\\resources\\settings.properties"));
			scanIn = new Scanner(System.in);
		} catch (Exception e) {
			System.err.println("File properties not found!!!");
			e.printStackTrace();
			System.exit(0);
		}
	}

	public App() {
		req = new Request();
		rep = new Report();
		parser = new SAXUtil(rep);
		repWr = new ReportWriter();
	}

	/**
	 * this comment needs to check commit and push
	 * 
	 * @param args
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws IOException {
		App app = new App();
		while (true) {
			System.out.println("1 - make request.");
			System.out.println("2 - view reult.");
			System.out.println("3 - write result to file.");
			System.out.println("4 - exit.");
			System.out.print(">");
			String res = scanIn.nextLine();
			try {
				Integer par = Integer.parseInt(res);
				if (par.equals(EXIT)) {
					break;
				}
				app.action(par);
			} catch (NumberFormatException e) {
				System.err.println("Wrong argument = " + res);
				System.out.print(">");
				scanIn.nextLine();
			} catch (IOException e) {
				System.err.println("Problem with request or write to file:");
				e.printStackTrace();
				System.out.print(">");
				scanIn.nextLine();
			}
		}
	}

	public void action(Integer par) throws IOException {
		switch (par) {
		case 1:// make request and write result to report
			try {
				InputStream resp = req.send();
				if (resp != null) {
					parser.parse(resp);
				} else {
					System.err.println("Problem with request, response is null; status code is " + req.getStatusCode());
				}
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
			}
			break;
		case 2:// view result;
			viewReport();
			break;
		case 3:// write to file
			writeToFile();
			break;
		}
	}

	public void writeToFile() throws IOException {
		repWr.writeReport(rep);
	}

	public void viewReport() {
		if (rep.isEmpty()) {
			System.out.println("Report is empty.");
			return;
		}
		for (String line : rep.getLines()) {
			System.out.println(line);
		}
	}
}