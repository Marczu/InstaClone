package com.marcinmejner.instaclone.Search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.marcinmejner.instaclone.Profile.ProfileActivity;
import com.marcinmejner.instaclone.R;
import com.marcinmejner.instaclone.Utils.BottomNavigationViewHelper;
import com.marcinmejner.instaclone.Utils.UserListAdapter;
import com.marcinmejner.instaclone.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
    private UserListAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Log.d(TAG, "onCreate: started");
        mSearchParam = findViewById(R.id.search);
        mListView = findViewById(R.id.listView);

        hideSoftKeyboard();
        setupNavigationNavigationView();
        initTextListener();
    }

    private void initTextListener(){
        Log.d(TAG, "initTextListener: initializing");

        mUserList = new ArrayList<>();

        mSearchParam.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String text = mSearchParam.getText().toString();
                searchForMatch(text);
            }
        });
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
                        updateUserList();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private void updateUserList(){
        Log.d(TAG, "updateUserList: updatning users list");
        mAdapter = new UserListAdapter(SearchActivity.this, R.layout.layout_user_listitem, mUserList);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick: selected user: " + mUserList.get(position).toString());

                //navigate to user profile
                Intent intent = new Intent(SearchActivity.this, ProfileActivity.class);
                intent.putExtra(getString(R.string.calling_activity), getString(R.string.search_activity));
                intent.putExtra(getString(R.string.intent_user), mUserList.get(position));
                startActivity(intent);
            }
        });
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
