package com.example.fei.zmap_test;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import com.example.fei.zmap_test.common.AppUpdateManager;
import com.example.fei.zmap_test.customlayout.SettingItemLayout;

/**
 * 设置界面
 * 现有功能包括 版本更新，消息推送
 */
public class SettingActivity extends AppCompatActivity {
    public static String MY_CITY_NAME;
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

        if(ContextCompat.checkSelfPermission(SettingActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE )!= PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(SettingActivity.this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        getVersionCodeName();

        SettingItemLayout checkUpdateView = findViewById(R.id.SettingActivity_check_update);
        checkUpdateView.setVersionName(versionName);

        SettingItemLayout citySwitch = findViewById(R.id.SettingActivity_city_switch);
        if(!TextUtils.isEmpty(MY_CITY_NAME)) citySwitch.setDetail("城市 " + MY_CITY_NAME);

        addListener(R.id.SettingActivity_about_zmap);
        addListener(R.id.SettingActivity_check_update);
        addListener(R.id.SettingActivity_city_switch);
        addListener(R.id.SettingActivity_clear_cache);
        addListener(R.id.SettingActivity_map_setting);
        addListener(R.id.SettingActivity_navigation_setting);
    }

    /**
     * 为每个控件添加监听器
     * @param res 控件ID
     */
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
                        intent = new Intent(SettingActivity.this, CitySelectActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.SettingActivity_clear_cache:
                    case R.id.SettingActivity_map_setting:
                        intent = new Intent(SettingActivity.this, MapSettingActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.SettingActivity_message_notify:
                        changeNotificationStatus();
                        break;
                    case R.id.SettingActivity_navigation_setting:
                    case R.id.SettingActivity_wifi_auto_download:
                        Toast.makeText(SettingActivity.this, "正在全力开发中...", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    /**
     * 改变消息推送的开启状态
     */
    public void changeNotificationStatus(){
        Switch s = findViewById(R.id.SettingActivity_message_notify);
        SharedPreferences sharedPreferences = getSharedPreferences("time", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(s.isChecked()){
            editor.putInt("interval", 43200);
        } else{
            editor.putInt("interval", 0x7FFFFFFF);
        }
        editor.apply();
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
