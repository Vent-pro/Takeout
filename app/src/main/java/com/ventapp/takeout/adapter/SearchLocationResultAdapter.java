package com.ventapp.takeout.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ventapp.takeout.R;
import com.ventapp.takeout.gson.LiteAddress;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by bowenshen on 2020/3/24.
 */

public class SearchLocationResultAdapter extends RecyclerView.Adapter<SearchLocationResultAdapter.ViewHolder> {

    private List<LiteAddress> liteAddressList;

    public SearchLocationResultAdapter(List<LiteAddress> liteAddressList) {
        this.liteAddressList=liteAddressList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_location_result,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        LiteAddress liteAddress=liteAddressList.get(position);
        holder.poiNameText.setText(liteAddress.getName());
        holder.addressText.setText(liteAddress.getAddress());
        double distance=liteAddress.getDistance();
        String distanceString="";
        if(distance>=1000){
            distance=((double)Math.round(distance/10))/100;
            distanceString=distance+"km";
        }else{
            distanceString=(int)distance+"m";
        }
        holder.distanceText.setText(distanceString);
    }

    @Override
    public int getItemCount() {
        return liteAddressList.size();
    }
    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView poiNameText;
        TextView addressText;
        TextView distanceText;

        public ViewHolder(View itemView) {
            super(itemView);
            poiNameText=itemView.findViewById(R.id.text_poiName);
            addressText=itemView.findViewById(R.id.text_address);
            distanceText=itemView.findViewById(R.id.text_distance);
        }
    }
}

