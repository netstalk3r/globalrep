package com.akvelon.report;

/**
 * Class for report
 */
public class Report {

	private String bliID;
	private String bliName;
	private String bliOwner;
	private String taskName;
	private String taskOwner;
	private String reportName;

	public Report() {
		super();
	}

	public Report(String bliID, String bliName, String bliOwner, String taskName, String taskOwner, String reportName) {
		super();
		this.bliID = bliID;
		this.bliName = bliName;
		this.bliOwner = bliOwner;
		this.taskName = taskName;
		this.taskOwner = taskOwner;
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

	public String getBliOwner() {
		return bliOwner;
	}

	public void setBliOwner(String bliOwner) {
		this.bliOwner = bliOwner;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getTaskOwner() {
		return taskOwner;
	}

	public void setTaskOwner(String taskOwner) {
		this.taskOwner = taskOwner;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

}
