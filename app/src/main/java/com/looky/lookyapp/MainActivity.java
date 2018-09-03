package com.looky.lookyapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.looky.helper.SQLiteHandler;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private final int FRAGMENT_MAIN = 1;
    private final int FRAGMENT_LIKE = 2;
    private final int FRAGMENT_CHART = 3;
    private final int FRAGMENT_SETTING = 4;

    private int current_fragment;

    private ImageView bt_tab_main;
    private ImageButton bt_tab_like;
    private ImageButton bt_tab_chart;
    private ImageButton bt_tab_setting;

    public static SQLiteHandler db;
    public static HashMap<String, String> user;
    public static HashMap<String, String> tutorial;

    public static FloatingActionButton fab;

    public static String previousCategory;
    public static String currentCategory;
    public static String previousSubCategory;
    public static String currentSubCategory;

    public static MenuItem menuFilter;
    public static MenuItem menuDelete;

    public static final int WEBVIEW_REQUEST = 1;

    public static Activity activity;

    public static String mainTutorial;
    public static String likeTutorial;
    public static String chartTutorial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        Drawable logo = getResources().getDrawable(R.drawable.looky);
        getSupportActionBar().setLogo(logo);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        db = new SQLiteHandler(getApplicationContext());
        user = db.getUserDetails();
        tutorial = db.getTutorialDetails();

        mainTutorial = tutorial.get("tutorial_main");
        likeTutorial = tutorial.get("tutorial_like");
        chartTutorial = tutorial.get("tutorial_chart");

        if(user.get("gender").equals("2")){
            Intent genderIntent = new Intent(getApplicationContext(), GenderActivity.class);
            startActivity(genderIntent);
            finish();
        }

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setImageResource(R.drawable.cart);
        currentCategory = "모두보기";
        previousCategory = "모두보기";
        currentSubCategory = "모두보기";
        previousSubCategory = "모두보기";

        bt_tab_main = (ImageView)findViewById(R.id.bt_tab_main);
        bt_tab_like = (ImageButton)findViewById(R.id.bt_tab_like);
        bt_tab_chart = (ImageButton)findViewById(R.id.bt_tab_chart);
        bt_tab_setting = (ImageButton)findViewById(R.id.bt_tab_setting);


        bt_tab_main.setOnClickListener(this);
        bt_tab_like.setOnClickListener(this);
        bt_tab_chart.setOnClickListener(this);
        bt_tab_setting.setOnClickListener(this);

        current_fragment = 1;

        callFragment(FRAGMENT_MAIN);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_tab_main :
                callFragment(FRAGMENT_MAIN);
                current_fragment = 1;
                break;
            case R.id.bt_tab_like :
                callFragment(FRAGMENT_LIKE);
                current_fragment = 2;
                break;
            case R.id.bt_tab_chart :
                callFragment(FRAGMENT_CHART);
                current_fragment = 3;
                break;
            case R.id.bt_tab_setting :
                callFragment(FRAGMENT_SETTING);
                current_fragment = 4;
                break;

        }
    }

    private void callFragment(int frament_no){

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        setTabTint(frament_no);

        switch (frament_no){
            case 1:
                MainFragment fragmentMain = new MainFragment();
                transaction.replace(R.id.fragment_container, fragmentMain);
                transaction.commit();
                break;

            case 2:
                LikeFragment likeFragment= new LikeFragment();
                transaction.replace(R.id.fragment_container, likeFragment);
                transaction.commit();
                break;

            case 3:
                ChartFragment chartFragment= new ChartFragment();
                transaction.replace(R.id.fragment_container, chartFragment);
                transaction.commit();
                break;

            case 4:
                SettingFragment settingFragment= new SettingFragment();
                transaction.replace(R.id.fragment_container, settingFragment);
                transaction.commit();
                break;
        }

    }

    private void setTabTint(int view) {
        switch(view) {
            case 1:
                bt_tab_main.setColorFilter(getResources().getColor(R.color.black));
                bt_tab_like.setColorFilter(getResources().getColor(R.color.darkgray));
                bt_tab_chart.setColorFilter(getResources().getColor(R.color.darkgray));
                bt_tab_setting.setColorFilter(getResources().getColor(R.color.darkgray));
                break;
            case 2:
                bt_tab_main.setColorFilter(getResources().getColor(R.color.darkgray));
                bt_tab_like.setColorFilter(getResources().getColor(R.color.black));
                bt_tab_chart.setColorFilter(getResources().getColor(R.color.darkgray));
                bt_tab_setting.setColorFilter(getResources().getColor(R.color.darkgray));
                break;
            case 3:
                bt_tab_main.setColorFilter(getResources().getColor(R.color.darkgray));
                bt_tab_like.setColorFilter(getResources().getColor(R.color.darkgray));
                bt_tab_chart.setColorFilter(getResources().getColor(R.color.black));
                bt_tab_setting.setColorFilter(getResources().getColor(R.color.darkgray));
                break;
            case 4:
                bt_tab_main.setColorFilter(getResources().getColor(R.color.darkgray));
                bt_tab_like.setColorFilter(getResources().getColor(R.color.darkgray));
                bt_tab_chart.setColorFilter(getResources().getColor(R.color.darkgray));
                bt_tab_setting.setColorFilter(getResources().getColor(R.color.black));
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);

        menuFilter = menu.findItem(R.id.menu_filter);
        menuFilter.setIcon(R.drawable.ic_filter);
        if(current_fragment == 4) menuFilter.setVisible(false);

        menuDelete = menu.findItem(R.id.menu_delete);
        menuDelete.setIcon(R.drawable.ic_trash);
        menuDelete.setVisible(false);

        return true;

    }

}
