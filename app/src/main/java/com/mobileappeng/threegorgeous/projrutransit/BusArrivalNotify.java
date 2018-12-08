package com.mobileappeng.threegorgeous.projrutransit;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import com.example.administrator.weather.MainActivity;

public class BusArrivalNotify extends Service {
    public static final String TAG = "Bus Arrival Serive";
    NotificationManager nm ;
    //用这个变量来唯一的标定一个Notification对象
    final static int NOTIFY = 0x123;
    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(TAG, "onCreate() executed");
    }

    public void send(){
        Intent intent = new Intent(getApplicationContext() , TodaySummaryActivity.class);
        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

        Notification notify =new Notification.Builder(this)
                //设置打开通知后， 该标题栏通知自动消失
                .setAutoCancel(true)
                //设置显示在状态栏中的通知提示信息
                .setTicker("有新消息")
                //设置通知内容的标题
                .setContentTitle("一条新消息")
                //设置通知内容
                .setContentText("阿奇从远方发来贺电")
                //设置使用系统默认的声音、默认LED灯
                .setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_LIGHTS)
                //设置消息中显示的发送时间
                .setWhen(System.currentTimeMillis())
                .setShowWhen(true)   //设置是否setWhe指定的显示时间
                //返回Notification对象
                .build();

        //发送通知
        nm.notify(NOTIFY, notify);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand() executed");
        send();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() executed");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
