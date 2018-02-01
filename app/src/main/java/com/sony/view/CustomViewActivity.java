package com.sony.view;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.sony.www.demo.R;

/**
 * Created by nsh on 2017/12/6. 下午4:01
 */

public class CustomViewActivity extends AppCompatActivity implements View.OnClickListener {

    public final String TAG = this.getClass().getSimpleName();
    private Button createDatebase;
    private Button sendNotification;

    private static final int NOTIFICATION_ID = 1;
    private static final String NOTIFICATION_CHANNEL_ID = "my_notification_channel";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_view);
//        Connector.getDatabase();
        Log.d(TAG, "this CustomViewActivity");

        createDatebase = (Button) findViewById(R.id.create_database);
        sendNotification = (Button) findViewById(R.id.send_notification);
        createDatebase.setOnClickListener(this);
        sendNotification.setOnClickListener(this);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    call();
                } else {
                    Toast.makeText(CustomViewActivity.this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;

            default:
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {

            case R.id.create_database:
                if (ContextCompat.checkSelfPermission(CustomViewActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(CustomViewActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                    Log.d(TAG, "request user permission");
                } else {
                    call();
                }
                break;


            case R.id.send_notification:
                //pending intent
                Intent intent = new Intent(this, NotificationActivity.class);
                AudioAttributes att = new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                        .build();

                PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
                //getServer
                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "channel1", NotificationManager.IMPORTANCE_DEFAULT);
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(Color.RED);
                notificationChannel.setShowBadge(true);
                notificationChannel.setVibrationPattern(new long[]{0, 2000, 1000, 1000});
                notificationChannel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE), att);
                notificationManager.createNotificationChannel(notificationChannel);

                Notification.Builder builder = new Notification.Builder(this, NOTIFICATION_CHANNEL_ID)
                        .setContentTitle("This is content title")
                        .setContentText("This is content text")
                        .setWhen(System.currentTimeMillis())
                        .setNumber(3)
                        .setAutoCancel(true)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentIntent(pi);
//                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round));

                Log.d(TAG, "send notification");
                notificationManager.notify(1, builder.build());

                break;
            default:
                break;
        }
    }

    private void call() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:10086"));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
