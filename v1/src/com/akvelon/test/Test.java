package com.akvelon.test;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ReportChecker checker = new ReportChecker();
		try {
			checker.checkBatchReport("./src/reports/daily/");
			//checker.checkSingleReport("./src/reports/daily/");
		} catch (Exception e) {
			System.out.println("failed to read report properties.");
			e.printStackTrace();
		}
	}
}
