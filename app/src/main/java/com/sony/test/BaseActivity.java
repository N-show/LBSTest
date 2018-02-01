package com.sony.test;

import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by nsh on 2017/10/25.
 */

public class BaseActivity extends AppCompatActivity {

    public final String TAG = getClass().getSimpleName();

    private ForceOfflineReceiver forceOfflineReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter intentFilter = new IntentFilter("com.sony.broadcast.FORCE_OFFLINE");
        forceOfflineReceiver = new ForceOfflineReceiver();

        registerReceiver(forceOfflineReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (null != forceOfflineReceiver) {
            unregisterReceiver(forceOfflineReceiver);
            forceOfflineReceiver = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
        ActivityCollector.stringBuilder.delete(0, ActivityCollector.stringBuilder.length());
    }
}
