package com.example.fei.zmap_test;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by FEI on 2017/12/20.
 * 搜索结果的listView的Adapter
 */

public class SearchResultListAdapter extends ArrayAdapter<SearchResultItem> {
    private int resourceId;

    public SearchResultListAdapter(Context context, int resourceId, List<SearchResultItem> objects){
        super(context, resourceId, objects);
        this.resourceId = resourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        SearchResultItem item = getItem(position);
        View view;
        ViewHolder viewHolder;
        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            view.findViewById(R.id.search_history_item).setFocusable(false);
            viewHolder = new ViewHolder();
            viewHolder.result_text = view.findViewById(R.id.search_history_result_item_text);
            view.setTag(viewHolder);
        }
        else{
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        if(item != null) viewHolder.result_text.setText(item.getItemDetail());
        else viewHolder.result_text.setText("");
        return view;

    }
    class ViewHolder {
        TextView result_text;
    }
}
