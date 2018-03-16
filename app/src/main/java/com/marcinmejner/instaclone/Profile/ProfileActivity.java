package com.marcinmejner.instaclone.Profile;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toolbar;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.marcinmejner.instaclone.R;
import com.marcinmejner.instaclone.Utils.BottomNavigationViewHelper;

/**
 * Created by Marc on 15.03.2018.
 */

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "ProfileActivity";
    public static final int ACTIVITY_NUM = 4;


    private Context mContex = ProfileActivity.this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Log.d(TAG, "onCreate: started");

        setupNavigationNavigationView();
        setupToolbar();
    }

    private void setupToolbar() {
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.profileToolbar);
        setSupportActionBar(toolbar);

        toolbar.setOnMenuItemClickListener(new android.support.v7.widget.Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Log.d(TAG, "onMenuItemClick: clicked menu" + item);

                switch (item.getItemId()) {
                    case R.id.profileMenu:
                        Log.d(TAG, "onMenuItemClick: navigating to profile preferences");
                }
                return false;
            }
        });
    }

    private void setupNavigationNavigationView() {
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setup(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContex, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu, menu);

        return true;
    }
}
