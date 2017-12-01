package com.example.fei.zmap_test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class Login_phone extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_phone);
        getSupportActionBar().hide();

        ((TextView)findViewById(R.id.title_text)).setText("手机号登录");
        TextView go_login_account = (TextView) findViewById(R.id.go_login_account_text);
        go_login_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login_phone.this, Login_account.class);
                startActivity(intent);
                finish();
            }
        });

        //按钮目前只有返回功能
        Button login_button = (Button) findViewById(R.id.login_phone_button);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
