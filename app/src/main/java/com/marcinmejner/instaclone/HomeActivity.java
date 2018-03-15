package com.marcinmejner.instaclone;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.marcinmejner.instaclone.Utils.BottomNavigationViewHelper;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";
    public static final int ACTIVITY_NUM = 0;

    private Context mContex = HomeActivity.this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setupNavigationNavigationView();
    }

    private void setupNavigationNavigationView(){
        BottomNavigationViewEx bottomNavigationViewEx =  findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setup(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContex, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }
}
