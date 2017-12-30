package com.example.fei.zmap_test;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * 关于小组的简介
 */
public class AboutUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us_layout);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) actionBar.hide();
    }
}
