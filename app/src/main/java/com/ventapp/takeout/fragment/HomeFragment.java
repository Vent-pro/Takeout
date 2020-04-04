package com.ventapp.takeout.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.ventapp.takeout.R;
import com.ventapp.takeout.adapter.ShopAdapter;
import com.ventapp.takeout.gson.LiteAddress;
import com.ventapp.takeout.gson.Location;
import com.ventapp.takeout.gson.Shop;
import com.ventapp.takeout.util.GlideImageLoader;
import com.ventapp.takeout.util.HttpUtility;
import com.ventapp.takeout.util.MyBannerListener;
import com.ventapp.takeout.activity.LocationActivity;
import com.ventapp.takeout.util.Utility;
import com.youth.banner.Banner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;


/**
 * Created by bowenshen on 2020/fruit/17.
 */

public class HomeFragment extends Fragment {
    private final static String TAG="HomeFragment";
    private final static String DEFAULT_ADDRESS_NAME="未知";

    private LiteAddress liteAddress=new LiteAddress();

    @Bind(R.id.text_curr_location)TextView currLocationText;
    @Bind(R.id.banner_ad)Banner banner;
    @Bind(R.id.progress_bar)ProgressBar progressBar;
    @Bind(R.id.recyclerview_shop)RecyclerView recyclerView;
    @Bind(R.id.swipe_refresh)SwipeRefreshLayout swipeRefresh;
    @Bind(R.id.tab_layout)TabLayout tabLayout;

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
        initLayout();
    }

    private void initLayout(){
        liteAddress.setName(DEFAULT_ADDRESS_NAME);
        requestLocate();
        tabLayout.addTab(tabLayout.newTab().setText("推荐美食"));
        tabLayout.addTab(tabLayout.newTab().setText("果蔬生鲜"));
        tabLayout.addTab(tabLayout.newTab().setText("到店自取"));
        swipeRefresh.setColorSchemeResources(R.color.textColorBlue);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout();
                swipeRefresh.setRefreshing(false);
            }
        });
        initBanner();
    }

    private void refreshLayout(){
        requestLocate();
    }

    private void loadLayout(){
        final String poiName=liteAddress.getName();
        getActivity().runOnUiThread(new Thread(){
            @Override
            public void run() {
                currLocationText.setText(poiName);
            }
        });
        initShops();
    }
    private void initShops(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recyclerView.setAdapter(null);
            }
        });
        progressBar.setVisibility(View.VISIBLE);
        double lat=liteAddress.getLocation().getLat();
        double lng=liteAddress.getLocation().getLng();
        String address=Utility.SERVER_URL+"/searchshop?lat="+lat+"&lng="+lng;
        HttpUtility.sendOkHttpRequest(address, new HttpUtility.CallBack() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String msg=response.body().string();
                final List<Shop> shopList=Utility.handleSearchShopResponse(msg);
                HttpUtility.sendCalDistanceRequest(shopList,liteAddress.getLocation().getLat(),liteAddress.getLocation().getLng());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        ShopAdapter shopAdapter=new ShopAdapter(shopList);
                        recyclerView.setAdapter(shopAdapter);
                        Log.d(TAG, "run: recyclerView init finish");
                    }
                });
            }
            @Override
            public void onFailure(Call call, IOException e) {

            }
        });
    }

    private void initBanner(){
        banner.setImageLoader(new GlideImageLoader());
        banner.setDelayTime(2000);
        List images=new ArrayList();
        images.add(R.drawable.ad);
        images.add(R.drawable.ad);
        images.add(R.drawable.ad);
        images.add(R.drawable.ad);
        images.add(R.drawable.ad);
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
                loadLayout();
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
                    loadLayout();
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
    private void startLocationActivity(){
        Intent intent=new Intent(getContext(),LocationActivity.class);
        startActivityForResult(intent,1);
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
}
