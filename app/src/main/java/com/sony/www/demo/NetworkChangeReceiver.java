package com.sony.www.demo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

/**
 * Created by nsh on 2017/10/24.
 */

public class NetworkChangeReceiver extends BroadcastReceiver {

    private ConnectivityManager connectivityManager;
    private NetworkInfo activeNetworkInfo;

    @Override
    public void onReceive(Context context, Intent intent) {

        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isAvailable()) {
            Toast.makeText(context, "network available", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "network not available", Toast.LENGTH_SHORT).show();
        }

    }
}
