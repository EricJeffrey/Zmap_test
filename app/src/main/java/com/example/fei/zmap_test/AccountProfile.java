package com.example.fei.zmap_test;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.fei.zmap_test.db.Users;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.litepal.crud.DataSupport;

public class AccountProfile extends AppCompatActivity {
    public Users current_user =null;
    private LinearLayout top_view;
    private ImageButton user_head_icon_btn;
    private LinearLayout bottom_view;
    private String url="http://www.idooooo.tk";//服务器接口地址
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_profile_layout);
        getSupportActionBar().hide();

        user_head_icon_btn = (ImageButton)findViewById(R.id.Account_profile_head_icon_btn);
        top_view = findViewById(R.id.Account_profile_head_icon_choose_view);
        bottom_view = findViewById(R.id.Account_profile_view);

        current_user = DataSupport.findLast(Users.class);

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
                                DataSupport.deleteAll(Users.class);
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
                //那只有更过分了
                switch (((int)v.getId())-2131165185){
                    case 1:user_head_icon_btn.setImageResource(R.drawable.avatar_1);break;
                    case 2:user_head_icon_btn.setImageResource(R.drawable.avatar_10);break;
                    case 3:user_head_icon_btn.setImageResource(R.drawable.avatar_11);break;
                    case 4:user_head_icon_btn.setImageResource(R.drawable.avatar_12);break;
                    case 5:user_head_icon_btn.setImageResource(R.drawable.avatar_13);break;
                    case 6:user_head_icon_btn.setImageResource(R.drawable.avatar_14);break;
                    case 7:user_head_icon_btn.setImageResource(R.drawable.avatar_15);break;
                    case 8:user_head_icon_btn.setImageResource(R.drawable.avatar_16);break;
                    case 9:user_head_icon_btn.setImageResource(R.drawable.avatar_17);break;
                    case 10:user_head_icon_btn.setImageResource(R.drawable.avatar_18);break;
                    case 11:user_head_icon_btn.setImageResource(R.drawable.avatar_2);break;
                    case 12:user_head_icon_btn.setImageResource(R.drawable.avatar_3);break;
                    case 13:user_head_icon_btn.setImageResource(R.drawable.avatar_4);break;
                    case 14:user_head_icon_btn.setImageResource(R.drawable.avatar_5);break;
                    case 15:user_head_icon_btn.setImageResource(R.drawable.avatar_6);break;
                    case 16:user_head_icon_btn.setImageResource(R.drawable.avatar_7);break;
                    case 17:user_head_icon_btn.setImageResource(R.drawable.avatar_8);break;
                    case 18:user_head_icon_btn.setImageResource(R.drawable.avatar_9);break;
                    default:break;
                }
                Toast.makeText(AccountProfile.this, "设置你的头像为" + ((int)v.getId()-2131165185), Toast.LENGTH_SHORT).show();
                sendRequestWithHttpClient((int)v.getId()-2131165185);
                Users users =new Users();
                users.setId_head((int)v.getId()-2131165185);
                users.updateAll();

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

    private void sendRequestWithHttpClient(final int id_head){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpClient httpCient = new DefaultHttpClient();  //创建HttpClient对象
                HttpGet httpGet = new HttpGet(url+"/?action=modifyheadid&username="+current_user.getUsername()+"&id_head="+id_head);
                try {
                    HttpResponse httpResponse = httpCient.execute(httpGet);//第三步：执行请求，获取服务器发还的相应对象
                    if((httpResponse.getEntity())!=null){
                        HttpEntity entity =httpResponse.getEntity();
                        String response = EntityUtils.toString(entity,"utf-8");//将entity当中的数据转换为字符串

                        //TODO 数据根据返回值设置需要才更加严谨
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
