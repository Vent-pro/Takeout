package com.ventapp.takeout.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.ventapp.takeout.R;
import com.ventapp.takeout.gson.LiteAddress;
import com.ventapp.takeout.gson.Location;
import com.ventapp.takeout.util.GlideImageLoader;
import com.ventapp.takeout.util.MyBannerListener;
import com.ventapp.takeout.activity.LocationActivity;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by bowenshen on 2020/3/17.
 */

public class HomeFragment extends Fragment {
    private final static String TAG="HomeFragment";
    private final static String DEFAULT_ADDRESS_NAME="未知";

    private LiteAddress liteAddress=new LiteAddress();

    @Bind(R.id.text_curr_location)TextView currLocationText;
    @Bind(R.id.banner_ad)Banner banner;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_home,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        liteAddress.setName(DEFAULT_ADDRESS_NAME);
        initLayout();
        initBanner();
        requestLocate();
    }

    private void initLayout(){
        final String poiName=liteAddress.getName();

        getActivity().runOnUiThread(new Thread(){
            @Override
            public void run() {
                currLocationText.setText(poiName);
                if(poiName!=DEFAULT_ADDRESS_NAME){

                }else{

                }
            }
        });

    }

    private void initBanner(){
        banner.setImageLoader(new GlideImageLoader());
        banner.setDelayTime(2000);
        List images=new ArrayList();
        images.add(R.drawable.home_down);
        images.add(R.drawable.home_down);
        images.add(R.drawable.home_down);
        images.add(R.drawable.arrow_down);
        images.add(R.drawable.arrow_down);
        banner.setImages(images);
        banner.isAutoPlay(true);
        MyBannerListener mBannerListener=new MyBannerListener();
        banner.setOnBannerListener(mBannerListener);
        banner.start();
    }

    @OnClick({R.id.button_location,R.id.button_message,R.id.button_search})
    public void onViewClick(View view){
        switch (view.getId()){
            case R.id.button_location:
                requestLocationService();
                break;
            case R.id.button_search:
                Log.d(TAG, "onViewClick: search");
                break;
            default:
                break;
        }
    }


    private void requestLocate(){
        List<String> permissionList=new ArrayList<>();
        if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if(!permissionList.isEmpty()){
            String[] permissions=permissionList.toArray(new String[permissionList.size()]);
            requestPermissions(permissions,1);
        }else{
            locate();
        }
    }

    private void locate(){
        final LocationClient mLocationClient=new LocationClient(getActivity().getApplicationContext());
        mLocationClient.registerLocationListener(new BDAbstractLocationListener(){
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                Poi poi=bdLocation.getPoiList().get(0);
                liteAddress.setName(poi.getName());
                liteAddress.setLocation(new Location(bdLocation.getLatitude(),bdLocation.getLongitude()));
                Log.d(TAG, "locationService: start");
                mLocationClient.stop();
                Log.d(TAG, "locationService: stop");
                initLayout();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if(resultCode== Activity.RESULT_OK){
                    Log.d(TAG, "onActivityResult: RESULT_OK");
                    liteAddress=(LiteAddress)data.getBundleExtra("return_data").get("address_data");
                    Log.d(TAG, "onActivityResult: liteAddress: poiName: "+liteAddress.getName());
                    Log.d(TAG, "onActivityResult: liteAddress: lat: "+liteAddress.getLocation().getLat());
                    Log.d(TAG, "onActivityResult: liteAddress: lng "+liteAddress.getLocation().getLng());
                    initLayout();
                }
                break;
            default:
                break;
        }
    }
    
    private void requestLocationService(){
        List<String> permissionList=new ArrayList<>();
        if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if(!permissionList.isEmpty()){
            String[] permissions=permissionList.toArray(new String[permissionList.size()]);
            requestPermissions(permissions,2);
        }else{
            startLocationActivity();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(grantResults.length>0){
            for(int result:grantResults){
                if(result!=PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(getContext(),"未获取相应权限",Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            switch (requestCode){
                case 1:
                    locate();
                    break;
                case 2:
                    startLocationActivity();
                    break;
                default:
                    break;
            }
        }
        else{
            Toast.makeText(getContext(), "出现未知错误", Toast.LENGTH_SHORT).show();
        }

    }
    private void startLocationActivity(){
        Intent intent=new Intent(getContext(),LocationActivity.class);
        startActivityForResult(intent,1);
    }
}
