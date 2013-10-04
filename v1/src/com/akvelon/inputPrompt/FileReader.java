package com.akvelon.inputPrompt;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class FileReader {

	public List<File> readFolderFiles(String folderPath) {
		File folder = new File(folderPath);
		File[] listOfFiles = folder.listFiles();
	
		for (File file : listOfFiles) {
		    if (file.isFile()) {
		    }
		}
		return Arrays.asList(listOfFiles);
	}
}
