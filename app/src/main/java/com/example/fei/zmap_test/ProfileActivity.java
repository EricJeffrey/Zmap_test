package com.example.fei.zmap_test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fei.zmap_test.customlayout.ProfileColumnLayout;
import com.example.fei.zmap_test.db.Users;

import org.litepal.crud.DataSupport;

public class ProfileActivity extends AppCompatActivity {
    public Users current_user =null;
    public TextView username_textView;
    public ImageButton user_head_icon_btn;
    private static final String TAG = "ProfileActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_layout);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) actionBar.hide();

        username_textView = findViewById(R.id.login_register_text);
        user_head_icon_btn = findViewById(R.id.show_me);

        addListener(R.id.back);
        addListener(R.id.login_register_text);
        addListener(R.id.show_me);
        addListener(R.id.profile_setting);

        ProfileColumnLayout tmp = findViewById(R.id.sub_column_mine);
        for(int i = 1; i <= 4; i++) addListenerForSubColumn(tmp, i);            //向“我的”子项添加监听器
        tmp = findViewById(R.id.sub_column_drive_car);
        for(int i = 1; i <= 4; i++) addListenerForSubColumn(tmp, i);            //向“驾车”子项添加监听器
        tmp = findViewById(R.id.sub_column_others);
        for(int i = 1; i <= 4; i++) addListenerForSubColumn(tmp, i);            //向“其他”子项添加监听器

    }

    @Override
    protected void onStart(){
        super.onStart();
        current_user = DataSupport.findLast(Users.class);
        Log.e(TAG, "onCreate: all user:"+DataSupport.count(Users.class));
//        for(Users users:DataSupport.findAll(Users.class)){
//            Log.e(TAG, "onCreate: every user:"+users.getUsername());
//        }
        if(current_user !=null && current_user.getUser_id() != 0){
            username_textView.setText(current_user.getUsername());  //修改用户名显示
            user_head_icon_btn.setImageResource(AccountProfileActivity.getHeadIconResourceFromId(current_user.getId_head()));
            Log.e(TAG, "onCreate: get user:"+current_user.getUser_id());
        }else {
            username_textView.setText("登录/注册");
            user_head_icon_btn.setImageResource(R.drawable.profile_head);
            Log.e(TAG, "onCreate: lose user");
        }
    }

    //添加监听器
    public void addListener(final int res){
        findViewById(res).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (res){
                    case R.id.back:
                        finish();
                        break;
                    case R.id.login_register_text:
                    case R.id.show_me:
                        if(current_user !=null){
                            if (current_user.getUser_id() != 0)  {
                                Intent intent = new Intent(ProfileActivity.this, AccountProfileActivity.class);
                                startActivity(intent);
                            }else {
                                login();
                            }
                        }else {
                            login();
                        }
                        break;
                    case R.id.profile_setting:
                        Intent intent = new Intent(ProfileActivity.this, SettingActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });
    }

    /**
     * 向我的，驾车，其他三栏中某一栏的每个子项添加监听器
     * @param tmp：我的，驾车，其他三栏中的某一栏
     * @param id：表示某一栏中第几个子项
     */
    public void addListenerForSubColumn(final ProfileColumnLayout tmp, final int id){
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (id) {
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                        Toast.makeText(ProfileActivity.this, "正在全力开发中...", Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(ProfileActivity.this, LoginAccountActivity.class);
        startActivity(intent);
    }
}
