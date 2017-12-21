package com.example.fei.zmap_test.customLayout;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.fei.zmap_test.LoginAccountActivity;
import com.example.fei.zmap_test.R;
import com.example.fei.zmap_test.RegisterByUsernameActivity;

/**
 * Created by FEI on 2017/12/1.
 * custom title
 */

public class TitleLayout extends LinearLayout {
    private String title_text;
    private String right_text;
    private int button_img;

    public TitleLayout(Context context, AttributeSet attrs){
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.title_layout, this);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TitleLayout);
        title_text = typedArray.getString(R.styleable.TitleLayout_title_text);
        right_text = typedArray.getString(R.styleable.TitleLayout_right_text);
        button_img = typedArray.getResourceId(R.styleable.TitleLayout_button_src, R.drawable.back_button_blue_pressed_gray);
        typedArray.recycle();

        setSrc();
        addListener(R.id.back);
        addListener(R.id.right_text);
    }
    public void setSrc(){
        ((TextView) findViewById(R.id.title_text)).setText(title_text);
        ((TextView) findViewById(R.id.right_text)).setText(right_text);
        ((ImageButton) findViewById(R.id.back)).setImageResource(button_img);
    }
    public void addListener(final int res){
        findViewById(res).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (res){
                    case R.id.back:
                        ((Activity) getContext()).finish();break;
                    case R.id.right_text:
                        if(!TextUtils.isEmpty(right_text) && right_text.equals("注册")){
                            Context context = getContext();
                            Intent intent = new Intent(context, RegisterByUsernameActivity.class);
                            context.startActivity(intent);
                            ((Activity) context).finish();
                        }
                        else if(right_text.equals("登录")){
                            Context context = getContext();
                            Intent intent = new Intent(context, LoginAccountActivity.class);
                            context.startActivity(intent);
                            ((Activity) context).finish();
                        }
                        break;
                }
            }
        });
    }
}
