package com.example.admin.mzxing.utils;

import android.content.Context;
import android.content.res.AssetManager;

import com.example.admin.mzxing.bean.Const;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Created by Admin on 2018/4/16.
 */

public class ComUtil {
    private static ComUtil comUtil;

    public synchronized static ComUtil getInstance() {
        if (comUtil == null) {
            comUtil = new ComUtil();
        }
        return comUtil;
    }

    public String getAssetsString(Context context, String fileName) {
        AssetManager assets = context.getAssets();
        StringBuilder builder = null;
        InputStream open = null;
        BufferedReader bufferedReader = null;
        try {
            builder = new StringBuilder();
            open = assets.open(fileName);
            bufferedReader = new BufferedReader(new InputStreamReader(open));
            String lineStrin = "";
            while ((lineStrin = bufferedReader.readLine()) != null) {
                builder.append(lineStrin);
            }
        } catch (IOException e) {
            LogUtils.e("e--" + e.toString());
        } finally {
            try {
                if (open != null) {
                    open.close();
                }
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return builder.toString();
    }
    public boolean writeToFile(File file, String fileName, Context context){
        AssetManager assets = context.getAssets();
        try {
            InputStream open = assets.open(fileName);
            StringBuilder builder=new StringBuilder();
            BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(open));
            String str="";
            while ((str=bufferedReader.readLine())!=null){
                builder.append(str);
            }
            FileOutputStream fileOutputStream=new FileOutputStream(file);
            fileOutputStream.write(builder.toString().getBytes("utf-8"));
            open.close();
            bufferedReader.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
