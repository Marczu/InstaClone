package com.marcinmejner.instaclone.Login;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.marcinmejner.instaclone.R;
import com.marcinmejner.instaclone.Utils.FirebaseMethods;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";

    private Context context;
    private String email, username, password;
    private EditText mEmail, mUsername, mPassword;
    private TextView loadingPleaseWait;
    private Button buttonRegister;
    private ProgressBar mPrograssbar;

    //Firebase Auth
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseMethods firebaseMethods;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

    private String append = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Log.d(TAG, "onCreate: started");
        context = RegisterActivity.this;
        firebaseMethods = new FirebaseMethods(context);
        setupFirebaseAuth();
        initWidgets();
        init();



    }

    private void init(){
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = mEmail.getText().toString();
                username = mUsername.getText().toString();
                password = mPassword.getText().toString();
                Log.d(TAG, "onClick: wewnątrz buttona");

                if(checkInputs(email, username, password)){
                    mPrograssbar.setVisibility(View.VISIBLE);
                    loadingPleaseWait.setVisibility(View.VISIBLE);

                    firebaseMethods.registerNewEmail(email, password, username);

                }
            }


        });
    }

    private boolean checkInputs(String email, String username, String password){
        Log.d(TAG, "checkInputs: checking for null");
        if(email.equals("") || username.equals("") || password.equals("")){
            Toast.makeText(context, "All fields must be filled", Toast.LENGTH_SHORT).show();
            return false;
        }
            return true;
    }

    /*Inicjalizacja widgetów aktywności*/
    private void initWidgets(){
        buttonRegister = findViewById(R.id.btn_register);
        mPrograssbar = findViewById(R.id.progressBar);
        loadingPleaseWait = findViewById(R.id.pleaseWait);
        mEmail = findViewById(R.id.input_email);
        mPassword = findViewById(R.id.input_password);
        mUsername = findViewById(R.id.input_username);
        context = RegisterActivity.this;

        mPrograssbar.setVisibility(View.GONE);
        loadingPleaseWait.setVisibility(View.GONE);
    }

    private boolean isStringNull(String string) {
        if (string.equals("")) {
            return true;
        } else {
            return false;
        }
    }

    /*Setup Firebase*/

    private void setupFirebaseAuth() {
        Log.d(TAG, "setupFirebaseAuth: ");
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged: user signed_in" + user.getUid());

                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // sprawdzamy czy user jest juz w użyciu
                            if(firebaseMethods.checkIfUsernameExists(username, dataSnapshot)){
                                append = myRef.push().getKey().substring(3,10);
                                Log.d(TAG, "onDataChange: Username already exists, appending random string to name: " + append);
                            }
                            username = username + append;

                            //Dodawanie nowego usera do bazy

                            firebaseMethods.addNewUser(email, username, "", "", "");

                            Toast.makeText(context, "Signup successfull! sending verification email", Toast.LENGTH_LONG).show();


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                } else {
                    Log.d(TAG, "onAuthStateChanged: user signed_out");
                }
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthStateListener != null) {
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
    }
}
