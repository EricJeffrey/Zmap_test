package com.example.fei.zmap_test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class LoginAccount extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_account);
        getSupportActionBar().hide();

        addListener(R.id.go_login_phone_text);
        addListener(R.id.find_password);
        //按钮目前只有返回功能
        addListener(R.id.login_account_button);
    }
    public void addListener(final int res){
        findViewById(res).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (res){
                    case R.id.go_login_phone_text:
                        Intent intent = new Intent(LoginAccount.this, LoginPhone.class);
                        startActivity(intent);
                        finish();break;
                    case R.id.find_password:
                        Toast.makeText(LoginAccount.this, "别担心", Toast.LENGTH_SHORT).show(); break;
                    case R.id.login_account_button:finish();break;
                }
            }
        });
    }
}
