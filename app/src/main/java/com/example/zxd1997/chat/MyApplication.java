package com.example.zxd1997.chat;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {
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
