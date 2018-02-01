package com.sony.download;

/**
 * Created by nsh on 2018/1/23. 下午12:39
 */

public interface DownloadListener {

    /**
     * 通知当前下载进度
     *
     * @param progress
     */
    void onProgress(int progress);

    /**
     * 下载成功
     */
    void onSuccess();

    /**
     * 下载失败
     */
    void onFailed();

    /**
     * 暂停下载
     */
    void onPaused();

    /**
     * 取消下载
     */
    void onCanceled();


}
