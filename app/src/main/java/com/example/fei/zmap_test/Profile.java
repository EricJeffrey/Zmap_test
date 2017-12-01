package com.example.fei.zmap_test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toolbar;

public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().hide();

        addListener(R.id.back);
        addListener(R.id.login_register_text);
        addListener(R.id.show_me);



    }
    public void login(){
        Intent intent = new Intent(Profile.this, Login_phone.class);
        startActivity(intent);
    }
    public void addListener(final int res){
        findViewById(res).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (res){
                    case R.id.back:finish();break;
                    case R.id.login_register_text:login();break;
                    case R.id.show_me:login();break;
                }
            }
        });
    }
}
