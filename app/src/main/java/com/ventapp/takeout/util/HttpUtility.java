package com.ventapp.takeout.util;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by bowenshen on 2020/2/25.
 */

public class HttpUtility {
    public static void sendOkHttpRequest(String address,CallBack callBack){
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callBack);
    }
    public interface CallBack extends Callback{
        @Override
        void onResponse(Call call, Response response) throws IOException;

        @Override
        void onFailure(Call call, IOException e);
    }
}
