package com.sony.test1;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sony.test.BaseActivity;
import com.sony.www.demo.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class TestActivity extends BaseActivity {

    private EditText ed;
    private Button save;
    private FileOutputStream output = null;
    private BufferedWriter bufferedWriter;
    private FileInputStream input;
    private BufferedReader bufferedReader;
    private StringBuilder stringBuilder;
    private Button restoreBt;
    private Button create;
    private MyDatabaseHelper myDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        ed = (EditText) findViewById(R.id.ed);
        save = (Button) findViewById(R.id.save);
        restoreBt = (Button) findViewById(R.id.restore);
        create = (Button) findViewById(R.id.create);


        myDatabaseHelper = new MyDatabaseHelper(TestActivity.this, "BookStore.db", null, 1);


        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = myDatabaseHelper.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                contentValues.put("name", "The Da Vinci Code");
                contentValues.put("author", "Dan Brown");
                contentValues.put("pages", 500);
                contentValues.put("price", 16.96);
                db.insert("Book", null, contentValues);
                Cursor cursor = db.query("Book", null, null, null, null, null, null);
                Log.d(TAG, cursor.getString(cursor.getColumnIndex("name")));

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                save();
                sharedpreferencesSave();
            }
        });

        restoreBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = getSharedPreferences("data1", MODE_PRIVATE);
                String name = sp.getString("name", "");
                int age = sp.getInt("age", 0);
                boolean gender = sp.getBoolean("gender", false);

                Log.d(TAG, "name = " + name + ", age = " + age + ", gender = " + gender);
            }
        });

//
//        String loadText = load();
//
//        if (!TextUtils.isEmpty(loadText)) {
//            ed.setText(loadText);
//            ed.setSelection(loadText.length());
//
//        }
        TestActivity testActivity = new TestActivity();

    }

    private class InterClass {
        public InterClass() {
            System.out.println("InterClass Create");
        }
    }

    public TestActivity() {
        InterClass interClass = new InterClass();
        System.out.println("OuterClass Create");
    }

    private void sharedpreferencesSave() {
        SharedPreferences.Editor edit = getSharedPreferences("data1", MODE_PRIVATE).edit();
        edit.putString("name", "nsh");
        edit.putBoolean("gender", false);
        edit.putInt("age", 24);
        edit.apply();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        save();
    }

    private void save() {
        try {
            String text = ed.getText().toString();
            output = openFileOutput("data", Context.MODE_PRIVATE);
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(output));
            bufferedWriter.write(text);

            Toast.makeText(this, "Save sucess!!!", Toast.LENGTH_SHORT).show();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != bufferedWriter) {
                    bufferedWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String load() {
        try {
            input = openFileInput("data");

            bufferedReader = new BufferedReader(new InputStreamReader(input));

            stringBuilder = new StringBuilder();

            String line = null;
            while (null != (line = bufferedReader.readLine())) {
                stringBuilder.append(line);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != bufferedReader) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        return stringBuilder.toString();
    }
}
