package com.example.fei.zmap_test.customLayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.fei.zmap_test.R;

/**
 * Created by FEI on 2017/12/3.
 * ProfileActivity middle four column
 */

public class ProfileMidColumn extends LinearLayout{

    public ProfileMidColumn(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        LayoutInflater.from(context).inflate(R.layout.profile_mid_column_layout, this);
        TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.ProfileMidColumn);
        String column_text = typedArray.getString(R.styleable.ProfileMidColumn_column_text);
        int img = typedArray.getResourceId(R.styleable.ProfileMidColumn_column_img, Color.TRANSPARENT);
        typedArray.recycle();
        ((TextView) findViewById(R.id.profile_mid_column_text)).setText(column_text);
        ((ImageView) findViewById(R.id.profile_mid_column_img)).setImageResource(img);
    }
}
