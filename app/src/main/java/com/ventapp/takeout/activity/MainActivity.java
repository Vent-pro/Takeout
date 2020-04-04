package com.ventapp.takeout.activity;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;
import com.ventapp.takeout.R;
import com.ventapp.takeout.adapter.FragAdapter;
import com.ventapp.takeout.fragment.HomeFragment;
import com.ventapp.takeout.fragment.MineFragment;
import com.ventapp.takeout.fragment.OrdersFragment;
import com.ventapp.takeout.util.Utility;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity{

    private static final String TAG="MainActivity";

    public static Activity activity;

    @Bind(R.id.viewpager_main) ViewPager mainViewPager;

    @Bind(R.id.home_button) LinearLayout homeButton;
    @Bind(R.id.home_image) ImageView homeImage;
    @Bind(R.id.home_text) TextView homeText;
    @Bind(R.id.orders_button) LinearLayout ordersButton;
    @Bind(R.id.orders_image) ImageView ordersImage;
    @Bind(R.id.orders_text) TextView ordersText;
    @Bind(R.id.mine_button) LinearLayout mineButton;
    @Bind(R.id.mine_image) ImageView mineImage;
    @Bind(R.id.mine_text) TextView mineText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        activity=this;
        List<Fragment> fragmentList=new ArrayList<>();
        fragmentList.add(new HomeFragment());
        fragmentList.add(new OrdersFragment());
        fragmentList.add(new MineFragment());
        mainViewPager.setAdapter(new FragAdapter(getSupportFragmentManager(),fragmentList));
        mainViewPager.setOffscreenPageLimit(2);
        mainViewPager.setCurrentItem(0);

        /*
        SDKInitializer.initialize(getApplicationContext());
        Log.d(TAG, "onCreate: distance: "+ DistanceUtil.getDistance(new LatLng(0,30),new LatLng(0,30.05)));
        Log.d(TAG, "onCreate: distance: "+ DistanceUtil.getDistance(new LatLng(0,30),new LatLng(0,30.045)));
        Log.d(TAG, "onCreate: distance: "+ DistanceUtil.getDistance(new LatLng(30,0),new LatLng(30.05,0)));
        Log.d(TAG, "onCreate: distance: "+ DistanceUtil.getDistance(new LatLng(30,0),new LatLng(30.045,0)));
        */
    }


    @OnClick({R.id.home_button,R.id.orders_button,R.id.mine_button})
    public void onViewClick(View view) {
        resetButtons();
        switch (view.getId()){
            case R.id.home_button:
                mainViewPager.setCurrentItem(0);
                break;
            case R.id.orders_button:
                mainViewPager.setCurrentItem(1);
                break;
            case R.id.mine_button:
                mainViewPager.setCurrentItem(2);
                break;
            default:
                break;
        }
    }

    private void resetButtons(){

    }
}
