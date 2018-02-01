package com.sony.view;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by nsh on 2018/1/3. 上午10:25
 */

public class HttpUtil {

    public static void sendHttpRequest(String address, okhttp3.Callback callback) {

        OkHttpClient okHttpClient = new OkHttpClient();

        Request getRequest = new Request.Builder()
                .url(address)
                .build();

        okHttpClient.newCall(getRequest).enqueue(callback);

    }
}
