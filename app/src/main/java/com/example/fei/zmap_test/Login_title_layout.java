package com.example.fei.zmap_test;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by FEI on 2017/12/1.
 */

public class Login_title_layout extends LinearLayout {

    public Login_title_layout(Context context, AttributeSet attrs){
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.title_layout, this);
        findViewById(R.id.back).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Activity) getContext()).finish();
            }
        });
        TextView textView =  findViewById(R.id.go_register);
        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context1 = getContext();
                Intent intent = new Intent(context1, Register.class);
                context1.startActivity(intent);
                ((Activity) context1).finish();
            }
        });
    }
}
