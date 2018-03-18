package com.marcinmejner.instaclone.Profile;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.marcinmejner.instaclone.R;
import com.marcinmejner.instaclone.Utils.SectionStagePagerAdapter;

import java.util.ArrayList;

/**
 * Created by Marc on 16.03.2018.
 */

public class AccountSettingsActivity extends AppCompatActivity {
    private static final String TAG = "AccountSettingsActivity";

    private Context mContex;

    private SectionStagePagerAdapter pagerAdapter;
    private ViewPager viewPager;
    private RelativeLayout relativeLayout;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accountsettings);
        Log.d(TAG, "onCreate: started");
        mContex = AccountSettingsActivity.this;
        viewPager = findViewById(R.id.container);
        relativeLayout = findViewById(R.id.relLayout1);

        setupSettingList();

        setupFragments();

        //ustawianie BackArrow
        ImageView backArrow = findViewById(R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void setupFragments(){
        pagerAdapter = new SectionStagePagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(new EditProfileFragment(), getString(R.string.edit_profile_fragment));
        pagerAdapter.addFragment(new SignOutFragment(), getString(R.string.sign_out_fragment));
    }

    private void setViewPager(int fragmentNumber){
        relativeLayout.setVisibility(View.GONE);
        Log.d(TAG, "setViewPager: nagigating forgragment number: " + fragmentNumber);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(fragmentNumber);

    }

    private void setupSettingList(){
        Log.d(TAG, "setupSettingList: inicjuje 'accountSetting' list");
        ListView listView = findViewById(R.id.lvAccountSettings);

        ArrayList<String> options = new ArrayList<>();
        options.add(getString(R.string.edit_profile_fragment));
        options.add(getString(R.string.sign_out_fragment));

        ArrayAdapter adapter = new ArrayAdapter(mContex, android.R.layout.simple_list_item_1, options);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG, "onItemClick: navigating to fragment positiong " + i);
                setViewPager(i);
            }
        });
    }
}
