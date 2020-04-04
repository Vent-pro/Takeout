package com.ventapp.takeout;

import android.app.Application;
import android.content.Context;

/**
 * Created by bowenshen on 2020/2/23.
 */

public class TakeoutApplication extends Application {


    public final static String SERVER_URI="http://127.0.0.1/";

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
    }

    public static Context getContext(){
        return context;
    }
}
