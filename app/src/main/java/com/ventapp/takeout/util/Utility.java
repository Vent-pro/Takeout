package com.ventapp.takeout.util;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ventapp.takeout.gson.DetailedAddress;
import com.ventapp.takeout.gson.LiteAddress;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bowenshen on 2020/3/23.
 */

public class Utility {

    public final static String WEB_AK="NjgQKeDynZObQxNEYbu1Oh0SnEetDzBo";

    public static double handleDistanceReponse(String request){
        double distance=-1;
        try{
            JSONObject jsonObject=new JSONObject(request);
            JSONArray jsonArray=jsonObject.getJSONArray("result");
            distance=jsonArray.getJSONObject(0).getJSONObject("distance").getDouble("value");
            distance=((double)Math.round(distance/10))/100;
        }catch (JSONException e){
            e.printStackTrace();
        }
        return distance;
    }

    public static List<LiteAddress> handleSearchLocationReponse(String request){
        List<LiteAddress> addressList=new ArrayList<LiteAddress>();
        try{
            JSONObject jsonObject=new JSONObject(request);
            String jsonData=jsonObject.getString("results");
            Gson gson=new Gson();
            List<DetailedAddress> detailedAddressList=gson.fromJson(jsonData,new TypeToken<List<DetailedAddress>>(){}.getType());
            for(int i=0;i<detailedAddressList.size();i++){
                addressList.add(LiteAddress.parseLiteAddress(detailedAddressList.get(i)));
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return addressList;
    }

}
