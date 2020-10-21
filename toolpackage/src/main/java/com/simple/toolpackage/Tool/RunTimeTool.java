package com.simple.toolpackage.Tool;

import com.simple.toolpackage.NetTools.NetManager;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RunTimeTool {

    public static void main(String[] args) {
        String ipconfig = execCmd("adb shell ifconfig wlan0");
        String ip = NetManager.getIP();
        System.out.println("test: " + ip);
        String port = "5555";
        execCmd("adb tcpip "+port);
        execCmd("adb connect "+ ip + ":" + port);
    }

    private static final String TAG = "SDK";

    /**
     * 执行命令并且输出结果
     */
    public static String execCmd(String cmd) {
        return execCmd(cmd, null);
    }

    public static String execCmd(String cmd, String key) {
        try {
            Process p = Runtime.getRuntime().exec(cmd);
            InputStream in;

            try (DataOutputStream dos = new DataOutputStream(p.getOutputStream())) {
                try (BufferedReader dis = new BufferedReader(new InputStreamReader(p.getInputStream(), "GBK"))) {
                    dos.writeBytes(cmd + "\n");
                    dos.flush();
                    dos.writeBytes("exit\n");
                    dos.flush();

                    String line;
                    StringBuilder result = new StringBuilder();
                    while ((line = dis.readLine()) != null) {
                        if (key != null) {
                            if (line.contains(key))
                                result.append(line + "\n");
                        } else {
                            result.append(line + "\n");
                        }
                    }
                    int i = p.waitFor();
                    return result.toString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
