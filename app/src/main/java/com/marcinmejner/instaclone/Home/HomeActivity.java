package com.marcinmejner.instaclone.Home;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.marcinmejner.instaclone.Login.LoginActivity;
import com.marcinmejner.instaclone.R;
import com.marcinmejner.instaclone.Utils.BottomNavigationViewHelper;
import com.marcinmejner.instaclone.Utils.SectionPagerAdapter;
import com.marcinmejner.instaclone.Utils.UniversalImageLoader;
import com.nostra13.universalimageloader.core.ImageLoader;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";
    public static final int ACTIVITY_NUM = 0;

    private Context mContex = HomeActivity.this;

    //Firebase Auth
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        setupFirebaseAuth();

        initImageLoader();
        setupNavigationNavigationView();
        setupViewPager();
    }



    private void initImageLoader(){
        UniversalImageLoader universalImageLoader = new UniversalImageLoader(mContex);
        ImageLoader.getInstance().init(universalImageLoader.getConfig());

    }

    /*
    * Do tworzenia tabów Camera, Home, Message
    * */
    private void setupViewPager(){
        SectionPagerAdapter adapter = new SectionPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new CameraFragment());
        adapter.addFragment(new HomeFragment());
        adapter.addFragment(new MessagesFragment());

        ViewPager viewPager = findViewById(R.id.container);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_camera);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_action_name);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_arrow);
    }

    private void setupNavigationNavigationView(){
        BottomNavigationViewEx bottomNavigationViewEx =  findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setup(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContex, this, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

    /*
        ------------------------------FIREBASE -----------------------------------------
    */

    private void checkCurrentUser(FirebaseUser user) {
        Log.d(TAG, "checkCurrentUser: checking if user is logged in");
        if(user==null){
            Intent intent = new Intent(mContex, LoginActivity.class);
            startActivity(intent);
        }




    }
    private void setupFirebaseAuth(){
        mAuth = FirebaseAuth.getInstance();
//        mAuth.signOut();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                checkCurrentUser(user);

                if(user!=null){
                    Log.d(TAG, "user signed_in:  " + user.getUid());
                }else{
                    Log.d(TAG, "onAuthStateChanged: user signed_out");
                }
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
        checkCurrentUser(mAuth.getCurrentUser());
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mAuthStateListener != null){
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

}
