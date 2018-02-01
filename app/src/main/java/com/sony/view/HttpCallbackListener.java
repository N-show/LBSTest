package com.sony.view;

/**
 * Created by nsh on 2018/1/3. 上午10:32
 */

public interface HttpCallbackListener {

    void onFinish(String response);

    void onError();

}
