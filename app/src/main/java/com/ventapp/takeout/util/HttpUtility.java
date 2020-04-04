package com.ventapp.takeout.util;

import com.ventapp.takeout.gson.Shop;
import com.ventapp.takeout.gson.User;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

/**
 * Created by bowenshen on 2020/2/25.
 */

public class HttpUtility {
    public static void sendOkHttpRequest(String address,CallBack callBack){
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callBack);
    }
    public static User sendLoginRequest(String address,String phone) throws IOException{
        OkHttpClient client=new OkHttpClient();
        RequestBody requestBody=new FormBody.Builder().add("phone",phone).build();
        Request request=new Request.Builder().post(requestBody).url(address).build();
        String response=client.newCall(request).execute().body().string();
        User user=Utility.handleLoginResponse(response);
        return user;
    }
    public static void sendCalDistanceRequest(List<Shop> shopList,double lat,double lng) throws IOException{
        OkHttpClient client=new OkHttpClient();
        for(int i=0;i<shopList.size();i++){
            Shop shop=shopList.get(i);
            String address="http://api.map.baidu.com/routematrix/v2/riding" +
                    "?output=json" +
                    "&origins=" + lat + "," + lng +
                    "&destinations=" + shop.getLat() + "," + shop.getLng() +
                    "&ak="+Utility.WEB_AK;
            Request request=new Request.Builder().url(address).build();
            String response=client.newCall(request).execute().body().string();
            Utility.handleDistanceResponse(response,shop);
        }
    }

    public interface CallBack extends Callback{
        @Override
        void onResponse(Call call, Response response) throws IOException;

        @Override
        void onFailure(Call call, IOException e);
    }
}
