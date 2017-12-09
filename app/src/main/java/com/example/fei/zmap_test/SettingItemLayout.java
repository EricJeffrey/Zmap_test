package com.example.fei.zmap_test;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by FEI on 2017/12/9.
 * 设置界面中的每一个子项的布局
 */

public class SettingItemLayout extends LinearLayout {

    public SettingItemLayout(final Context context, AttributeSet attrs){
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.setting_item_layout, this);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SettingItemLayout);
        String text = typedArray.getString(R.styleable.SettingItemLayout_setting_text);
        String detail = typedArray.getString(R.styleable.SettingItemLayout_setting_detail_text);
        typedArray.recycle();
        Button button = findViewById(R.id.setting_text);
        TextView textView = findViewById(R.id.setting_detail_text);
        button.setText(text);
        textView.setText(detail);
    }
}
