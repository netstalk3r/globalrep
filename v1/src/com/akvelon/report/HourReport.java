package com.akvelon.report;

public class HourReport {

	private String teamMember;
	private double reportedHours;
	private double requiredHours;

	public HourReport() {
		super();
	}

	public HourReport(String teamMember, double reportedHours, double requiredHours) {
		super();
		this.teamMember = teamMember;
		this.reportedHours = reportedHours;
		this.requiredHours = requiredHours;
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
	
}
