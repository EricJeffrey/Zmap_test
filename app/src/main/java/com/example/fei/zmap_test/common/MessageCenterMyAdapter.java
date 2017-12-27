package com.example.fei.zmap_test.common;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import com.example.fei.zmap_test.ArticleActivity;
import com.example.fei.zmap_test.R;

/**
 * Created by FEI on 2017/12/24.
 * 消息中心的ListView的Adapter
 */

public class MessageCenterMyAdapter extends RecyclerView.Adapter<MessageCenterMyAdapter.ViewHolder> {
    private ArrayList<String> title;
    private ArrayList<String> href;
    private static final String TAG = "MessageCenterMyAdapter";
    public MessageCenterMyAdapter(ArrayList<String> title, ArrayList<String> href){
        this.title = title;
        this.href = href;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.messages_item_layout, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, ArticleActivity.class);
                int pos = holder.getAdapterPosition();
                intent.putExtra("href", href.get(pos));
                intent.putExtra("title", title.get(pos));
                context.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView.setText(title.get(position));
    }

    @Override
    public int getItemCount() {
        return title.size();
    }
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        ViewHolder(View view){
            super(view);
            textView = view.findViewById(R.id.MessageItem_text_view);
        }
    }
}
