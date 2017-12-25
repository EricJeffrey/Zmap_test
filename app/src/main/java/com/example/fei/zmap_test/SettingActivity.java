package com.example.fei.zmap_test;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.fei.zmap_test.common.AppUpdateManager;
import com.example.fei.zmap_test.customlayout.SettingItemLayout;

public class SettingActivity extends AppCompatActivity {
    private static final String TAG = "SettingActivity";
    private String versionName;
    private int versionCode;
    private AppUpdateManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_layout);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) actionBar.hide();
        getVersionCodeName();

        SettingItemLayout checkUpdateView = findViewById(R.id.SettingActivity_check_update);
        checkUpdateView.setVersionName(versionName);

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
                        Intent intent = new Intent(SettingActivity.this, AboutUsActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.SettingActivity_check_update:
                        if(versionName == null) getVersionCodeName();
                        Log.e(TAG, "onClick: now manager is " + manager);
                        if(manager != null) Log.e(TAG, "onClick: now manager statusCode is " + manager.getStatusCode());
                        if(manager == null || manager.getStatusCode() == 0){
                            manager = new AppUpdateManager(SettingActivity.this, versionCode);
                            manager.checkForUpdate();
                        }
                        else{
                            Toast.makeText(SettingActivity.this, "正在检查更新", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.SettingActivity_city_switch:
                    case R.id.SettingActivity_clear_cache:
                    case R.id.SettingActivity_map_setting:
                    case R.id.SettingActivity_message_notify:
                    case R.id.SettingActivity_navigation_setting:
                    case R.id.SettingActivity_wifi_auto_download:
                        Toast.makeText(SettingActivity.this, "正在全力开发中...", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    /**
     * 获取App版本号和版本名称
     */
    public void getVersionCodeName(){
        PackageManager packageManager = getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
            versionCode = packageInfo.versionCode;
            versionName = packageInfo.versionName;
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
