package com.example.fei.zmap_test;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class AccountProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_profile_layout);
    }
    public void addListener(final int res){
        findViewById(res).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (res){
                    case R.id.Account_profile_head_icon_setting:
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
                            }
                        });
                        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //TODO cancel
                                Toast.makeText(AccountProfile.this, "这里应该取消退出", Toast.LENGTH_SHORT).show();
                            }
                        });
                        break;
                    case R.id.Account_profile_user_name_setting:
                        Toast.makeText(AccountProfile.this, "正在全力开发中...", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }
}
