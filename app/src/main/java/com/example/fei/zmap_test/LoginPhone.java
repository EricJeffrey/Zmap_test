package com.example.fei.zmap_test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class LoginPhone extends AppCompatActivity {
    private final static String TAG = "LoginPhone";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_phone);
        getSupportActionBar().hide();
        Log.d(TAG, "onCreate: create login phone class");

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
                        Intent intent = new Intent(LoginPhone.this, LoginAccount.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.login_phone_button:finish();break;
                }
            }
        });
    }
}
