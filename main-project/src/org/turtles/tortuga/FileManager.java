package org.turtles.tortuga;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;

public class FileManager {
	
	FileOutputStream fos;
	
	public FileManager(String filename, Context context) {		
		try {
			this.fos = openFileOutput(filename, context.MODE_PRIVATE);
			initializeFile();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void initializeFile() throws IOException {
		
		fos.write("@relation training".getBytes());
		fos.write("@attribute numericfield avg".getBytes());
		fos.write("@attribute numericfield std".getBytes());
		fos.write("@attribute numericfield total".getBytes());
		fos.write("@attribute numericfield maxFreq".getBytes());
		fos.write("@attribute numericfield maxFreqIndex".getBytes());
	}
	
	public void addDataToFile(int avg, int std, int total, int maxFreq, int maxFreqIndex) throws IOException {
		
		String string = avg + "," + std + "," + total + "," + maxFreq + "," + maxFreqIndex;
		fos.write(string.getBytes());
	}
	
	public void close() throws IOException {
		fos.close();
	}
}
