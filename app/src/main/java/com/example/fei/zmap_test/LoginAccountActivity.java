package com.example.fei.zmap_test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.fei.zmap_test.http.HTTPCallback;
import com.example.fei.zmap_test.http.HTTPRequest;
import com.example.fei.zmap_test.db.Users;

public class LoginAccountActivity extends AppCompatActivity implements HTTPCallback {
    private String url;
    private EditText username;
    private EditText password;//用户名和密码
    private String username_text;
    private String password_text;
    public static final int SHOW_RESPONSE = 0;
    public Users resp_user;
    private static final String TAG = "LoginAccountActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_account_layout);
        getSupportActionBar().hide();
        url = getString(R.string.URl); //服务器接口地址

        username = (EditText) findViewById(R.id.login_account_username);
        password = (EditText) findViewById(R.id.login_account_password);


        addListener(R.id.go_login_phone_text);
        addListener(R.id.find_password);                    //按钮目前只有返回功能
        addListener(R.id.login_account_button);


    }

    public void addListener(final int res) {
        findViewById(res).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (res) {
                    case R.id.go_login_phone_text:
                        Intent intent = new Intent(LoginAccountActivity.this, LoginPhoneActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    case R.id.find_password:
                        Toast.makeText(LoginAccountActivity.this, "别担心", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.login_account_button:
                        username_text = username.getText().toString().trim();
                        password_text = password.getText().toString().trim();
                        Log.e("get ", "+" + username_text + "-" + password_text);
                        HTTPRequest.getOurInstance().login(LoginAccountActivity.this, username_text, password_text, LoginAccountActivity.this);
                        break;
                }
            }
        });
    }

    @Override
    public void onFinish(int status) {
        if (status > 0) {
            Toast.makeText(LoginAccountActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(LoginAccountActivity.this, "账号密码错误", Toast.LENGTH_SHORT).show();
            password.setText("");
        }

    }
}