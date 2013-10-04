package com.akvelon.test;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.akvelon.inputPrompt.FileReader;

public class FileOptionsReader {
	private String folderPath = "";
	
	public FileOptionsReader(String rootFolder) {
		this.folderPath = rootFolder;
	}
	
	private Map<String, String> convertToOptions(List<File> reports) {
		Map<String, String> options = new HashMap<String, String>();
		int counter = 1;
		for(File report : reports) {
			options.put(String.valueOf(counter), report.getName());
			counter++;
		}
		return options;
	}
	
	public Map<String, String> readOptions() {
		FileReader fileReader = new FileReader();
		List<File> files = fileReader.readFolderFiles(folderPath);
		Map<String,String> options = this.convertToOptions(files);
		
		return options;
	}
}
