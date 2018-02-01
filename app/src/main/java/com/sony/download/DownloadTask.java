package com.sony.download;

import android.os.AsyncTask;
import android.os.Environment;

import com.sony.download.utils.LogUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author:nsh
 * @data:2018/1/23. 下午1:12
 */

// 1传入字符给后台任务 2使用整型数据来作为进度显示单位  3使用整型数据来返回执行结果
public class DownloadTask extends AsyncTask<String, Integer, Integer> {

    public final String TAG = getClass().getSimpleName();


    public static final int TYPE_SUCCESS = 0;
    public static final int TYPE_FAILED = 1;
    public static final int TYPE_PAUSED = 2;
    public static final int TYPE_CANCELED = 3;

    private DownloadListener downloadListener;

    private boolean isCanceled = false;
    private boolean isPaused = false;
    private int lastProgress;

    public DownloadTask(DownloadListener downloadListener) {
        this.downloadListener = downloadListener;
    }


    /**
     * 主线程中调用
     * 此方法在后台任务开始前调用 用于一些界面初始化的操作 比如显示一个进度条
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    /**
     * 此方法是在子线程中执行的
     *
     * @param strings
     * @return
     */
    @Override
    protected Integer doInBackground(String... strings) {

        InputStream is = null;
        RandomAccessFile saveFile = null;
        File file = null;

        try {
//          记录已下载文件的长度
            long downloadLength = 0;
//          下载地址
            String downloadUrl = strings[0];
//          下载文件名字
            String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
//          下载文件的存储位置
            String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
            LogUtils.d(TAG, "所下载文件的路径为:" + directory);

            file = new File(directory + fileName);

//          如果文件存在就 获取已下载文件长度 以便于断点下载 知道再次下载的位置
            if (file.exists()) {
                downloadLength = file.length();
            }

//          得到文件的总长度
            long contentLength = getContentLength(downloadUrl);
            LogUtils.d(TAG, "所下载文件的总大小为:" + contentLength);

//          获取到的字节为0 连接文件失败
            if (contentLength == 0) {
                return TYPE_FAILED;
//          已下载字节和总字节一样 下载成功
            } else if (downloadLength == contentLength) {
                return TYPE_SUCCESS;
            }

            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder()
//                  断点下载 指定从哪个字节开始下载
                    .addHeader("Range", "bytes=" + downloadLength + "-")
                    .url(downloadUrl)
                    .build();
            Response response = okHttpClient.newCall(request).execute();

            if (response != null) {
                is = response.body().byteStream();
                saveFile = new RandomAccessFile(file, "rw");
                saveFile.seek(downloadLength);
                byte[] b = new byte[1024];
                int total = 0;
                int len = 0;

                while ((len = is.read(b)) != -1) {
                    if (isCanceled) {
                        return TYPE_CANCELED;
                    } else if (isPaused) {
                        return TYPE_PAUSED;
                    } else {
                        total += len;
                    }

                    saveFile.write(b, 0, len);
//                  得到当前下载的百分比
                    int progress = (int) ((total + downloadLength) * 100 / contentLength);
//                  通知进度的方法也是在子线程中 通过handler发送消息来通知
                    publishProgress(progress);

                }
            }

            response.body().close();
            return TYPE_SUCCESS;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {

                if (is != null) {
                    is.close();
                }

                if (saveFile != null) {
                    saveFile.close();
                }

                if (isCanceled && file != null) {
                    file.delete();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return TYPE_FAILED;
    }


    /**
     * 此方法在主线程中
     * 后台任务调用 publishProgress(progress) 方法后 此方法调用 这里可以进行UI操作
     *
     * @param values
     */
    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        int progress = values[0];

        if (progress > lastProgress) {
            downloadListener.onProgress(progress);
            lastProgress = progress;
        }
    }


    /**
     * 此方法在主线程中
     * 当后台任务结束后此方法调用 可以进行UI操作 提示任务完成 关闭对话框
     *
     * @param status
     */
    @Override
    protected void onPostExecute(Integer status) {
        super.onPostExecute(status);

        switch (status) {
            case TYPE_SUCCESS:
                downloadListener.onSuccess();
                break;
            case TYPE_FAILED:
                downloadListener.onFailed();
                break;
            case TYPE_PAUSED:
                downloadListener.onPaused();
                break;
            case TYPE_CANCELED:
                downloadListener.onCanceled();
                break;
            default:
                break;
        }
    }


    /**
     * 返回文件大小
     *
     * @param downloadUrl
     * @return
     * @throws IOException
     */
    private long getContentLength(String downloadUrl) throws IOException {

        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(downloadUrl)
                .build();
        Response response = okHttpClient.newCall(request).execute();

        if (response != null && response.isSuccessful()) {
            long contentLength = response.body().contentLength();
            response.close();
            return contentLength;
        }


        return 0;
    }


    public void pausedDownload() {
        isPaused = true;
    }

    public void cancelDownload() {
        isCanceled = true;
    }

}
