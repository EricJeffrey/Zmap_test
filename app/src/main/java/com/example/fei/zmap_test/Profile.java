package com.example.fei.zmap_test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fei.zmap_test.db.Users;

import org.litepal.crud.DataSupport;

public class Profile extends AppCompatActivity {
    public Users current_user =null;
    public TextView username_textView;
    public ImageButton user_head_icon_btn;
    private static final String TAG = "Profile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_layout);
        getSupportActionBar().hide();

        username_textView = findViewById(R.id.login_register_text);
        user_head_icon_btn = findViewById(R.id.show_me);



/*
        if(savedInstanceState != null){
            Log.e(TAG,"!=null");
            current_user=new Gson().fromJson(savedInstanceState.getString("current_user"),Users.class);
            if (current_user.getId() != 0)  username_textView.setText(current_user.getUsername());  //修改用户名显示
        }else Log.e(TAG,"=null");
        */

        addListener(R.id.back);
        addListener(R.id.login_register_text);
        addListener(R.id.show_me);
        addListener(R.id.profile_setting);

        ProfileColumnLayout tmp = findViewById(R.id.sub_column_mine);
        for(int i = 1; i <= 4; i++) addListenerForSubColumn(tmp, i);            //向“我的”子项添加监听器
        tmp = findViewById(R.id.sub_column_drive_car);
        for(int i = 1; i <= 4; i++) addListenerForSubColumn(tmp, i);            //“驾车”
        tmp = findViewById(R.id.sub_column_others);
        for(int i = 1; i <= 4; i++) addListenerForSubColumn(tmp, i);            //“其他”

    }

    @Override
    protected void onStart(){
        super.onStart();
        current_user = DataSupport.findLast(Users.class);
        if(current_user !=null){
            if (current_user.getId() != 0) {
                username_textView.setText(current_user.getUsername());  //修改用户名显示
                user_head_icon_btn.setImageResource(AccountProfile.getHeadIconResourceFromId(current_user.getId_head()));
            }
            Log.e(TAG, "onCreate: get user");
        }else {
            username_textView.setText("登录/注册");
            user_head_icon_btn.setImageResource(R.drawable.profile_head);
            Log.e(TAG, "onCreate: lose user");
        }
    }


    @Override
    public void onRestart(){
        super.onRestart();

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
    protected void onStop(){
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    //使用数据库对用户对象进行更新，不需要继续保存用户对象到暂存
/*    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("current_user",new Gson().toJson(current_user));
        Log.e(TAG,"onSaveInstanceState");
        super.onSaveInstanceState(outState);
    }*/





    //添加监听器
    public void addListener(final int res){
        findViewById(res).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (res){
                    case R.id.back:
                        finish();break;
                    case R.id.login_register_text:
                    case R.id.show_me:
                        if(current_user !=null){
                            if (current_user.getId() != 0)  {
                                Intent intent = new Intent(Profile.this, AccountProfile.class);
                                startActivity(intent);
                            }
                        }else {
                            login();
                        }break;
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
        startActivity(intent);
    }
}
