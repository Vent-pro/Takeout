package com.ventapp.takeout.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ventapp.takeout.gson.DetailedAddress;
import com.ventapp.takeout.gson.LiteAddress;
import com.ventapp.takeout.gson.Shop;
import com.ventapp.takeout.gson.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RunnableFuture;

import cn.smssdk.gui.layout.ProgressDialogLayout;

import static android.view.View.GONE;

/**
 * Created by bowenshen on 2020/fruit/23.
 */

public class Utility {

    public final static String WEB_AK="NjgQKeDynZObQxNEYbu1Oh0SnEetDzBo";
    public final static String SERVER_URL="http://192.168.1.9";
    public final static String POLICY_URL="https://wap.cmpassport.com/resources/html/contract.html";

    private static ProgressDialog curcProgressDialog=null;

    public static int getStatusBarLightMode(Window window) {
        int result = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                result = 3;
            } else {//5.0

            }
        }
        return result;
    }



    public static void handleDistanceResponse(String response,Shop shop){
        try{
            JSONObject jsonObject=new JSONObject(response);
            JSONArray jsonArray=jsonObject.getJSONArray("result");
            double distance=jsonArray.getJSONObject(0).getJSONObject("distance").getDouble("value");
            shop.setDistance(distance);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    public static List<LiteAddress> handleSearchLocationResponse(String response){
        List<LiteAddress> addressList=new ArrayList<LiteAddress>();
        try{
            JSONObject jsonObject=new JSONObject(response);
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

    public static User handleLoginResponse(String response){
        User user=null;
        try{
            JSONObject jsonObject=new JSONObject(response);
            String token=jsonObject.getString("token");
            TokenUtility.setToken(token);
            Gson gson=new Gson();
            user=gson.fromJson(jsonObject.getString("user"),User.class);
            return user;
        }catch(JSONException e){
            e.printStackTrace();
        }
        return user;
    }

    public static List<Shop> handleSearchShopResponse(String response){
        List<Shop> shopList=null;
        try{
            JSONArray jsonArray=new JSONArray(response);
            Gson gson=new Gson();
            shopList=gson.fromJson(jsonArray.toString(),new TypeToken<List<Shop>>(){}.getType());
        }catch (JSONException e){
            e.printStackTrace();
        }
        return shopList;
    }


    public static void showAlertDialogOnUiThread(final Activity activity, final String title, final String msg){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder dialog=new AlertDialog.Builder(activity);
                dialog.setTitle(title);
                dialog.setMessage(msg);
                dialog.setCancelable(true);
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                dialog.show();
            }
        });

    }

    public static void showProgressDialog(Activity activity,String title,String msg){
        curcProgressDialog=new ProgressDialog(activity);
        curcProgressDialog.setTitle(title);
        curcProgressDialog.setMessage(msg);
        curcProgressDialog.setCancelable(false);
        curcProgressDialog.show();
    }

    public static void dismissProgressDialog(){
        if(curcProgressDialog!=null){
            curcProgressDialog.dismiss();
            curcProgressDialog=null;
        }
    }

    public static String getDistanceString(double distance){
        String distanceString;
        if(distance>=1000){
            distance=((double)Math.round(distance/10))/100;
            distanceString=distance+"km";
        }else{
            distanceString=(int)distance+"m";
        }
        return distanceString;
    }
}
