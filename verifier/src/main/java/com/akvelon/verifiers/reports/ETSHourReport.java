package com.akvelon.verifiers.reports;

public class ETSHourReport implements Comparable<ETSHourReport> {

	private String name;
	private String email;
	private double hours;

	public ETSHourReport() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getHours() {
		return hours;
	}

	public void setHours(double hours) {
		this.hours = hours;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		ETSHourReport other = (ETSHourReport) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "PersonalHourReport [name=" + name + ", email=" + email + ", hours=" + hours + "]";
	}

	@Override
	public int compareTo(ETSHourReport o) {
		return this.getName().compareTo(o.getName());
	}

	
}
