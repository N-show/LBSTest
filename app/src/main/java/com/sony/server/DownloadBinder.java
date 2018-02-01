package com.sony.server;

import android.os.Binder;
import android.util.Log;

/**
 * Created by nsh on 2018/1/8. 下午2:40
 */

public class DownloadBinder extends Binder {

    public final String TAG = getClass().getSimpleName();

    public void startDownload() {
        Log.d(TAG, "startDownload");
    }

    public int getProgress() {
        Log.d(TAG, "getProgress");
        return 0;
    }



}
