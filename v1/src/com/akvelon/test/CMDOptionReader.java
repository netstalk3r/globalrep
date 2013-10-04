package com.akvelon.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

public class CMDOptionReader {
	private String exitCommand = "";
	
	public CMDOptionReader(String exitCommand) {
		this.exitCommand = exitCommand;
	}
	
	public String readOption(Map<String, String> options) {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String answer = "";
		
		do {
			System.out.println("Select option: ");
			System.out.println(exitCommand + " - to exit");
			for (String option : options.keySet()) {
				System.out.println(option + " - " + options.get(option));
			}
			try {
		         answer = br.readLine();
		      } catch (IOException ioe) {
		         System.out.println("IO error trying to read your name!");
		      }
		} while (!options.keySet().contains(answer) && !exitCommand.equals(answer));
		
		return answer;
	}
}
