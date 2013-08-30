package com.test.parseXML.report;

import java.util.ArrayList;
import java.util.List;

public class Report {

	private List<String> reportLines;
	
	public Report() {
		reportLines = new ArrayList<String>();
	}
	
	public void setLine(String line) {
		reportLines.add(line);
	}
	
	public List<String> getLines() {
		return reportLines;
	}
	
	public boolean isEmpty() {
		return reportLines.isEmpty();
	}
}
