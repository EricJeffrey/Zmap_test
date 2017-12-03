package com.example.fei.zmap_test;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.media.Image;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by FEI on 2017/12/3.
 * Three column at the bottom of profile
 */

public class ProfileColumnLayout extends LinearLayout {
    private  int sub_column_img4;
    private  int sub_column_img1;
    private  int sub_column_img2;
    private int sub_column_img3;
    private String column_title_text;
    private String sub_column_detail_text1;
    private String sub_column_detail_text2;
    private String sub_column_detail_text3;
    private String sub_column_detail_text4;
    private String sub_column_title_text1;
    private String sub_column_title_text2;
    private String sub_column_title_text3;
    private String sub_column_title_text4;
    private LinearLayout sub_column_1;
    private LinearLayout sub_column_2;
    private LinearLayout sub_column_3;
    private LinearLayout sub_column_4;

    public ProfileColumnLayout(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        LayoutInflater.from(context).inflate(R.layout.profile_column_layout, this);

        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.ProfileColumnLayout);
        sub_column_img1 = typedArray.getResourceId(R.styleable.ProfileColumnLayout_sub_column_img1, Color.TRANSPARENT);
        sub_column_img2 = typedArray.getResourceId(R.styleable.ProfileColumnLayout_sub_column_img2, Color.TRANSPARENT);
        sub_column_img3 = typedArray.getResourceId(R.styleable.ProfileColumnLayout_sub_column_img3, Color.TRANSPARENT);
        sub_column_img4 = typedArray.getResourceId(R.styleable.ProfileColumnLayout_sub_column_img4, Color.TRANSPARENT);
        column_title_text = typedArray.getString(R.styleable.ProfileColumnLayout_column_title_text);
        sub_column_detail_text1 = typedArray.getString(R.styleable.ProfileColumnLayout_sub_column_detail_text1);
        sub_column_detail_text2 = typedArray.getString(R.styleable.ProfileColumnLayout_sub_column_detail_text2);
        sub_column_detail_text3 = typedArray.getString(R.styleable.ProfileColumnLayout_sub_column_detail_text3);
        sub_column_detail_text4 = typedArray.getString(R.styleable.ProfileColumnLayout_sub_column_detail_text4);
        sub_column_title_text1 = typedArray.getString(R.styleable.ProfileColumnLayout_sub_column_title_text1);
        sub_column_title_text2 = typedArray.getString(R.styleable.ProfileColumnLayout_sub_column_title_text2);
        sub_column_title_text3 = typedArray.getString(R.styleable.ProfileColumnLayout_sub_column_title_text3);
        sub_column_title_text4 = typedArray.getString(R.styleable.ProfileColumnLayout_sub_column_title_text4);
        typedArray.recycle();
        sub_column_1 = findViewById(R.id.sub_column_1);
        sub_column_2 = findViewById(R.id.sub_column_2);
        sub_column_3 = findViewById(R.id.sub_column_3);
        sub_column_4 = findViewById(R.id.sub_column_4);
        if(TextUtils.isEmpty(sub_column_title_text4)) {
            findViewById(R.id.sub_column_3_4_div).setVisibility(GONE);
            findViewById(R.id.sub_column_4).setVisibility(GONE);
        }
        ((TextView) findViewById(R.id.column_title_text)).setText(column_title_text);
        ((TextView) findViewById(R.id.title_text1)).setText(sub_column_title_text1);
        ((TextView) findViewById(R.id.title_text2)).setText(sub_column_title_text2);
        ((TextView) findViewById(R.id.title_text3)).setText(sub_column_title_text3);
        ((TextView) findViewById(R.id.title_text4)).setText(sub_column_title_text4);
        ((TextView) findViewById(R.id.detail_text1)).setText(sub_column_detail_text1);
        ((TextView) findViewById(R.id.detail_text2)).setText(sub_column_detail_text2);
        ((TextView) findViewById(R.id.detail_text3)).setText(sub_column_detail_text3);
        ((TextView) findViewById(R.id.detail_text4)).setText(sub_column_detail_text4);
        ((ImageView) findViewById(R.id.img1)).setImageResource(sub_column_img1);
        ((ImageView) findViewById(R.id.img2)).setImageResource(sub_column_img2);
        ((ImageView) findViewById(R.id.img3)).setImageResource(sub_column_img3);
        ((ImageView) findViewById(R.id.img4)).setImageResource(sub_column_img4);
    }
    //添加监听器
    public void setOnClickListener(int sub_column_id, OnClickListener onClickListener){
        switch (sub_column_id){
            case 1: findViewById(R.id.sub_column_1).setOnClickListener(onClickListener);
                break;
            case 2: findViewById(R.id.sub_column_2).setOnClickListener(onClickListener);
                break;
            case 3: findViewById(R.id.sub_column_3).setOnClickListener(onClickListener);
                break;
            case 4: findViewById(R.id.sub_column_4).setOnClickListener(onClickListener);
                break;
        }
    }
    public int getSub_column_img1() {
        return sub_column_img1;
    }

    public int getSub_column_img2() {
        return sub_column_img2;
    }

    public int getSub_column_img3() {
        return sub_column_img3;
    }

    public int getSub_column_img4() {
        return sub_column_img4;
    }

    public String getColumn_title_text() {
        return column_title_text;
    }

    public String getSub_column_detail_text1() {
        return sub_column_detail_text1;
    }

    public String getSub_column_detail_text2() {
        return sub_column_detail_text2;
    }

    public String getSub_column_detail_text3() {
        return sub_column_detail_text3;
    }

    public String getSub_column_detail_text4() {
        return sub_column_detail_text4;
    }

    public String getSub_column_title_text1() {
        return sub_column_title_text1;
    }

    public String getSub_column_title_text2() {
        return sub_column_title_text2;
    }

    public String getSub_column_title_text3() {
        return sub_column_title_text3;
    }

    public String getSub_column_title_text4() {
        return sub_column_title_text4;
    }

    public LinearLayout getSub_column_1() {
        return sub_column_1;
    }

    public LinearLayout getSub_column_2() {
        return sub_column_2;
    }

    public LinearLayout getSub_column_3() {
        return sub_column_3;
    }

    public LinearLayout getSub_column_4() {
        return sub_column_4;
    }
}
