package com.example.fei.zmap_test;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fei.zmap_test.http.HttpCallback;
import com.example.fei.zmap_test.http.HttpRequest;
import com.example.fei.zmap_test.db.Users;

import org.litepal.crud.DataSupport;

public class AccountProfileActivity extends AppCompatActivity implements HttpCallback {
    public Users current_user;
    private ScrollView top_view;
    private ImageButton user_head_icon_btn;
    private LinearLayout bottom_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_profile_layout);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) actionBar.hide();

        user_head_icon_btn = findViewById(R.id.Account_profile_head_icon_btn);
        top_view = findViewById(R.id.Account_profile_head_icon_choose_view);
        bottom_view = findViewById(R.id.Account_profile_view);
        TextView username_view = findViewById(R.id.Account_profile_nick_name_text);

        current_user = DataSupport.findLast(Users.class);  //从数据库读出当前登陆的用户
        username_view.setText( current_user.getUsername());
        user_head_icon_btn.setImageResource(getHeadIconResourceFromId(current_user.getId_head()));



        addListener(R.id.Account_profile_head_icon_setting);//头像设置界面
        addListener(R.id.Account_profile_logout); //logout
        addListener(R.id.Account_profile_user_name_setting);//设置用户名
        addListener(R.id.Account_profile_all_head_icon_holder);//头像选择界面
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
                        AlertDialog.Builder dialog = new AlertDialog.Builder(AccountProfileActivity.this);
                        dialog.setTitle("退出登录？");
                        dialog.setCancelable(true);
                        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                current_user=DataSupport.findLast(Users.class);
                                DataSupport.deleteAll(Users.class,"User_id > ?","0");
                                finish();
                            }
                        });
                        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        dialog.show();
                        break;
                    case R.id.Account_profile_user_name_setting:
                        Toast.makeText(AccountProfileActivity.this, "正在全力开发中...", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.Account_profile_all_head_icon_holder:
                        topViewAnimDisappear();
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
                int id_head= Integer.parseInt((String)v.getTag());
                user_head_icon_btn.setImageResource(getHeadIconResourceFromId(id_head));
                HttpRequest.getOurInstance().changeHeadIcon(current_user.getUsername(),id_head,AccountProfileActivity.this);
                Users users= new Users();
                users.setId_head(id_head);
                users.updateAll("User_id > ?","0");
                topViewAnimDisappear();

            }
        };
        findViewById(R.id.Account_profile_head_icon_choose_btn_1).setOnClickListener(onClickListener);
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
            topViewAnimDisappear();
        }
    }

    //头像选择界面动画出现，同时取消底层界面激活状态
    public void topViewAnimShow(){
        Animation pop_up_anim = AnimationUtils.loadAnimation(AccountProfileActivity.this, R.anim.activity_account_profile_head_icon_choose_pop_up);
        top_view.startAnimation(pop_up_anim);
        top_view.setVisibility(View.VISIBLE);
        bottom_view.setActivated(false);
    }

    //头像选择界面动画消失，同时激活底层界面
    public void topViewAnimDisappear(){
        Animation pop_down_anim = AnimationUtils.loadAnimation(AccountProfileActivity.this, R.anim.activity_account_profile_head_icon_pop_down);
        top_view.startAnimation(pop_down_anim);
        top_view.setVisibility(View.GONE);
        bottom_view.setActivated(true);
    }


    //通过ID获得View
    public static int getHeadIconResourceFromId(int id){
        switch (id){
            case 0:return R.drawable.profile_head;
            case 1:return R.drawable.avatar_1;
            case 2:return R.drawable.avatar_2;
            case 3:return R.drawable.avatar_3;
            case 4:return R.drawable.avatar_4;
            case 5:return R.drawable.avatar_5;
            case 6:return R.drawable.avatar_6;
            case 7:return R.drawable.avatar_7;
            case 8:return R.drawable.avatar_8;
            case 9:return R.drawable.avatar_9;
            case 10:return R.drawable.avatar_10;
            case 11:return R.drawable.avatar_11;
            case 12:return R.drawable.avatar_12;
            case 13:return R.drawable.avatar_13;
            case 14:return R.drawable.avatar_14;
            case 15:return R.drawable.avatar_15;
            case 16:return R.drawable.avatar_16;
            case 17:return R.drawable.avatar_17;
            case 18:return R.drawable.avatar_18;
            default:return 0;
        }
    }

    /**
     * 回调处理返回数据
     *
     * @param status ：返回状态
     */
    @Override
    public void onFinish(int status) {

    }
}
