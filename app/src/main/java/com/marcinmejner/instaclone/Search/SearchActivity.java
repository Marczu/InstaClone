package com.marcinmejner.instaclone.Search;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.marcinmejner.instaclone.R;
import com.marcinmejner.instaclone.Utils.BottomNavigationViewHelper;
import com.marcinmejner.instaclone.models.User;

import java.util.List;

/**
 * Created by Marc on 15.03.2018.
 */

public class SearchActivity extends AppCompatActivity {
    private static final String TAG = "SearchActivity";
    public static final int ACTIVITY_NUM = 1;

    private Context mContex = SearchActivity.this;

    //widgets
    private EditText mSearchParam;
    private ListView mListView;


    //vars
    private List<User> mUserList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Log.d(TAG, "onCreate: started");

        hideSoftKeyboard();

        setupNavigationNavigationView();
    }

    private void searchForMatch(String keyword){
        Log.d(TAG, "searchForMatch: searching for a match");
        mUserList.clear();
        //update user list
        if(keyword.length() == 0){

        }else{
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
            Query query = reference
                    .child(getString(R.string.dbname_users))
                    .orderByChild(getString(R.string.field_username))
                    .equalTo(keyword);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                        Log.d(TAG, "onDataChange: found user" + singleSnapshot.getValue(User.class).toString());

                        mUserList.add(singleSnapshot.getValue(User.class));
                        //update user list view
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private void hideSoftKeyboard(){
        if(getCurrentFocus() != null){
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    private void setupNavigationNavigationView() {
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setup(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContex, this, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }
}
