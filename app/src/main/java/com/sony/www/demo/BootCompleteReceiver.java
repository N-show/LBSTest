package com.sony.www.demo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;


/**
 * Created by nsh on 2017/10/24.
 */

public class BootCompleteReceiver extends BroadcastReceiver {
    public final String TAG = this.getClass().getSimpleName();
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "BootComplete", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "BootComplete");
    }
}
