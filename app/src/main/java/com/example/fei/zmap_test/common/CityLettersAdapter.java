package com.example.fei.zmap_test.common;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.fei.zmap_test.R;

import java.util.ArrayList;

/**
 * Created by FEI on 2018/1/3.
 * 城市列表右侧字母项
 */

public class CityLettersAdapter extends RecyclerView.Adapter<CityLettersAdapter.ViewHolder> {
    private static final String TAG = "CityLettersAdapter";
    private ArrayList<CityLetterItem> letterItems;
    private RecyclerView recyclerView;

    public CityLettersAdapter(ArrayList<CityLetterItem> letterItems, RecyclerView recyclerView){
        this.letterItems = letterItems;
        this.recyclerView = recyclerView;
    }
    static class ViewHolder extends CityListAdapter.ViewHolder{
        Button button;
        public ViewHolder(View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.CityLetterItem_button);
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.city_letter_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CityLetterItem item = letterItems.get(holder.getAdapterPosition());
                int toPos = item.getIndexPos();
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int lastPos = linearLayoutManager.findLastVisibleItemPosition();
                if(lastPos - toPos <= 3)
                    recyclerView.scrollToPosition(toPos + 5);
                else
                    recyclerView.scrollToPosition(toPos);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CityLetterItem item = letterItems.get(position);
        holder.button.setText(item.getLetters());
    }

    @Override
    public int getItemCount() {
        return letterItems.size();
    }


}
