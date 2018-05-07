package com.marcinmejner.instaclone.Profile;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.marcinmejner.instaclone.R;
import com.marcinmejner.instaclone.Utils.ViewPostFragment;
import com.marcinmejner.instaclone.models.Photo;

/**
 * Created by Marc on 15.03.2018.
 */

public class ProfileActivity extends AppCompatActivity implements ProfileFragment.OnGridImageSelectedListener {
    private static final String TAG = "ProfileActivity";

    @Override
    public void onGridImageSelected(Photo photo, int activityNumber) {
        Log.d(TAG, "onGridImageSelected: wybrano zdjecie z gridView " + photo.toString());

        ViewPostFragment fragment = new ViewPostFragment();
        Bundle args = new Bundle();
        args.putParcelable(getString(R.string.photo), photo);
        args.putInt(getString(R.string.activity_number), activityNumber);
        fragment.setArguments(args);
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(getString(R.string.view_post_fragment));
        transaction.commit();
    }
    
    public static final int ACTIVITY_NUM = 4;
    private static final int NUM_GRID_COLLUMNS = 3;

    private Context mContex = ProfileActivity.this;

    private ProgressBar progressBar;
    ImageView profilePhoto;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        init();
    }

    private void init(){
        com.orhanobut.logger.Logger.d("inflating" + getString(R.string.profile_fragment));
        Log.d(TAG, "init: hmmm");

        ProfileFragment profileFragment = new ProfileFragment();
        android.support.v4.app.FragmentTransaction transaction = ProfileActivity.this.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, profileFragment);
        transaction.addToBackStack(getString(R.string.profile_fragment));
        transaction.commit();
    }
}
