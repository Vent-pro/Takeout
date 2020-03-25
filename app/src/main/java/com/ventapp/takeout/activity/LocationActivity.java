package com.ventapp.takeout.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.location.PoiRegion;
import com.ventapp.takeout.R;
import com.ventapp.takeout.gson.LiteAddress;
import com.ventapp.takeout.gson.Location;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by bowenshen on 2020/3/19.
 */

public class LocationActivity extends AppCompatActivity{

    private static String TAG="LocationActivity";

    private String currCity;
    private String currCityCode;
    private String currPoiName;
    private String currStreet;
    private String currLocation;

    private LiteAddress liteAddress=new LiteAddress();

    @Bind(R.id.text_curr_city)TextView currCityText;
    @Bind(R.id.text_curr_location) TextView currLocationText;

    public LocationClient mLocationClient=null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        ButterKnife.bind(this);
        requestLocation();
    }
    private void requestLocation(){
        currLocationText.setText("定位中...");
        if(mLocationClient!=null){
            mLocationClient.start();
        }else{
            mLocationClient=new LocationClient(getApplicationContext());
            mLocationClient.registerLocationListener(new BDAbstractLocationListener() {
                @Override
                public void onReceiveLocation(BDLocation bdLocation) {
                    Log.d(TAG,"latitude: "+bdLocation.getLatitude());
                    Log.d(TAG,"longtitude: "+bdLocation.getLongitude());
                    Log.d(TAG,"coorType: "+bdLocation.getCoorType());
                    Log.d(TAG,"errorCode: "+bdLocation.getLocType());
                    Log.d(TAG, "fullAddress: "+bdLocation.getAddrStr());
                    Log.d(TAG, "district: "+bdLocation.getDistrict());
                    Log.d(TAG, "street: "+bdLocation.getStreet());
                    Log.d(TAG, "adcode: "+bdLocation.getAdCode());
                    Log.d(TAG, "town: "+bdLocation.getTown());
                    Log.d(TAG, "locationDescribe: "+bdLocation.getLocationDescribe());
                    Poi poi=bdLocation.getPoiList().get(0);
                    Log.d(TAG, "poiName: "+poi.getName());
                    Log.d(TAG, "poiTags: "+poi.getTags());
                    Log.d(TAG, "poiAddr: "+poi.getAddr());
                    PoiRegion poiRegion=bdLocation.getPoiRegion();
                    Log.d(TAG, "poiDerectionDesc: "+poiRegion.getDerectionDesc());
                    Log.d(TAG, "poiRegionName: "+poiRegion.getName());
                    Log.d(TAG, "poiTags: "+poiRegion.getTags());
                    currPoiName=poi.getName();
                    currStreet=bdLocation.getStreet();
                    currCity=bdLocation.getCity();
                    currCityCode=bdLocation.getCityCode();
                    currLocation=currPoiName+"("+currStreet+")";
                    liteAddress.setName(currPoiName);
                    liteAddress.setCity(currCity);
                    liteAddress.setLocation(new Location(bdLocation.getLatitude(),bdLocation.getLongitude()));
                    runOnUiThread(new Thread(){
                        @Override
                        public void run() {
                            currCityText.setText(currCity);
                            currLocationText.setText(currLocation);
                        }
                    });
                    mLocationClient.stop();
                }
            });
            LocationClientOption option=new LocationClientOption();
            option.setCoorType("bd0911");
            option.setOpenGps(true);
            option.setIsNeedAddress(true);
            option.setIsNeedLocationDescribe(true);
            option.setIsNeedLocationPoiList(true);
            option.setNeedNewVersionRgc(true);
            mLocationClient.setLocOption(option);
            mLocationClient.start();
        }
    }


    @OnClick({R.id.button_set_address,R.id.button_relocate,R.id.button_back,R.id.button_search_location,R.id.text_curr_location})
    public void onViewClick(View view){
        switch (view.getId()){
            case R.id.button_set_address:
                Log.d(TAG, "onViewClick: set address");
                break;
            case R.id.button_relocate:
                Log.d(TAG, "onViewClick: relocate");
                requestLocation();
                break;
            case R.id.button_back:
                Log.d(TAG, "onViewClick: back");
                finish();
                break;
            case R.id.button_search_location:
                Log.d(TAG, "onViewClick: search address");
                SearchLocationActivity.actionStart(this,liteAddress);
                break;
            case R.id.text_curr_location:
                returnData(liteAddress);
                break;
            default:
                break;
        }
    }
    private void returnData(LiteAddress liteAddress){
        Intent intent=new Intent();
        Bundle bundle=new Bundle();
        bundle.putParcelable("address_data",liteAddress);
        intent.putExtra("return_data",bundle);
        setResult(RESULT_OK,intent);
        finish();
    }
}
