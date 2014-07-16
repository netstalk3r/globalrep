package com.akvelon.verifiers.reports;

public class V1HourReport {
	
	private String name;
	private double reportedHours;
	
	public V1HourReport() {
		super();
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public double getReportedHours() {
		return reportedHours;
	}

	public void setReportedHours(double reportedHours) {
		this.reportedHours = reportedHours;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		long temp;
		temp = Double.doubleToLongBits(reportedHours);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		V1HourReport other = (V1HourReport) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (Double.doubleToLongBits(reportedHours) != Double.doubleToLongBits(other.reportedHours))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "V1HourReport [name=" + name + ", requiredHours=" + reportedHours + "]";
	}
	

}
