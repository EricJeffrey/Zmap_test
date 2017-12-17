package com.example.fei.zmap_test;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import java.util.ArrayList;

public class AccountProfile extends AppCompatActivity {
    private LinearLayout top_view;
    private LinearLayout bottom_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_profile_layout);
        getSupportActionBar().hide();

        top_view = findViewById(R.id.Account_profile_head_icon_choose_view);
        bottom_view = findViewById(R.id.Account_profile_view);

        addListener(R.id.Account_profile_head_icon_setting);
        addListener(R.id.Account_profile_logout);
        addListener(R.id.Account_profile_user_name_setting);
        addListener(R.id.Account_profile_view);
        addListener(R.id.Account_profile_head_icon_choose_view);
        addListenerForChooseButton();
    }
    public void addListener(final int res){
        findViewById(res).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (res){
                    case R.id.Account_profile_head_icon_setting:
                        topViewAnimShow();
                        break;
                    case R.id.Account_profile_logout:
                        AlertDialog.Builder dialog = new AlertDialog.Builder(AccountProfile.this);
                        dialog.setTitle("退出登录？");
                        dialog.setCancelable(true);
                        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //TODO log out
                                Toast.makeText(AccountProfile.this, "这里应该退出登录", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //TODO cancel
                                Toast.makeText(AccountProfile.this, "这里应该取消退出", Toast.LENGTH_SHORT).show();
                            }
                        });
                        dialog.show();
                        break;
                    case R.id.Account_profile_user_name_setting:
                        Toast.makeText(AccountProfile.this, "正在全力开发中...", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.Account_profile_view:
                        break;
                    case R.id.Account_profile_head_icon_choose_view:
                        topViewAnimDisapper();
                        break;
                    case R.id.Account_profile_head_icon_choose_btn_1:
                        break;
                }
            }
        });
    }
    //为每个头像选择图标设置监听器
    public void addListenerForChooseButton(){
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO reset user head icon and update
                Toast.makeText(AccountProfile.this, "设置你的头像为" + v.getId(), Toast.LENGTH_SHORT).show();
            }
        };
        findViewById(R.id.Account_profile_head_icon_choose_btn_1).setOnClickListener(onClickListener);  //有点过分
        findViewById(R.id.Account_profile_head_icon_choose_btn_2).setOnClickListener(onClickListener);
        findViewById(R.id.Account_profile_head_icon_choose_btn_3).setOnClickListener(onClickListener);
        findViewById(R.id.Account_profile_head_icon_choose_btn_4).setOnClickListener(onClickListener);
        findViewById(R.id.Account_profile_head_icon_choose_btn_5).setOnClickListener(onClickListener);
        findViewById(R.id.Account_profile_head_icon_choose_btn_6).setOnClickListener(onClickListener);
        findViewById(R.id.Account_profile_head_icon_choose_btn_7).setOnClickListener(onClickListener);
        findViewById(R.id.Account_profile_head_icon_choose_btn_8).setOnClickListener(onClickListener);
        findViewById(R.id.Account_profile_head_icon_choose_btn_9).setOnClickListener(onClickListener);
        findViewById(R.id.Account_profile_head_icon_choose_btn_10).setOnClickListener(onClickListener);
        findViewById(R.id.Account_profile_head_icon_choose_btn_11).setOnClickListener(onClickListener);
        findViewById(R.id.Account_profile_head_icon_choose_btn_12).setOnClickListener(onClickListener);
        findViewById(R.id.Account_profile_head_icon_choose_btn_13).setOnClickListener(onClickListener);
        findViewById(R.id.Account_profile_head_icon_choose_btn_14).setOnClickListener(onClickListener);
        findViewById(R.id.Account_profile_head_icon_choose_btn_15).setOnClickListener(onClickListener);
        findViewById(R.id.Account_profile_head_icon_choose_btn_16).setOnClickListener(onClickListener);
        findViewById(R.id.Account_profile_head_icon_choose_btn_17).setOnClickListener(onClickListener);
        findViewById(R.id.Account_profile_head_icon_choose_btn_18).setOnClickListener(onClickListener);
    }

    @Override
    public void onBackPressed() {
        if(top_view.getVisibility() == View.GONE) finish();
        else {
            topViewAnimDisapper();
        }
    }
    //头像选择界面动画出现，同时取消底层界面激活状态
    public void topViewAnimShow(){
        Animation pop_up_anim = AnimationUtils.loadAnimation(AccountProfile.this, R.anim.activity_account_profile_head_icon_choose_pop_up);
        top_view.startAnimation(pop_up_anim);
        top_view.setVisibility(View.VISIBLE);
        bottom_view.setActivated(false);
    }
    //头像选择界面动画消失，同时激活底层界面
    public void topViewAnimDisapper(){
        Animation pop_down_anim = AnimationUtils.loadAnimation(AccountProfile.this, R.anim.activity_account_profile_head_icon_pop_down);
        top_view.startAnimation(pop_down_anim);
        top_view.setVisibility(View.GONE);
        bottom_view.setActivated(true);
    }
}
