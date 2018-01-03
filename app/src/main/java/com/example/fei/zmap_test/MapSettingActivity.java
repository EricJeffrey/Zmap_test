package com.example.fei.zmap_test;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MapSettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_setting_layout);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) actionBar.hide();
    }
}
