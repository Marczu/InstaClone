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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.marcinmejner.instaclone.R;
import com.marcinmejner.instaclone.models.User;
import com.marcinmejner.instaclone.models.UserAccountSettings;

public class FirebaseMethods {
    private static final String TAG = "FirebaseMethods";

    //Firebase Auth
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private String userID;

    private Context mContex;

    public FirebaseMethods(Context mContex) {
        this.mContex = mContex;
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null){
            userID = mAuth.getCurrentUser().getUid();
        }
    }

    public boolean checkIfUsernameExists(String username, DataSnapshot datasnapshot){
        Log.d(TAG, "checkIfUsernameExists: sprawdzamy czy " + username + "juz istnieje");

        User user = new User();

        for (DataSnapshot ds : datasnapshot.child(userID).getChildren()) {
            Log.d(TAG, "checkIfUsernameExists: datasnapshot : " + ds);
            user.setUsername(ds.getValue(User.class).getUsername());
        }

        if(StringManipulation.expandUsername(user.getUsername()).equals(username)){
            Log.d(TAG, "checkIfUsernameExists: found a match: " + user.getUsername() );
            return true;
        }

        return false;
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

    /**
     * Dodawanie nowego usera do bazy
     * @param email
     * @param username
     * @param desctiption
     * @param website
     * @param profile_photo
     */
    public void addNewUser(String email, String username, String desctiption, String website, String profile_photo){
        User user = new User(userID, 3444801, email, StringManipulation.condenseUsername(username));

        myRef.child(mContex.getString(R.string.dbname_users))
                .child(userID)
                .setValue(user);

        UserAccountSettings settings = new UserAccountSettings(desctiption, username, 0, 0, 0, profile_photo, username, website);

        myRef.child(mContex.getString(R.string.dbname_user_account_settings))
                .child(userID)
                .setValue(settings);

    }
}
