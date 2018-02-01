package com.sony.view;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sony.www.demo.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class WebViewActivity extends AppCompatActivity implements View.OnClickListener {

    public final String TAG = this.getClass().getSimpleName();
    public static final int UPDATE_TEXT = 1;
    public String url = "http://172.16.241.212:8080/get_data.json";

    private WebView webView;
    private Button connection;
    private HttpURLConnection urlConnection;
    private HttpURLConnection urlConnection1;
    private BufferedReader bufferedReader;
    private TextView webText;
    private Button okHttp;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATE_TEXT:
                    webText.setText("Nice to meet you");
                    Log.d(TAG, "handler message");
                    break;
                case 2:
                    webText.setText("Nice to meet you2");
                    Log.d(TAG, "handler message2");
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
//        webView = (WebView) findViewById(R.id.web_view);

//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.setWebViewClient(new WebViewClient());
//        webView.loadUrl("http://www.baidu.com");

        connection = (Button) findViewById(R.id.connection);
        okHttp = (Button) findViewById(R.id.okhttp);
        webText = (TextView) findViewById(R.id.web_text);
        connection.setOnClickListener(this);
        okHttp.setOnClickListener(this);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.connection:
                sendRequestWithHttpURLConnection();
                break;
            case R.id.okhttp:
//                okHttpRequest();
//                okHttpUtilRequest();
                sendMessage();
                break;
            default:
                break;
        }
    }

    private void sendMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "sendMessage");
                Message message = Message.obtain();
                message.what = 2;
                handler.sendMessage(message);
            }
        }).start();
    }

    private void okHttpUtilRequest() {
        HttpUtil.sendHttpRequest(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "connection error");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                showRsponse(response.toString());
                parseJSONWithGson(response.body().string());
            }
        });
    }

    private void okHttpRequest() {

        new Thread(new Runnable() {
            @Override
            public void run() {


                //okhttp
                OkHttpClient okHttpClient = new OkHttpClient();

                //get
                Request getPequest = new Request.Builder()
                        .url(url)
                        .build();

                //post
                RequestBody postRequestBody = new FormBody.Builder()
                        .add("username", "admin")
                        .add("password", "123456")
                        .build();
                Request postRequest = new Request.Builder()
                        .url(url)
                        .post(postRequestBody)
                        .build();

                try {
                    Response response = okHttpClient.newCall(getPequest).execute();
                    String responseData = response.body().string();
                    parseJSONWithGson(responseData);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    private void parseJSONWithGson(String responseData) {
        Gson gson = new Gson();
        List<Person> personList = gson.fromJson(responseData, new TypeToken<List<Person>>() {
        }.getType());
        for (Person p : personList) {
            Log.d(TAG, p.getId());
            Log.d(TAG, p.getName());
            Log.d(TAG, p.getVersion());
        }
    }

    private void sendRequestWithHttpURLConnection() {
        //open thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://www.baidu.com");
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.getContentEncoding();
                    urlConnection.setConnectTimeout(8000);
                    urlConnection.setReadTimeout(8000);

                    InputStream inputStream = urlConnection.getInputStream();
                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder response = new StringBuilder();
                    String line = null;
                    while ((line = bufferedReader.readLine()) != null) {
                        response.append(line);
                    }
                    showRsponse(response.toString());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
            }
        }).start();
    }

    private void showRsponse(final String s) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                webText.setText(s);
            }
        });
    }
}
