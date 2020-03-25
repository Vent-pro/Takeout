package com.ventapp.takeout.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by bowenshen on 2020/3/17.
 */

public class FragAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragList;

    public FragAdapter(FragmentManager fm,List<Fragment> fragList) {
        super(fm);
        this.fragList=fragList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragList.get(position);
    }

    @Override
    public int getCount() {
        return fragList.size();
    }

}
