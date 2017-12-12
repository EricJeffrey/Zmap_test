package com.example.fei.zmap_test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fei.zmap_test.db.users;
import com.google.gson.Gson;

public class Profile extends AppCompatActivity {
    public boolean isLogin = false;
    public users current_user;
    public TextView username_textView;
    private static final String TAG = "Profile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_layout);
        getSupportActionBar().hide();

        username_textView = (TextView) findViewById(R.id.login_register_text);
        if(savedInstanceState != null){
            Log.e(TAG,"!=null");
            current_user=new Gson().fromJson(savedInstanceState.getString("current_user"),users.class);
            if (current_user.getId() != 0)  username_textView.setText(current_user.getUsername());  //修改用户名显示
        }else Log.e(TAG,"=null");

        addListener(R.id.back);
        addListener(R.id.login_register_text);
        addListener(R.id.show_me);
        addListener(R.id.profile_setting);

        ProfileColumnLayout tmp =((ProfileColumnLayout) findViewById(R.id.sub_column_mine));
        for(int i = 1; i <= 4; i++) addListenerForSubColumn(tmp, i);            //向“我的”子项添加监听器
        tmp =((ProfileColumnLayout) findViewById(R.id.sub_column_drive_car));
        for(int i = 1; i <= 4; i++) addListenerForSubColumn(tmp, i);            //“驾车”
        tmp =((ProfileColumnLayout) findViewById(R.id.sub_column_others));
        for(int i = 1; i <= 4; i++) addListenerForSubColumn(tmp, i);            //“其他”

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
    @Override
    protected void onResume() {
        super.onResume();

    }
    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("current_user",new Gson().toJson(current_user));
        Log.e(TAG,"onSaveInstanceState");
        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if(resultCode == RESULT_OK){
                    String userJson=data.getStringExtra("resp_user");
                    if(userJson!=null) {
                        current_user = new Gson().fromJson(userJson, users.class);
                        if (current_user.getId() != 0)  username_textView.setText(current_user.getUsername());  //修改用户名显示
                    }
                }
                break;
            default: break;
        }
    }

    //添加监听器
    public void addListener(final int res){
        findViewById(res).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (res){
                    case R.id.back:
                        finish();break;
                    case R.id.login_register_text:
                        login();break;
                    case R.id.show_me:
                        login();break;
                    case R.id.profile_setting:
                        Intent intent = new Intent(Profile.this, SettingActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });
    }
    //向我的，驾车，其他三栏中某一栏的每个子项添加监听器
    public void addListenerForSubColumn(final ProfileColumnLayout tmp, final int id){
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (id) {
                    case 1:
                        Toast.makeText(Profile.this, "你点击了" + tmp.getSub_column_title_text1(), Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(Profile.this, "你点击了" + tmp.getSub_column_title_text2(), Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(Profile.this, "你点击了" + tmp.getSub_column_title_text3(), Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        Toast.makeText(Profile.this, "你点击了" + tmp.getSub_column_title_text4(), Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
        switch (id){
            case 1:
                tmp.getSub_column_1().setOnClickListener(onClickListener);
                break;
            case 2:
                tmp.getSub_column_2().setOnClickListener(onClickListener);
                break;
            case 3:
                tmp.getSub_column_3().setOnClickListener(onClickListener);
                break;
            case 4:
                tmp.getSub_column_4().setOnClickListener(onClickListener);
                break;
        }
    }
    public void login(){
        Intent intent = new Intent(Profile.this, LoginAccount.class);
        startActivityForResult(intent,1);
    }
}
