package com.example.fei.zmap_test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_layout);
        getSupportActionBar().hide();

        addListener(R.id.SettingActivity_about_zmap);
        addListener(R.id.SettingActivity_check_update);
        addListener(R.id.SettingActivity_city_switch);
        addListener(R.id.SettingActivity_clear_cache);
        addListener(R.id.SettingActivity_map_setting);
        addListener(R.id.SettingActivity_message_notify);
        addListener(R.id.SettingActivity_navigation_setting);
        addListener(R.id.SettingActivity_wifi_auto_download);
    }
    public void addListener(final int res){
        findViewById(res).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (res){
                    case R.id.SettingActivity_about_zmap:
                        Toast.makeText(SettingActivity.this, "正在全力开发中...", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.SettingActivity_check_update:
                        Toast.makeText(SettingActivity.this, "正在全力开发中...", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.SettingActivity_city_switch:
                        Toast.makeText(SettingActivity.this, "正在全力开发中...", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.SettingActivity_clear_cache:
                        Toast.makeText(SettingActivity.this, "正在全力开发中...", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.SettingActivity_map_setting:
                        Toast.makeText(SettingActivity.this, "正在全力开发中...", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.SettingActivity_message_notify:
                        Toast.makeText(SettingActivity.this, "正在全力开发中...", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.SettingActivity_navigation_setting:
                        Toast.makeText(SettingActivity.this, "正在全力开发中...", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.SettingActivity_wifi_auto_download:
                        Toast.makeText(SettingActivity.this, "正在全力开发中...", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }
}
