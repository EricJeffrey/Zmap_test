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
        addListener(R.id.go_login_account_text);

        //按钮目前只有返回功能
        addListener(R.id.login_phone_button);
    }
    public void addListener(final int res){
        findViewById(res).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (res){
                    case R.id.go_login_account_text:
                        Intent intent = new Intent(Login_phone.this, Login_account.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.login_phone_button:finish();break;
                }
            }
        });
    }
}
