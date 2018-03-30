package com.marcinmejner.instaclone.Utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseMethods {
    private static final String TAG = "FirebaseMethods";

    //Firebase Auth
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private String userID;

    private Context mContex;

    public FirebaseMethods(Context mContex) {
        this.mContex = mContex;
        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null){
            userID = mAuth.getCurrentUser().getUid();
        }
    }

    /**
     * Register new email and password to firebase Auth
     * @param email
     * @param password
     * @param username
     */
    public void registerNewEmail(final String email, String password, final String username ){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            userID = mAuth.getCurrentUser().getUid();
                            Log.d(TAG, "createUserWithEmail:success: userID = " + userID);



                        } else {
                            Toast.makeText(mContex, "Failed to register", Toast.LENGTH_SHORT).show();
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());


                        }

                        // ...
                    }
                });
    }
}
