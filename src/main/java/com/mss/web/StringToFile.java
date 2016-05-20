package com.mss.web;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class StringToFile {

	public File stringToFile(String string){
	
		File file = new File("input.xml");
		 FileOutputStream fop;
		try {
			fop = new FileOutputStream(file);
		
		 if (!file.exists()) {
				file.createNewFile();
			}
		byte[] contentInBytes = string.getBytes();
		fop.write(contentInBytes);
		fop.flush();
		fop.close();
				
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return file;
	}
	
}
