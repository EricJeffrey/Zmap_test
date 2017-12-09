package com.example.fei.zmap_test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_layout);
        getSupportActionBar().hide();

        ((TextView)findViewById(R.id.title_text)).setText("注册");
        ((TextView)findViewById(R.id.right_text)).setText("");

        //按钮目前只有返回功能
        Button confirm_button = (Button) findViewById(R.id.register_button);
        confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}
