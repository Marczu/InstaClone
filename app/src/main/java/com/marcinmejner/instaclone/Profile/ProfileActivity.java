package com.marcinmejner.instaclone.Profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toolbar;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.marcinmejner.instaclone.R;
import com.marcinmejner.instaclone.Utils.BottomNavigationViewHelper;
import com.marcinmejner.instaclone.Utils.GridImageAdapter;
import com.marcinmejner.instaclone.Utils.UniversalImageLoader;

import java.util.ArrayList;

/**
 * Created by Marc on 15.03.2018.
 */

public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "ProfileActivity";
    public static final int ACTIVITY_NUM = 4;
    private static final int NUM_GRID_COLLUMNS = 3;

    private Context mContex = ProfileActivity.this;


    private ProgressBar progressBar;
    ImageView profilePhoto;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Log.d(TAG, "onCreate: started");


        setupNavigationNavigationView();
        setupToolbar();
        setupActivityWidgets();
        setProfileImage();
        tempGridSetup();

    }

    private void tempGridSetup(){
        ArrayList<String> imgURLs = new ArrayList<>();
        imgURLs.add("https://upload.wikimedia.org/wikipedia/commons/5/5d/Tori_Amos_smiling_at_one_of_her_fans..jpg");
        imgURLs.add("https://static.stereogum.com/uploads/2017/10/GettyImages-863061362-1509044208-640x443.jpg");
        imgURLs.add("https://irom.files.wordpress.com/2014/07/tori-amos-stylish-at-greek.jpg");
        imgURLs.add("http://2.bp.blogspot.com/_gIsmEby1LjU/TFrkNSlojdI/AAAAAAAABJw/lUlLnWagx7U/s1600/Tori+Amos.jpg");
        imgURLs.add("https://i.pinimg.com/originals/bf/c1/59/bfc159a4b0de11cd413ac3140538b75e.jpg");
        imgURLs.add("https://i.pinimg.com/originals/96/a9/37/96a937323867168aa3c0560b2545e001.jpg");
        imgURLs.add("http://jazzsoul.pl/images//2013/11/tori-amos.jpg");
        imgURLs.add("http://images.nymag.com/images/2/daily/2009/11/20091111_toriamos_250x375.jpg");
        imgURLs.add("https://d-pt.ppstatic.pl/kadry/k/r/1/17/76/5395c801c09e0_o,size,250x400,q,71,h,7c0d8d.jpg");
        imgURLs.add("http://www.contactmusic.com/images/press/tori-amos-unrepentant-geraldines-2014.jpg");
        imgURLs.add("http://images.nymag.com/images/2/daily/2009/11/20091111_toriamos_250x375.jpg");
        imgURLs.add("http://coolspotters.com/files/photos/34942/tori-amos-profile.jpg");
        imgURLs.add("https://upload.wikimedia.org/wikipedia/commons/5/5d/Tori_Amos_smiling_at_one_of_her_fans..jpg");

        setupImageGrid(imgURLs);
    }

    private void setupImageGrid(ArrayList<String> imageURLs){
        GridView gridView = findViewById(R.id.gridView);

        int gridWidth = getResources().getDisplayMetrics().widthPixels;
        int imageWidth = gridWidth/NUM_GRID_COLLUMNS;
        gridView.setColumnWidth(imageWidth);

        GridImageAdapter gridImageAdapter = new GridImageAdapter(mContex, R.layout.layout_grid_imageview, "", imageURLs);
        gridView.setAdapter(gridImageAdapter);


    }

    private void setProfileImage() {
        Log.d(TAG, "setProfileImage: setting profile photo");
        String imageURL = "https://www.famousbirthdays.com/headshots/tori-amos-2.jpg";
        UniversalImageLoader.setImage(imageURL, profilePhoto, progressBar, "");


    }

    private void setupActivityWidgets() {
        progressBar = findViewById(R.id.profileProgressBar);
        progressBar.setVisibility(View.GONE);
        profilePhoto = findViewById(R.id.profile_photo);

    }

    private void setupToolbar() {
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.profileToolbar);
        setSupportActionBar(toolbar);

        ImageView profileMenu = findViewById(R.id.profileMenu);
        profileMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: navigating to account settings");
                Intent intent = new Intent(mContex, AccountSettingsActivity.class);
                startActivity(intent);
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


}
