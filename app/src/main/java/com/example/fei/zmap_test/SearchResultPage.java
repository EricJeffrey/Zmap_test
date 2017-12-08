package com.example.fei.zmap_test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.TextView;

public class SearchResultPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result_layout);

        TextView textView = (TextView) findViewById(R.id.result_text);
        Intent intent = getIntent();
        String text = intent.getExtras().getString("search_text");
        text = "我们找不到" + text;
        if(!TextUtils.isEmpty(text)) textView.setText(text);
    }
}
