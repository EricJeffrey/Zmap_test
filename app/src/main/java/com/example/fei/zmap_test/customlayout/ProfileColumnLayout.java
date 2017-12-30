package com.example.fei.zmap_test.customlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.fei.zmap_test.R;

/**
 * Created by FEI on 2017/12/3.
 * 个人信息界面的中间栏布局
 */

public class ProfileColumnLayout extends LinearLayout {
    private LinearLayout sub_column_1;
    private LinearLayout sub_column_2;
    private LinearLayout sub_column_3;
    private LinearLayout sub_column_4;

    public ProfileColumnLayout(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        LayoutInflater.from(context).inflate(R.layout.profile_column_layout, this);

        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.ProfileColumnLayout);
        int sub_column_img1 = typedArray.getResourceId(R.styleable.ProfileColumnLayout_sub_column_img1, Color.TRANSPARENT);
        int sub_column_img2 = typedArray.getResourceId(R.styleable.ProfileColumnLayout_sub_column_img2, Color.TRANSPARENT);
        int sub_column_img3 = typedArray.getResourceId(R.styleable.ProfileColumnLayout_sub_column_img3, Color.TRANSPARENT);
        int sub_column_img4 = typedArray.getResourceId(R.styleable.ProfileColumnLayout_sub_column_img4, Color.TRANSPARENT);
        String column_title_text = typedArray.getString(R.styleable.ProfileColumnLayout_column_title_text);
        String sub_column_detail_text1 = typedArray.getString(R.styleable.ProfileColumnLayout_sub_column_detail_text1);
        String sub_column_detail_text2 = typedArray.getString(R.styleable.ProfileColumnLayout_sub_column_detail_text2);
        String sub_column_detail_text3 = typedArray.getString(R.styleable.ProfileColumnLayout_sub_column_detail_text3);
        String sub_column_detail_text4 = typedArray.getString(R.styleable.ProfileColumnLayout_sub_column_detail_text4);
        String sub_column_title_text1 = typedArray.getString(R.styleable.ProfileColumnLayout_sub_column_title_text1);
        String sub_column_title_text2 = typedArray.getString(R.styleable.ProfileColumnLayout_sub_column_title_text2);
        String sub_column_title_text3 = typedArray.getString(R.styleable.ProfileColumnLayout_sub_column_title_text3);
        String sub_column_title_text4 = typedArray.getString(R.styleable.ProfileColumnLayout_sub_column_title_text4);
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
