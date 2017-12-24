package com.example.fei.zmap_test.customlayout;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.fei.zmap_test.R;

import java.util.ArrayList;

/**
 * Created by FEI on 2017/12/24.
 * 消息中心listView适配器
 */

public class MessageCenterAdapter extends ArrayAdapter<String> {
    private int resourceId;

    public MessageCenterAdapter(Context context, int resourceId, ArrayList<String> list){
        super(context, resourceId, list);
        this.resourceId = resourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String data = getItem(position);
        View view;
        ViewHolder viewHolder;
        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.textView = view.findViewById(R.id.MessageItem_text_view);
            view.setTag(viewHolder);
        }
        else{
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }
        viewHolder.textView.setText(data);
        return view;
    }
    class ViewHolder{
        TextView textView;
    }
}
