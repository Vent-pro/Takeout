package com.ventapp.takeout.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ventapp.takeout.R;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by bowenshen on 2020/3/24.
 */

public class SearchLocationHistoryAdapter extends RecyclerView.Adapter<SearchLocationHistoryAdapter.ViewHolder> {

    private List<String> historyList;

    public SearchLocationHistoryAdapter(List<String> historyList) {
        this.historyList=historyList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_location_history,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String poiName=historyList.get(position);
        holder.textView.setText(poiName);
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView=(TextView) itemView.findViewById(R.id.text_search_location_history);
        }
    }
}
