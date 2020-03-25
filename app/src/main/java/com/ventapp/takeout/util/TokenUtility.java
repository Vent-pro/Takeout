package com.ventapp.takeout.util;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.ventapp.takeout.TakeoutApplication;

/**
 * Created by bowenshen on 2020/2/25.
 */

public class TokenUtility {
    private static String token;

    static {
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(TakeoutApplication.getContext());
        token=sharedPreferences.getString("token",null);
    }
    public static String getToken(){
        return token;
    }
    public static void setToken(String token){
        TokenUtility.token=token;
        SharedPreferences sharedPreferences=PreferenceManager.getDefaultSharedPreferences(TakeoutApplication.getContext());
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("token",token);
    }
}
