package com.sony.download;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.sony.www.demo.R;

public class DownloadActivity extends AppCompatActivity implements View.OnClickListener {

    public final String TAG = getClass().getSimpleName();

    String downloadUrl = "http://api.map.baidu.com/portal/sdk/api_filedownload?file_path=/Android/2-8-0/BaiduLBS_Android_V2.8.0_33554464.zip";


    private Button start;
    private Button pause;
    private Button cancel;


    private DownloadService.DownloadBinder downloadBinder;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            downloadBinder = (DownloadService.DownloadBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        start = (Button) findViewById(R.id.start);
        pause = (Button) findViewById(R.id.pause);
        cancel = (Button) findViewById(R.id.cancel);

        start.setOnClickListener(this);
        pause.setOnClickListener(this);
        cancel.setOnClickListener(this);

        Intent downloadService = new Intent(this, DownloadService.class);
        startService(downloadService);
        bindService(downloadService, serviceConnection, BIND_AUTO_CREATE);

////        是否不再提示开启运行时权限的对话框
//        boolean isShow = ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);


//        检查有没有授权这个权限 没有则要请求这个权限 设置运行时权限
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //            请求权限 请求码为1
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        }
    }

    @Override
    public void onClick(View v) {
        if (downloadBinder == null) {
            return;
        }
        int id = v.getId();
        switch (id) {
            case R.id.start:
                downloadBinder.startDownload(downloadUrl);
                break;
            case R.id.pause:
                downloadBinder.pauseDownload();
                break;
            case R.id.cancel:
                downloadBinder.cancelDownload();
                break;
            default:
                break;
        }
    }

    /**
     * 请求运行时权限 用户选择允许拒绝后会回调此方法 根据requestCode 和 grantResults 来做相应处理
     *
     * @param requestCode
     * @param permissions
     * @param grantResults 0 or -1
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "拒绝权限程序将无法使用", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }
}
