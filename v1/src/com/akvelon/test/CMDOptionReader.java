package com.akvelon.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

import org.apache.log4j.Logger;

public class CMDOptionReader {
	
	private static final Logger log = Logger.getLogger(CMDOptionReader.class);
	
	private String exitCommand = "";
	
	public CMDOptionReader(String exitCommand) {
		this.exitCommand = exitCommand;
	}
	
	public String readOption(Map<String, String> options) {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String answer = "";
		
		do {
			log.info("Select option: ");
			log.info(exitCommand + " - to exit");
			for (String option : options.keySet()) {
				log.info(option + " - " + options.get(option));
			}
			try {
		         answer = br.readLine();
		      } catch (IOException ioe) {
		    	  log.info("IO error trying to read your name!");
		      }
		} while (!options.keySet().contains(answer) && !exitCommand.equals(answer));
		
		return answer;
	}
}
