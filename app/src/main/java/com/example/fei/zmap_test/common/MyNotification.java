package com.example.fei.zmap_test.common;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.text.format.Time;
import android.util.Log;

import com.example.fei.zmap_test.MessageCenterActivity;
import com.example.fei.zmap_test.R;
import com.example.fei.zmap_test.SearchPageActivity;

/**
 * Created by do_pc on 2017/12/28.
 *
 */

public class MyNotification {
    private static final String TAG = "Notification";
    private static String[] stringArray;

    /**
     *  基于时间的间隔确定是否推送
     * @param sharedPreferences 源
     * @param editor    源
     * @return boolean
     */
    public static boolean isTimeToSend(SharedPreferences sharedPreferences, SharedPreferences.Editor editor){
        int interval = sharedPreferences.getInt("interval", 43200);
        int last_time =sharedPreferences.getInt("last_time",0);
        String curr_time =""+System.currentTimeMillis();
        curr_time = curr_time.substring(1,10);
        editor.putInt("last_time",Integer.valueOf(curr_time));
        editor.apply();
        return (Integer.valueOf(curr_time) - last_time) > interval;
    }

    /**
     * 根据时间段选择内容
     * @param notificationManager 通知管理器
     */
    public static void whichToSend(NotificationManager notificationManager){
        Resources res =MyApplication.getContext().getResources();
        Time time =new Time();
        time.setToNow();
        int hour=time.hour;
        Log.e(TAG, "isTimeToSend: hour"+hour );
        switch (hour){
            case 1:
            case 2:
            case 3:stringArray=res.getStringArray(R.array.midnight);break;
            case 4:
            case 5:
            case 6:stringArray=res.getStringArray(R.array.beforedawn);break;
            case 7:
            case 8:
            case 9:stringArray=res.getStringArray(R.array.morning);break;
            case 10:
            case 11:
            case 12:
            case 13:stringArray=res.getStringArray(R.array.noon);break;
            case 14:
            case 15:
            case 16:stringArray=res.getStringArray(R.array.afternoon);break;
            case 17:
            case 18:
            case 19:
            case 20:stringArray=res.getStringArray(R.array.evening);break;
            case 21:
            case 22:stringArray=res.getStringArray(R.array.night);break;
            case 23:
            case 24:
            case 0:stringArray=res.getStringArray(R.array.midnight);break;
            default:break;
        }

        Notification.Builder builder = new Notification.Builder(MyApplication.getContext());
        builder.setSmallIcon(R.drawable.snowy_day)
                .setTicker(stringArray[1])
                .setContentTitle(stringArray[0])
                .setContentText(stringArray[2])
                .setAutoCancel(true);
        Intent intent;
        if(!stringArray[1].contains("sleepy")){
            intent = new Intent(MyApplication.getContext(), SearchPageActivity.class);
        }else {
            intent = new Intent(MyApplication.getContext(), MessageCenterActivity.class);
        }
        PendingIntent pendingIntents = PendingIntent.getActivity(MyApplication.getContext(), 0, intent,0);
        builder.setContentIntent(pendingIntents);
        notificationManager.notify(1, builder.build());

    }
}
