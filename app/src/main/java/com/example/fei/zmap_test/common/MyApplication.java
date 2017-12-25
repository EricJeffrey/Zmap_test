package com.example.fei.zmap_test.common;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import org.litepal.LitePal;

/**
 * Created by FEI on 2017/12/25.
 * 通过该类获取全局Context
 */

public class MyApplication extends Application {
    @SuppressLint("StaticFieldLeak")
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        LitePal.initialize(context);
    }
    public static Context getContext(){
        return context;
    }
}
