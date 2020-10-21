package com.easy.android.tools;
import java.io.IOException;

public class Main {

	public static void main(String[] args) {
		switch (args[0]) {
			case "buildpath":
				path(args);
				break;
            case "choosetype":
                choose(args);
                break;
			case "buildconfig":
				config(args);
				break;
			default:
				System.out.println("错误类型：" + args[0]);
				break;
		}
	}
	
	private static void path(String ... args) {
		if (args.length == 3) {
			try {
				Path.changPath(args[1],args[2]);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("参数个数不对！main(String[] args):" + args.length);
			for (String arg : args) {
				System.out.println(arg);
			}
			System.exit(1);
		}
	}

    private static void choose(String ... args) {
        int length = args.length;
        if (length >= 4) {

            String add = "";
            String end = "";
            if(length >= 5)
                add = args[4];
            if (length >= 6)
                end = args[5];
            try {
                ChooseType.chooseType(args[1],args[2],args[3], add, end);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("参数个数不对！main(String[] args):" + length);
            for (String arg : args) {
                System.out.println(arg);
            }
            System.exit(1);
        }
    }

	private static void config(String ... args) {
		if (args.length == 3) {
			try {
				Config.config(args[1],args[2]);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("参数个数不对！main(String[] args):" + args.length);
			for (String arg : args) {
				System.out.println(arg);
			}
			System.exit(1);
		}
	}

}
