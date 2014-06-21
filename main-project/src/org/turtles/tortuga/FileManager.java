package org.turtles.tortuga;

import android.content.Context;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;

public class FileManager {
	
	FileOutputStream fos;
	
	public FileManager(String filename, Context context) throws IOException {		
		this.fos = context.openFileOutput(filename, context.MODE_PRIVATE);
		initializeFile();
	}
	
	public void initializeFile() throws IOException {
		fos.write("@relation training\n".getBytes());
		fos.write("@attribute avg real\n".getBytes());
		fos.write("@attribute std real\n".getBytes());
		fos.write("@attribute total real\n".getBytes());
		fos.write("@attribute maxFreq real\n".getBytes());
		fos.write("@attribute maxFreqIndex real\n".getBytes());
		fos.write("@attribute class {right, down, left, up, none}\n\n".getBytes());
		fos.write("@data\n".getBytes());
	}
	
	public void append(double avg, double std, double total, double maxFreq, double maxFreqIndex, String classname) throws IOException {
		String string = "\n" + avg + "," + std + "," + total + "," + maxFreq + "," + maxFreqIndex + "," + classname;
		System.out.println(string);
		fos.write(string.getBytes());
	}
	
	public void close() throws IOException {
		fos.close();
	}
}
