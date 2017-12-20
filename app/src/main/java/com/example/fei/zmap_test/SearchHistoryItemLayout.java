package com.example.fei.zmap_test;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by FEI on 2017/12/8.
 * item of search history list
 */

public class SearchHistoryItemLayout extends LinearLayout {
    private TextView textView;

    public SearchHistoryItemLayout(Context context, AttributeSet attrs){
        super(context, attrs);
    }
    public SearchHistoryItemLayout(final Context context, AttributeSet attrs, String text){
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.search_history_result_item_layout, this);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SearchHistoryItemLayout);
        String itemText = typedArray.getString(R.styleable.SearchHistoryItemLayout_item_text);
        typedArray.recycle();

        textView = findViewById(R.id.search_history_result_item_text);
        textView.setText(itemText);
        textView.setText(text);

        View view = findViewById(R.id.search_history_item);
        view.setFocusable(true);
        view.setClickable(true);
    }
    public void setOnItemClickListener(View.OnClickListener onClickListener){
        findViewById(R.id.search_history_item).setOnClickListener(onClickListener);
    }
    public String getItemText(){
        return textView.getText().toString();
    }
}
