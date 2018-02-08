package com.sony.material;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.sony.www.demo.R;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MaterialActivity extends AppCompatActivity {

    private String fruitNameList[] = {"Apple", "Banana", "Cherry", "Grape", "Mango", "Orange", "Pear", "Pineapple", "Strawberry", "Watermelon",};
    private int fruitImageResList[] = {R.drawable.apple_pic, R.drawable.banana_pic, R.drawable.cherry_pic, R.drawable.grape_pic, R.drawable.mango_pic, R.drawable.orange_pic, R.drawable.pear_pic, R.drawable.pineapple_pic, R.drawable.strawberry_pic, R.drawable.watermelon_pic,};

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private FloatingActionButton floatingActionButton;
    private RecyclerView recyclerView;
    private List<Fruit> fruitList = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;
    private FruitAdapter fruitAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);


        setSupportActionBar(toolbar);

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }


//        默认选中指定条目
        navigationView.setCheckedItem(R.id.nav_call);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerLayout.closeDrawers();
                return true;
            }
        });


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "Data deleted", Snackbar.LENGTH_SHORT).setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MaterialActivity.this, "Data restored", Toast.LENGTH_SHORT).show();
                    }
                }).show();
            }
        });


        initFruits();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        fruitAdapter = new FruitAdapter(fruitList);
        recyclerView.setAdapter(fruitAdapter);


        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshFruits();
            }
        });


    }

    private void refreshFruits() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MaterialActivity.this, "Refresh Success", Toast.LENGTH_SHORT).show();
                        initFruits();
                        fruitAdapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    private void initFruits() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < fruitNameList.length; j++) {
                Fruit fruit = new Fruit(fruitNameList[j], fruitImageResList[j]);
                fruitList.add(fruit);
            }
        }
    }

    /**
     * 加载菜单资源文件 创建菜单
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    /**
     * toolbar上菜单点击的事件
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.tb_add:
                Toast.makeText(this, "You clicked Add", Toast.LENGTH_SHORT).show();
                fruitAdapter.addData(4);
                break;
            case R.id.tb_delete:
                Toast.makeText(this, "You clicked Delete", Toast.LENGTH_SHORT).show();
                fruitAdapter.removeData(4);
                break;
            case R.id.tb_settings:
                Toast.makeText(this, "You clicked Settings", Toast.LENGTH_SHORT).show();
                break;
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                Toast.makeText(this, "Open DrawerLayout", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return true;
    }


    @Override
    public boolean onPreparePanel(int featureId, View view, Menu menu) {
        String className = "MenuBuilder";
        if (menu != null) {
            if (className.equals(menu.getClass().getSimpleName())) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);

                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }
}
