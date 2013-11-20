package com.akvelon.report;

import java.util.Date;

public class HourReport {

	private String teamMember;
	private double reportedHours;
	private double requiredHours;
	private Date sprintStartDate;

	public HourReport() {
		super();
	}

	public HourReport(String teamMember, double reportedHours, double requiredHours, Date springBeginDate) {
		super();
		this.teamMember = teamMember;
		this.reportedHours = reportedHours;
		this.requiredHours = requiredHours;
		this.sprintStartDate = springBeginDate;
	}

	public String getTeamMember() {
		return teamMember;
	}

	public void setTeamMember(String teamMember) {
		this.teamMember = teamMember;
	}

	public double getReportedHours() {
		return reportedHours;
	}

	public void setReportedHours(double reportedHours) {
		this.reportedHours = reportedHours;
	}

	public double getRequiredHours() {
		return requiredHours;
	}

	public void setRequiredHours(double requiredHours) {
		this.requiredHours = requiredHours;
	}

	public Date getSprintStartDate() {
		return sprintStartDate;
	}

	public void setSprintStartDate(Date springBeginDate) {
		this.sprintStartDate = springBeginDate;
	}

}
