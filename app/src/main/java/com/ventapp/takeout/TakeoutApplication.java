package com.ventapp.takeout;

import android.app.Application;
import android.content.Context;

/**
 * Created by bowenshen on 2020/2/23.
 */

public class TakeoutApplication extends Application {

    public final static String WEB_AK="NjgQKeDynZObQxNEYbu1Oh0SnEetDzBo";

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
