package com.easy.tools;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileManager
{
    public static String readString(String path)
    {
        File file = new File(path);

        if (!file.exists())
            return "";
        StringBuilder builder = null;
        try
        {
            try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file)))
            {
                byte[] b = new byte[1024];
                builder = new StringBuilder();
                int flag = 0;
                while ((flag = bufferedInputStream.read(b)) != -1)
                {
                    builder.append(new String(b, 0, flag));
                }

            }
            return builder.toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return "";
    }

    public static boolean writeString(String path, String content)
    {
        return writeString(path, content, false);
    }

    public static boolean writeString(String path, String content, boolean appand)
    {
        if (content == null)
            return false;

        File file = new File(path);
        if (!file.exists())
        {
            try
            {
                if (!file.getParentFile().exists())
                    file.getParentFile().mkdirs();

                file.createNewFile();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        try
        {
            try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file, appand)))
            {
                bufferedOutputStream.write(content.getBytes());
            }
            return true;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
    }
}
