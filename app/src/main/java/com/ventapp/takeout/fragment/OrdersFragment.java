package com.ventapp.takeout.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ventapp.takeout.R;

import butterknife.ButterKnife;

/**
 * Created by bowenshen on 2020/3/17.
 */

public class OrdersFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_orders,container,false);
        ButterKnife.bind(this,view);
        return view;
    }
}
