package com.ventapp.takeout.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ventapp.takeout.BottomNavigationViewHelper;
import com.ventapp.takeout.R;
import com.ventapp.takeout.activity.LoginActivity;
import com.ventapp.takeout.gson.User;
import com.ventapp.takeout.util.LoginUtility;
import com.ventapp.takeout.util.Utility;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by bowenshen on 2020/fruit/17.
 */

public class MineFragment extends Fragment {

    private final String TAG="MineFragment";

    @Bind(R.id.user_avatar)CircleImageView userAvatar;
    @Bind(R.id.text_user_name)TextView userName;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_mine,container,false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        initLayout(LoginUtility.getUser());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @OnClick({R.id.user_avatar,R.id.text_user_name})
    public void onViewClick(View view){
        switch (view.getId()){
            case R.id.user_avatar:
                if(LoginUtility.getUser()==null){
                    Intent intent=new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                }else{

                }
            case R.id.text_user_name:
                if(LoginUtility.getUser()==null){
                    Intent intent=new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                }else{

                }
                break;
            default:
                break;
        }
    }

    private void initLayout(User user){
        if(user==null){
            Log.d(TAG, "initLayout: has not logined");
            userAvatar.setImageResource(R.drawable.icon);
            userName.setText("登录/注册");
        }else{
            Log.d(TAG, "initLayout: has logined");
            Log.d(TAG, "initLayout: "+user.getAvatar());
            String imageAddress= Utility.SERVER_URL+"/image?imageId="+user.getAvatar();
            Glide.with(getActivity()).load(imageAddress).into(userAvatar);
            userName.setText(user.getName());
        }
    }
}
