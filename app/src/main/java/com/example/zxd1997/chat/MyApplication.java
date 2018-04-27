package com.example.zxd1997.chat;

import android.content.Context;

import org.litepal.LitePalApplication;

public class MyApplication extends LitePalApplication {
    private static Context context;
    private static String username;
    public static Context getContext() {
        return context;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        MyApplication.username = username;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
}
