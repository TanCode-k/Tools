package com.easy.android.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

public class Config {
	
	private static ArrayList<InforItem> configInfor = new ArrayList<InforItem>();

	public static void config(String configFile,String outFile) throws IOException {
//		config(configFile,outFile,"ext{\n","}\n","\t");
		config(configFile,outFile,"\n\n##setting\n\n","\n\n","");
	}

	public static void config(String configFile,String outFile,String head,String tail,String add) throws IOException {
		File config = new File(configFile);
		if(!config.exists()) {
			System.out.println(configFile+"文件不存在！");
			System.exit(1);
		}
		getConfig(config);
		Scanner sc = new Scanner(System.in);
		printConfig();
		guide();
		while(true) {
			System.out.print("输入命令：");
			String opt = sc.next();
			if(opt.equals("save")) {
				break;
			}else {
				int index = -1;
				try {
					 index = Integer.parseInt(opt);
					 if(index<1||index>configInfor.size()) {
						 System.out.println("错误输入："+opt);
						 continue;
					 }else {
						 String value = sc.next();
						 configInfor.set(index-1, configInfor.get(index-1).setValue(value));
						 configInfor.get(index-1).printValue("\t");
					 }
				}catch(Exception e) {
					System.out.println("错误输入："+opt);
					continue;
				}
			}
		}
		sc.close();
		writeConfigToFile(outFile,head,tail,add);
		printConfigValue();
		changConfig(config);
	}
	
	private static void changConfig(File config) throws IOException {
		FileUitl.write2File(config, "", false);
		for(int i=0; i<configInfor.size();i++) {
			FileUitl.write2File(config, configInfor.get(i).getConfigLine()+"\n",true);
		}
	}
	
	private static void writeConfigToFile(String outFile,String head,String tail,String add) throws IOException {
		File out = new File(outFile);
		if(!out.exists()) {
			out.getParentFile().mkdirs();
		}
		FileUitl.write2File(out, head, false);
		for(int i=0; i<configInfor.size();i++) {
			FileUitl.write2File(out, add+configInfor.get(i).ngetValue()+"\n",true);
		}
		FileUitl.write2File(out, tail, true);
	}
	
	private static void printConfig() {
		printConfigMark();
		printConfigValue();
	}
	
	private static void printConfigMark() {
		System.out.println("配置选项说明：");
		for(int i=1; i<=configInfor.size();i++) {
			configInfor.get(i-1).printMark("\t"+i+": ");
		}
		System.out.println("\n");
	}
	
	private static void printConfigValue() {
		System.out.println("配置信息：");
		for(int i=1; i<=configInfor.size();i++) {
			configInfor.get(i-1).printValue("\t"+i+": ");
		}	
		System.out.println("\n");
	}
	
	private static void guide() {
		System.out.println("命令说明：");
		System.out.println("\t 序号 value(给序号对应的选项设值)");
		System.out.println("\t save(退出并输出config文件)");
		
		System.out.println("\n\n");
	}
	
	
	
	private static void getConfig(File file) throws IOException {
		InputStreamReader read = new InputStreamReader(new FileInputStream(file), "utf-8");// 考虑到编码格式
		BufferedReader bufferedReader = new BufferedReader(read);
		String lineTxt = null;
		while ((lineTxt = bufferedReader.readLine()) != null) {
			getInput(lineTxt);
		}
		read.close();
	}
	
	private static void getInput(String line) {
		String[] infors = line.split(";");
		if(infors.length!=4) {
			System.out.println("文件内容有错误："+line);
		}
		String type = infors[0];
		String name = infors[1];
		String def = infors[2];
		String mark = infors[3];

		InforItem item = new InforItem(type,name,def,mark);
		configInfor.add(item);
	}
	
 
}

class InforItem {
	private String type;
	private String name;
	private String def_value;
	private String def;
	private String mark;

	public InforItem(String t, String n, String d, String m) {
		type = t;
		name = n;
		mark = m;
		def = d;
		setValue(d);
	}

	public void printValue(String add) {
		String print = add+ name + " == " + def_value;
		System.out.println(print);
	}
	
	public void printMark(String add) {
		String print = add+name+"  (";
		switch (type) {
			case "String":
			case "Boolean":
			case "Int":
				print +=mark;
				break;
			case "Switch":
				String[] options = mark.split("#");
				int i=1;
				for(String option:options) {
					print +=i+"、"+option+"  ";
					i++;
				}
				break;
			default:
				System.out.println("未知类型：" + type);
				System.exit(1);
				break;
		}
		print+=")";
		System.out.println(print);
	}
	
	public String getConfigLine() {
		return type+";"+name+";"+def+";"+mark;
	}

	public String getValue() {
		String result = "";
		switch (type) {
		case "String":
		case "Switch":
			result = name + " = '" + def_value + "'";
			break;
		case "Boolean":
		case "Int":
			result = name + " = " + def_value;
			break;
		default:
			System.out.println("未知类型：" + type);
			System.exit(1);
			break;
		}
		return result;
	}

	//多jar适配
	public String ngetValue(){//todo新增方法
		String result = "";
		switch (type) {
			case "String":
			case "Switch":
			case "Boolean":
			case "Int":
				result = name + " = " + def_value;
				break;
			default:
				System.out.println("未知类型：" + type);
				System.exit(1);
				break;
		}
		return result;
	}

	public InforItem setValue(String value) {
		switch (type) {
		case "String":
			def_value = value;
			break;
		case "Int":
			def_value = "" + Integer.parseInt(value);
			break;
		case "Boolean":
			def_value = "" + Boolean.parseBoolean(value);
			break;
		case "Switch"://todo
			try {
				int index = Integer.parseInt(value);
				String[] options = mark.split("#");
				def_value = options[index-1].split("@")[0];
			}catch(Exception e) {
				def_value = value;
			}
			break;
		default:
			System.out.println("未知类型：" + type);
			System.exit(1);
			break;
		}
		def = value;
		return this;
	}
}
