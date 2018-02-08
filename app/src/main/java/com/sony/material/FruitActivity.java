package com.sony.material;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sony.www.demo.R;

public class FruitActivity extends AppCompatActivity {

    public static final String FRUIT_NAME = "fruit_name";
    public static final String FRUIT_IMAGE_ID = "fruit_image_id";
    private ImageView fruitImage;
    private TextView fruitContentText;
    private Toolbar toolbarActivity;
    private CollapsingToolbarLayout collapsingToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fruit);

        Intent detailsIntent = getIntent();
        String fruit_name = detailsIntent.getStringExtra(FRUIT_NAME);
        int fruit_image_id = detailsIntent.getIntExtra(FRUIT_IMAGE_ID, 0);

        toolbarActivity = (Toolbar) findViewById(R.id.toolbar_activity);
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);

        fruitImage = (ImageView) findViewById(R.id.fruit_image_activity);
        fruitContentText = (TextView) findViewById(R.id.fruit_content_text);

        setSupportActionBar(toolbarActivity);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        collapsingToolbar.setTitle(fruit_name);

        Glide.with(this).load(fruit_image_id).into(fruitImage);
        String detailsContent = generateFruitContent(fruit_name);
        fruitContentText.setText(detailsContent);
    }

    private String generateFruitContent(String fruit_name) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 200; i++) {
            sb.append(i + "---This is " + fruit_name + "\n");
        }
        return sb.toString();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return true;
    }
}
