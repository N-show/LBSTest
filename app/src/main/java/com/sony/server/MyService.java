package com.sony.server;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.os.IBinder;
import android.util.Log;

import com.sony.view.NotificationActivity;
import com.sony.www.demo.R;

public class MyService extends Service {

    public final String TAG = this.getClass().getSimpleName();
    public static final String PRIMARY_CHANNEL = "default";

    private DownloadBinder mBinder = new DownloadBinder();


    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate service");
//        sendNotification();


    }

    private void sendNotification() {
        //        得到notification的管理器
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

//        这里设置点击notification后打开的activity  使用pendingIntent的方式
        Intent intent = new Intent(this, NotificationActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);

//        设置铃声属性
        AudioAttributes att = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build();

//        以后的通知要先创建通知渠道 channel  channelId = PRIMARY_CHANNEL   channelName = "MyChannel"  channel 级别 = 3 default
        NotificationChannel myChannel = new NotificationChannel(PRIMARY_CHANNEL, "MyChannel", NotificationManager.IMPORTANCE_DEFAULT);
//        是否在桌面icon右上角展示小红点
        myChannel.enableLights(true);
//        小红点颜色
        myChannel.setLightColor(Color.RED);
//        是否在久按桌面图标时显示此渠道的通知
        myChannel.setShowBadge(true);
//        设置震动频率
        myChannel.setVibrationPattern(new long[]{0, 2000, 1000, 1000});
//        设置通知的声音
        myChannel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE), att);
//        创建channel
        mNotificationManager.createNotificationChannel(myChannel);

//        得到对应channelId的channel
//        NotificationChannel mChannel = mNotificationManager.getNotificationChannel(PRIMARY_CHANNEL);
//        删除对应channelId的channel
//        mNotificationManager.deleteNotificationChannel(PRIMARY_CHANNEL);


//        创建notification 还要传入对应的 channelId
        Notification.Builder mBuilder = new Notification.Builder(this.getApplicationContext(), PRIMARY_CHANNEL)
//                设置小图标
                .setSmallIcon(R.drawable.easy)
//                设置标题
                .setContentTitle("My notification")
//                设置文本
                .setContentText("Hello World")
//                设置自动消失
                .setAutoCancel(true)
//                设置通知的数量
                .setNumber(3)
//                设置点击后打开的activity
                .setContentIntent(pi);
//        id 一样会覆盖同样的消息
        for (int i = 0; i < 3; i++) {
            mNotificationManager.notify(i, mBuilder.build());
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        Log.d(TAG, "onBind service");
        return mBinder;

    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind service");
        return super.onUnbind(intent);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand service");
        sendNotification();
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public boolean stopService(Intent name) {
        Log.d(TAG, "stopService service");
        return super.stopService(name);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy service");


    }
}
