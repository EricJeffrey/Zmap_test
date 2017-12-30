package com.example.fei.zmap_test.customlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.fei.zmap_test.R;

/**
 * Created by FEI on 2017/12/8.
 * 搜索历史记录列表的每一项
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

    /**
     * 为其设置监听器
     * @param onClickListener 监听器
     */
    public void setOnItemClickListener(View.OnClickListener onClickListener){
        findViewById(R.id.search_history_item).setOnClickListener(onClickListener);
    }
    public String getItemText(){
        return textView.getText().toString();
    }
}
