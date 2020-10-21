package com.simple.toolpackage.NetTools;

import com.simple.toolpackage.RegularManager;
import com.simple.toolpackage.Tool.FileManager;
import com.simple.toolpackage.Tool.RunTimeTool;

public class NetManager {

    public static void main(String[] args) {
        String ip = devices();
        System.out.println(ip);
    }

    public static void disconnect(String url){
        cmd("adb disconnect "+url);
    }
    public static String devices(){
        String device = cmd("adb devices");

        return device;
    }
    /**
     * 获取IP地址
     *
     * @param path
     * @return
     */
    public static String getIP() {
        String content = cmd("adb shell ifconfig wlan0");//获取网络信息
        String[] items = content.split(" ");
        String result = "";
        for (String s2 : items) {
            if (s2.contains("addr:")) {
                String[] split = s2.split(":");
                if (split.length > 1) {
                    result = split[1];
                }
            }
        }
        return result;
    }

    /**
     * tcpip 端口号设置
     *
     * @param port
     */
    public static void tcpip(String port) {
        if (port == null) {
            port = "5555";
        }

        cmd("adb tcpip " + port);
    }

    /**
     * 远程连接
     *
     * @param ip
     * @param port
     * @throws Exception
     */
    public static void connect(String ip, String port) throws Exception {
        if (ip == null || !RegularManager.isIP(ip)) {
            throw new Exception("ip 为空或ip格式不正确" + ip);
        }

        cmd("adb connect " + ip + ":" + port);

        System.out.println("连接成功 ->"+ip + ":" + port);
    }

    private static String cmd(String cmdline) {
        return RunTimeTool.execCmd(cmdline);
    }
}
