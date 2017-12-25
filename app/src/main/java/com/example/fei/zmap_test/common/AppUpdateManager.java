package com.example.fei.zmap_test.common;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fei.zmap_test.http.HttpCallback;
import com.example.fei.zmap_test.http.HttpRequest;

/**
 * Created by FEI on 2017/12/25.
 * 更新软件管理员
 */

public class AppUpdateManager implements HttpCallback {
    private static final int NEW_VERSION_FOUND = 1;
    private static final int VERSION_UP_TO_DATE = 0;
    private static final int ERROR_ON_CHECKING_UPDATE = -1;
    private Context context;
    private int versionCode;

    public AppUpdateManager(Context context, int versionCode){
        this.context = context;
        this.versionCode = versionCode;
    }

    public void checkForUpdate(){
        Toast.makeText(context, "正在检查更新...", Toast.LENGTH_SHORT).show();
        HttpRequest.getOurInstance().getUpdateCode(versionCode, this);
    }
    private void fireUpDialog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setCancelable(true);
        dialog.setTitle("是否下载最新版本？");
        dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //todo begin download
            }
        });
        dialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    @Override
    public void onFinish(int status) {
        switch (status){
            case NEW_VERSION_FOUND:
                fireUpDialog();
                break;
            case VERSION_UP_TO_DATE:
                Toast.makeText(context, "您已在使用最新版的Zmap", Toast.LENGTH_SHORT).show();
                break;
            case ERROR_ON_CHECKING_UPDATE:
                Toast.makeText(context, "ε=(´ο｀*)))无法连接到服务器", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
