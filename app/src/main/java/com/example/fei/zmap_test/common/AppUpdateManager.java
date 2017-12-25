package com.example.fei.zmap_test.common;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fei.zmap_test.R;
import com.example.fei.zmap_test.http.HttpCallback;
import com.example.fei.zmap_test.http.HttpRequest;

import java.io.File;

import static android.content.Context.DOWNLOAD_SERVICE;

/**
 * Created by FEI on 2017/12/25.
 * 更新软件管理员
 */

public class AppUpdateManager extends BroadcastReceiver implements HttpCallback {
    private static final String TAG = "AppUpdateManager";
    private static final int NEW_VERSION_FOUND = 1;
    private static final int VERSION_UP_TO_DATE = 0;
    private static final int ERROR_ON_CHECKING_UPDATE = -1;
    private Context context;
    private int versionCode;
    private int statusCode;
    private long downloadId;

    public AppUpdateManager(Context context, int versionCode){
        this.context = context;
        this.versionCode = versionCode;
        statusCode = 1;
    }

    /**
     * 当该对象任务出错或任务执行完毕之后调用
     * 作用：context取消注册广播接收器并设置其statusCode为0
     */
    public void setUseless(boolean isRegistered){
        if(isRegistered) context.unregisterReceiver(AppUpdateManager.this);
        statusCode = 0;
    }

    /**
     * 接收下载完成的广播
     * @param context：context
     * @param intent：intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        long downloadCompleteId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
        if(downloadCompleteId != -1 && downloadCompleteId== downloadId){
            installAPK(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/Zmap_test.apk"));
        }
        setUseless(true);
    }

    /**
     * 执行此方法开始检查更新
     */
    public void checkForUpdate(){
        Toast.makeText(context, "正在检查更新...", Toast.LENGTH_SHORT).show();
        HttpRequest.getOurInstance().getUpdateCode(versionCode, this);
    }

    /**
     * 检测到新版本，启动对话框
     */
    private void fireUpDialog(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                dialog.dismiss();
                setUseless(false);
            }
        });
        dialog.setCancelable(true);
        dialog.setTitle("是否下载最新版本？");
        dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
                context.registerReceiver(AppUpdateManager.this, intentFilter);

                DownloadManager downloadManager = (DownloadManager)context.getSystemService(DOWNLOAD_SERVICE);
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(MyApplication.getContext().getString(R.string.DOWNLOAD_URL)));
                request.setDestinationInExternalPublicDir("download", "/Zmap_test.apk");
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
                request.setMimeType("application/vnd.android.package-archive");

                if(downloadManager != null) downloadId = downloadManager.enqueue(request);
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
                setUseless(false);
                break;
            case ERROR_ON_CHECKING_UPDATE:
                Log.e(TAG, "onFinish: error go here");
                Toast.makeText(context, "ε=(´ο｀*)))无法连接到服务器", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "onFinish: error go away");
                setUseless(false);
                break;
        }
    }
    protected void installAPK(File file) {
        if (!file.exists()) return;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.parse("file://" + file.toString());
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        //在服务中开启activity必须设置flag,后面解释
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        setUseless(false);
    }

    public int getStatusCode() {
        return statusCode;
    }
}
