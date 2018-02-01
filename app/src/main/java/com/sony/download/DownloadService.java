package com.sony.download;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.widget.Toast;

import com.sony.download.utils.LogUtils;
import com.sony.www.demo.R;

import java.io.File;


public class DownloadService extends Service {

    private static final String PRIMARY_CHANNEL = "default";
    public final String TAG = getClass().getSimpleName();


    private DownloadTask downloadTask;
    private String downloadUrl;

    public DownloadService() {

    }


    private DownloadListener downloadListener = new DownloadListener() {
        @Override
        public void onProgress(int progress) {
            getNotificationManager().notify(1, getnotification("Downloading...", progress));
        }

        @Override
        public void onSuccess() {
            downloadTask = null;
//            将正在下载的前台通知关闭 创建下载成功的新提示
            stopForeground(true);
            getNotificationManager().notify(1, getnotification("Download Success", -1));
            Toast.makeText(DownloadService.this, "Download Success", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFailed() {
            downloadTask = null;
//            将正在下载的前台通知关闭 创建下载失败的新提示
            stopForeground(true);
            getNotificationManager().notify(1, getnotification("Download Failed", -1));
            Toast.makeText(DownloadService.this, "Download Failed", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPaused() {
            downloadTask = null;
//           提示暂停下载
            Toast.makeText(DownloadService.this, "Download Paused", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCanceled() {
            downloadTask = null;
//            将正在下载的前台通知关闭 创建下载成功的新提示
            stopForeground(true);
            Toast.makeText(DownloadService.this, "Download Canceled", Toast.LENGTH_SHORT).show();
        }
    };

    private DownloadBinder mBinder = new DownloadBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    class DownloadBinder extends Binder {

        //        mBinder中开始下载的方法
        public void startDownload(String url) {
            downloadUrl = url;
//            开启AsyncTask任务
            downloadTask = new DownloadTask(downloadListener);
            downloadTask.execute(downloadUrl);
//            开启前台通知
            startForeground(1, getnotification("Downloading...", 0));
            Toast.makeText(DownloadService.this, "Downloading...", Toast.LENGTH_SHORT).show();
        }

        //        mBinder中暂停下载的方法
        public void pauseDownload() {
            if (downloadTask != null) {
                LogUtils.d(TAG, "---暂停");
                downloadTask.pausedDownload();
            }
        }

        //        mBinder中取消下载的方法 并且删除源文件
        public void cancelDownload() {
            if (downloadTask != null) {
                downloadTask.cancelDownload();
            } else {
                if (downloadUrl != null) {
                    String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
                    String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
                    File file = new File(directory + fileName);
                    if (file.exists()) {
                        file.delete();
                    }
                    getNotificationManager().cancel(1);
                    stopForeground(true);
                    Toast.makeText(DownloadService.this, "Canceled", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    public NotificationManager getNotificationManager() {
        return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    private Notification getnotification(String hit, int progress) {
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
//        myChannel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE), att);
//        创建channel
        getNotificationManager().createNotificationChannel(myChannel);

//        得到对应channelId的channel
//        NotificationChannel mChannel = mNotificationManager.getNotificationChannel(PRIMARY_CHANNEL);
//        删除对应channelId的channel
//        mNotificationManager.deleteNotificationChannel(PRIMARY_CHANNEL);


//        这里设置点击notification后打开的activity  使用pendingIntent的方式
        Intent intent = new Intent(this, DownloadActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);

//        创建notification 还要传入对应的 channelId
        Notification.Builder mBuilder = new Notification.Builder(this.getApplicationContext(), PRIMARY_CHANNEL)
//                设置小图标
                .setSmallIcon(R.mipmap.ic_launcher)
//                设置大图标
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.easy))
//                设置标题
                .setContentTitle(hit)
//                设置自动消失
                .setAutoCancel(true)
//                设置通知的数量
                .setNumber(3)
//                设置点击后打开的activity
                .setContentIntent(pi);
        if (progress > 0) {
//            设置进度文本
            mBuilder.setContentText(progress + "%");
//            设置提示框进度条的最大值 当前值 操作完成继续展示或者移除
            mBuilder.setProgress(100, progress, false);
        }
        return mBuilder.build();
    }
}
