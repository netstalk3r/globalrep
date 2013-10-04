package com.akvelon.report;

/**
 * Class for report
 */
public class Report {

	private String bliID;
	private String bliName;
	private String ownerTaskName;
	private String reportName;
	
	public Report() {
		super();
	}
	
	public Report(String bliID, String bliName, String ownerTaskName, String reportName) {
		super();
		this.bliID = bliID;
		this.bliName = bliName;
		this.ownerTaskName = ownerTaskName;
		this.reportName = reportName;
	}
	
	public String getBliID() {
		return bliID;
	}
	public void setBliID(String bliID) {
		this.bliID = bliID;
	}
	public String getBliName() {
		return bliName;
	}
	public void setBliName(String bliName) {
		this.bliName = bliName;
	}
	public String getOwnerTaskName() {
		return ownerTaskName;
	}
	public void setOwnerTaskName(String ownerTaskName) {
		this.ownerTaskName = ownerTaskName;
	}
	public String getReportName() {
		return reportName;
	}
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
	
}
