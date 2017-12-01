package com.example.fei.zmap_test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class Login_account extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_account);
        getSupportActionBar().hide();

        ((TextView)findViewById(R.id.title_text)).setText("账号密码登录");
        TextView go_login_phone = (TextView) findViewById(R.id.go_login_phone_text);
        go_login_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login_account.this, Login_phone.class);
                startActivity(intent);
                finish();
            }
        });
        (findViewById(R.id.find_password)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Login_account.this, "我们会找到你的密码的", Toast.LENGTH_SHORT).show();
            }
        });

        //按钮目前只有返回功能
        Button login_button = (Button) findViewById(R.id.login_account_button);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
