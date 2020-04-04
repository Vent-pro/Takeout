package com.ventapp.takeout.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.ventapp.takeout.R;

import butterknife.ButterKnife;

/**
 * Created by bowenshen on 2020/fruit/17.
 */

public class SearchActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
    }
}
