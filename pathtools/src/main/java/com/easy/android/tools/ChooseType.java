package com.easy.android.tools;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ChooseType {
	
	public static String chooseType(String filePath,String outPath, String typePath, String add, String end) throws IOException {
		File outFile = new File(outPath);
		File typeFile = new File(typePath);
		if(!typeFile.exists()){
			typeFile.getParentFile().mkdirs();
		}
		if(!outFile.exists()) {
			outFile.getParentFile().mkdirs();
		}else {
	        System.out.print("原类型：");
			FileUitl.printFile(outFile);
		}
		File selectFile = select(filePath);
		FileUitl.write2File(outFile,selectFile.getAbsolutePath(),false);
		FileUitl.write2File(typeFile,add + selectFile.getName() + end,false);
		System.out.print("选择的类型：");
		FileUitl.printFile(outFile);
		return selectFile.getAbsolutePath();
	}
	
	public static File select(String filepath) {
		String select = "";
		List<File> paths = new ArrayList<File>();
		File root = new File(filepath);
		if (root.exists()) {
			File[] files = root.listFiles();
			if (null == files || files.length == 0) {
				System.out.println(filepath+"文件夹是空的!");
				System.exit(1);
			} else {
				for (File file : files) {
					if (file.isDirectory()) {
						paths.add(file);
					}
				}
			}
		} else {
			System.out.println("文件不存在!");
			System.exit(1);
		}
		
		int i=0;
		System.out.println("选择类型：");
		for(i=1; i<=paths.size(); i++) {
			System.out.println("\t"+i+":"+paths.get(i-1).getName());
		}
		Scanner sc = new Scanner(System.in); 
		System.out.print("输入(int)：");
		int index = sc.nextInt();
		sc.close();
		return paths.get(index-1);
	}
	

}
