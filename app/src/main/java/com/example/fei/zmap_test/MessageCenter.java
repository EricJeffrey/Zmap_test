package com.example.fei.zmap_test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MessageCenter extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_center_layout);
        getSupportActionBar().hide();
    }
}
