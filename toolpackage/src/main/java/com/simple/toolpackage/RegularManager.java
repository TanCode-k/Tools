package com.simple.toolpackage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegularManager {

    public static void main(String[] args) {
        String value = "EMAILADDRESS=mysgin@mysgin.com, CN=*.zzkyal.com, OU=mysgin, O=mysgin, L=HangZhou, ST=ZheJiang, C=CN";
        String rule = "CN=*(.*)(\\.*\\s)";
        String rule2 = ".*.";
        getRes(value,rule);
        isResExists(value,rule2);
    }

    public static String getRes(String content,String rule){

        Pattern compile = Pattern.compile(rule);
        Matcher matcher = compile.matcher(content);
        if (matcher.find()){
            String group = matcher.group(0);
            System.out.println("group: "+group);
        }else {
            System.out.println("not found");
        }

        return "";
    }

    public static void isResExists(String content,String rule){
        Pattern compile = Pattern.compile(rule);
        Matcher matcher = compile.matcher(content);
        boolean matches = matcher.find();
        System.out.println(matcher.group(0));
        System.out.println("res: " + matches);

    }

    /**
     * 判断是否为ip请求地址
     * @param url
     * @return
     */
    public static boolean isIP(String url)
    {
        try
        {
            String pattern = "((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})(\\.((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})){3}";

            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(url);
            boolean b = m.find();
            return b;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    /**
     * 获取IP样式的字符串
     * @param url
     * @return
     */
    public static String getIP(String url)
    {
        try
        {
            String pattern = "((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})(\\.((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})){3}";

            Pattern r = Pattern.compile(pattern);
            Matcher m = r.matcher(url);
            return m.group();
        }
        catch (Exception e)
        {
            return "";
        }
    }

}
