package com.marcinmejner.instaclone.Login;

import android.content.Context;
import android.content.Intent;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.marcinmejner.instaclone.Home.HomeActivity;
import com.marcinmejner.instaclone.Likes.LikesActivity;
import com.marcinmejner.instaclone.R;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";



    //Firebase Auth
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private Context context;
    ProgressBar progressBar;

    private EditText email, password;
    private TextView pleaseWait;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.d(TAG, "onCreate: start");
        Logger.addLogAdapter(new AndroidLogAdapter());


        progressBar = findViewById(R.id.progressBar);
        pleaseWait = findViewById(R.id.please_wait);
        email = findViewById(R.id.input_email);
        password = findViewById(R.id.input_password);
        context = LoginActivity.this;

        progressBar.setVisibility(View.GONE);
        pleaseWait.setVisibility(View.GONE);


        setupFirebaseAuth();
        init();
    }

    private boolean isStringNull(String string) {
        if (string.equals("")) {
            return true;
        } else {
            return false;
        }
    }

      /*--------------------------------------------------------------------------------
        ------------------------------FIREBASE -----------------------------------------
        --------------------------------------------------------------------------------*/

    private void init() {

        /*Ustawienia przycisku logowania*/
        Button buttonLogin = findViewById(R.id.btn_login);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Proba logowania");

                String emailText = email.getText().toString();
                String passwordText = password.getText().toString();

                if (isStringNull(emailText) || isStringNull(passwordText)) {
                    Toast.makeText(context, "You must fill all the fields", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "POZIOM FAIL");
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    pleaseWait.setVisibility(View.VISIBLE);


                    mAuth.signInWithEmailAndPassword(emailText, passwordText)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        try{

                                            if(user.isEmailVerified()){
                                                /* Jeśli udała sie weryfikacja i logowanie, przenosimy sie do Home Activity */
                                                Logger.d("Email był zweryfikowany, mozemy przejsc do homeActivity");
                                                startActivity(new Intent(context, HomeActivity.class));
                                                finish();
                                            }else{
                                                Toast.makeText(context, "Email is not verified\nPlease chceck your Email inbox", Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.GONE);
                                                pleaseWait.setVisibility(View.GONE);
                                                mAuth.signOut();
                                            }

                                        }catch (NullPointerException e){
                                            Log.d(TAG, "onComplete: NullPointerException " + e.getMessage());
                                        }

                                    } else {
                                        /* Jeśli weryfikacja i logowanie nie powiedzie się, wyświetlamy wiadomość dla usera */
                                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                        pleaseWait.setVisibility(View.GONE);

                                    }
                                }
                            });
                }
            }
        });
        /*
        *      Rejestrowanie nowego usera
        * */
        TextView linkSignUp = findViewById(R.id.link_signup);
        linkSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: navigating to register screen");
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }

    /*Setup Firebase*/

    private void setupFirebaseAuth() {
        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged: user signed_in" + user.getUid());
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
    public void onStop() {
        super.onStop();
        if (mAuthStateListener != null) {
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
    }


}
