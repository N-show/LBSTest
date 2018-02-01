package com.sony.www.demo;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.sony.test.BaseActivity;

import java.util.ArrayList;
import java.util.Random;

import static com.sony.test.ActivityCollector.dayin;

public class MainActivity extends BaseActivity {


    private ArrayList<Fruit> fruits = new ArrayList<Fruit>();
    private ArrayList<Msg> msgs = new ArrayList<Msg>();
    private RecyclerView re;
    private FruitAdapter fruitAdapter;
    private LinearLayoutManager linearLayout;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private MsgAdapter msgAdapter;
    private EditText input_text;
    private Button send;
    private IntentFilter intentFilter;
    private NetworkChangeReceiver networkChangeReceiver;
    private Button force;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initDate();

        re = (RecyclerView) findViewById(R.id.msg_recycler_view);
        input_text = (EditText) findViewById(R.id.input_text);
        send = (Button) findViewById(R.id.send);
        force = (Button) findViewById(R.id.force_offline);


        force.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.sony.broadcast.FORCE_OFFLINE");
                sendBroadcast(intent);
            }
        });


        linearLayout = new LinearLayoutManager(this);
//        linearLayout.setOrientation(LinearLayoutManager.HORIZONTAL);

        staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);

        fruitAdapter = new FruitAdapter(fruits);
        msgAdapter = new MsgAdapter(msgs);

        re.setLayoutManager(linearLayout);
//        re.setAdapter(fruitAdapter);
//        re.setLayoutManager(staggeredGridLayoutManager);
        re.setAdapter(msgAdapter);

        re.scrollToPosition(msgs.size() - 1);


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = input_text.getText().toString();
                if (!content.isEmpty()) {
                    Msg msg = new Msg(content, Msg.TYPE_SEND);
                    msgs.add(msg);
                    msgAdapter.notifyItemInserted(msgs.size() - 1);
                    re.scrollToPosition(msgs.size() - 1);
                    input_text.setText("");

                }

            }
        });

//        intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
//        networkChangeReceiver = new NetworkChangeReceiver();
//        registerReceiver(networkChangeReceiver, intentFilter);

        //Send Broadcast
        Intent intent = new Intent("com.sony.www.demo.MyBroadcastReceiver");
//        sendBroadcast(intent);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != networkChangeReceiver) {
            unregisterReceiver(networkChangeReceiver);
        }
    }


    private void initDate() {

        for (int i = 0; i < 200; i++) {
            Fruit fruit = new Fruit(getRandomLengthName(), R.mipmap.ic_launcher);
            fruits.add(fruit);
        }


        for (int i = 0; i < 20; i++) {
            Msg msg = new Msg(getRandomLengthName(), i % 2);
            msgs.add(msg);
        }
    }

    private String getRandomLengthName() {

        Random random = new Random();

        int length = random.nextInt(60) + 1;

        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < length; i++) {
            stringBuilder.append("Apple");
        }
        return stringBuilder.toString();
    }

}
