package com.easy.android.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class FileUitl {
	
	public static void write2File(File file, String content,boolean option) throws IOException {
		BufferedWriter bufferedWriter =  getWriter(file,"utf-8",option);
		bufferedWriter.write(content);
		bufferedWriter.flush();
		bufferedWriter.close();
	}

	public static void printFile(File file) throws IOException {

		System.out.println(readfFile(file));
	}
	
	public static String readfFile(File file) throws IOException {
		BufferedReader bufferedReader = getReader(file,"utf-8");
		StringBuilder sb = new StringBuilder();
		String lineTxt = null;
		while ((lineTxt = bufferedReader.readLine()) != null) {
			sb.append(lineTxt+"\n");
		}
		bufferedReader.close();
		return sb.toString();
	}
	
	public static BufferedWriter getWriter(File file, String encoding,boolean option) throws IOException {
	    return new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file,option), encoding));    
	}
	
	public static BufferedReader getReader(File file, String encoding) throws IOException {
	    return new BufferedReader(new InputStreamReader(new FileInputStream(file), encoding));
	}
	
}
