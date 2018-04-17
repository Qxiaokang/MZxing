package com.example.admin.mzxing.utils;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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
}
