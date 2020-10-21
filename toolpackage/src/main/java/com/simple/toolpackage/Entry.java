package com.simple.toolpackage;

import com.simple.toolpackage.NetTools.NetManager;
import com.simple.toolpackage.Tool.FileManager;

public class Entry {

    public static void main(String[] args) {

        String type = args[0];

//        String type = "ip";
//        String oriPath = "E:\\Test\\iptest.txt";
//        String goalPath = "E:\\Test\\iptestgoal.txt";

        switch (type) {
            case "ip":
                doIPSelect();
                break;
            case "pkgins":
                doPackageInstall();
                break;
        }
    }

    private static void doPackageInstall() {

    }

    private static void doIPSelect() {
        String[] split = NetManager.devices().split("\n");
        if (split.length > 2){
            System.out.println("设备已连接数大于1，停止连接，并中断原有连接");
            for (int i = 1; i < split.length; i++) {
                String s = split[i];
                s= s.replace("device","").replaceAll(" ","");
                if (RegularManager.isIP(s)){
                    NetManager.disconnect(s);
                    System.out.println(s);
                }
            }
            return;
        }


        String ip = NetManager.getIP();
        NetManager.tcpip("5555");
        try {
            NetManager.connect(ip,"5555");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
