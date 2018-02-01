package com.sony.server;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.nfc.Tag;
import android.os.Binder;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.sony.www.demo.R;

public class ServiceActivity extends AppCompatActivity implements View.OnClickListener {

    public final String TAG = getClass().getSimpleName();

    private Button start_server;
    private Button stop_server;
    private Button bind_service;
    private Button unbind_service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        start_server = (Button) findViewById(R.id.start_service);
        bind_service = (Button) findViewById(R.id.bind_service);
        unbind_service = (Button) findViewById(R.id.unbind_service);
        stop_server = (Button) findViewById(R.id.stop_service);

        start_server.setOnClickListener(this);
        bind_service.setOnClickListener(this);
        unbind_service.setOnClickListener(this);
        stop_server.setOnClickListener(this);
    }

    ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            DownloadBinder binder = (DownloadBinder) service;
            binder.startDownload();
            binder.getProgress();

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected");

        }
    };

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.start_service:
                Intent startIntent = new Intent(this, MyService.class);
                startService(startIntent);
                break;
            case R.id.bind_service:
                Intent bindIntent = new Intent(this, MyService.class);
                bindService(bindIntent, connection, BIND_AUTO_CREATE);
                break;
            case R.id.unbind_service:
                unbindService(connection);
                break;
            case R.id.stop_service:
                Intent stopIntent = new Intent(this, MyService.class);
                stopService(stopIntent);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
