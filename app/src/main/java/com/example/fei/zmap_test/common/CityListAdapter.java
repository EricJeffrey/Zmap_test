package com.example.fei.zmap_test.common;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fei.zmap_test.R;

import java.util.ArrayList;

/**
 * Created by FEI on 2018/1/3.
 * 城市选择列表RecyclerView适配器
 */

public class CityListAdapter extends RecyclerView.Adapter<CityListAdapter.ViewHolder> {
    private ArrayList<CityListItem> cityListItems;

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView titleView;
        TextView cityNameView;
        public ViewHolder(View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.CityItem_title_view);
            cityNameView = itemView.findViewById(R.id.CityItem_city_name_view);
        }
    }
    public CityListAdapter(ArrayList<CityListItem> cityListItems){
        this.cityListItems = cityListItems;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CityListItem item = cityListItems.get(position);
        if(item.isShowTitle()) {
            holder.titleView.setText(item.getTitle());
            holder.titleView.setVisibility(View.VISIBLE);
            holder.cityNameView.setVisibility(View.GONE);
        }
        else {
            holder.cityNameView.setText(item.getTitle());
            holder.cityNameView.setVisibility(View.VISIBLE);
            holder.titleView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return cityListItems.size();
    }
}
