package com.ventapp.takeout.gson;

import android.util.Log;

/**
 * Created by bowenshen on 2020/3/25.
 */

public class DetailedAddress extends LiteAddress{
    private DetailedInfo detail_info;

    public DetailedInfo getDetail_info() {
        return detail_info;
    }

    public void setDetail_info(DetailedInfo detail_info) {
        this.detail_info = detail_info;
    }
}
