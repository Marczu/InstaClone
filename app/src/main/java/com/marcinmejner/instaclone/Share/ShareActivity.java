package com.marcinmejner.instaclone.Share;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.marcinmejner.instaclone.R;
import com.marcinmejner.instaclone.Utils.BottomNavigationViewHelper;
import com.marcinmejner.instaclone.Utils.Permissions;
import com.marcinmejner.instaclone.Utils.SectionPagerAdapter;

/**
 * Created by Marc on 15.03.2018.
 */

public class ShareActivity extends AppCompatActivity {
    private static final String TAG = "ShareActivity";

    //Constants
    private static final int ACTIVITY_NUM = 2;
    public static final int VERIFY_PERMISSIONS_REQUEST = 1;

    private ViewPager mViewPager;



    private Context mContex = ShareActivity.this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        Log.d(TAG, "onCreate: started");

        if (checkPermissionsArray(Permissions.PERMISSIONS)) {
            setupViewPager();
        } else {
            veryfiPermissions(Permissions.PERMISSIONS);
        }
    }

    /**
     * Zwracamy aktyalny numer tabu:
     * 0 = GalleryFragment
     * 1 = PhotoFragment
     * @return
     */
    public int getCurrentTabNumber(){
        return mViewPager.getCurrentItem();
    }

    private void setupViewPager(){
        SectionPagerAdapter adapter = new SectionPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new GalleryFragment());
        adapter.addFragment(new PhotoFragment());

        mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.tabsBottom);
        tabLayout.setupWithViewPager(mViewPager);

        tabLayout.getTabAt(0).setText(getString(R.string.gallery));
        tabLayout.getTabAt(1).setText(getString(R.string.photo));
    }

    /**
     * Weryfikujemy wszystkie pozwolenia
     * @param permissions
     */
    private void veryfiPermissions(String[] permissions) {
        Log.d(TAG, "veryfiPermissions: weryfikujemy pozwolenia");

        ActivityCompat.requestPermissions(
                ShareActivity.this,
                permissions,
                VERIFY_PERMISSIONS_REQUEST
        );
    }

    /**
     * Sprawdzamy pozwolenia dla tablicy pozwoleń
     *
     * @param permissions
     * @return
     */
    private boolean checkPermissionsArray(String[] permissions) {
        Log.d(TAG, "checkPermissionsArray: sprawdzanie tablicy pozwoleń");

        for (int i = 0; i < permissions.length; i++) {
            String check = permissions[i];

            if (!checkPermisions(check)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Sprawdzamy czy pojedyńcze pozwolenie zostało udzielone
     * @param permission
     * @return
     */
    public boolean checkPermisions(String permission) {
        Log.d(TAG, "checkPermisions: sprawdzamy pozwolenie: " + permission);

        int permissionRequest = ActivityCompat.checkSelfPermission(ShareActivity.this, permission);

        if (permissionRequest != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "checkPermisions: Pozwolenie nie udzielone dla: " + permission);
            return false;
        } else {
            Log.d(TAG, "checkPermisions: Pozwolenie zostało udzielone dla: " + permission);
            return true;
        }
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
