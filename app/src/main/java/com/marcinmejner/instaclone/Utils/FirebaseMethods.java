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
import com.marcinmejner.instaclone.models.UserSettings;

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

        if (mAuth.getCurrentUser() != null) {
            userID = mAuth.getCurrentUser().getUid();
        }
    }

    /**
     * uaktualnianie 'user_account_setting' node dla aktualnego usera
     *
     * @param displayName
     * @param website
     * @param description
     * @param phoneNumber
     */
    public void updateUserAccountSetting(String displayName, String website, String description, long phoneNumber) {
        Log.d(TAG, "updateUserAccountSetting: uaktualnianie user_account_settings");

        if (displayName != null) {
            myRef.child(mContex.getString(R.string.dbname_user_account_settings))
                    .child(userID)
                    .child(mContex.getString(R.string.field_display_name))
                    .setValue(displayName);
        }

        if (website != null) {
            myRef.child(mContex.getString(R.string.dbname_user_account_settings))
                    .child(userID)
                    .child(mContex.getString(R.string.field_website))
                    .setValue(website);
        }

        if (description != null) {
            myRef.child(mContex.getString(R.string.dbname_user_account_settings))
                    .child(userID)
                    .child(mContex.getString(R.string.field_description))
                    .setValue(description);
        }

        if (phoneNumber != 0) {
            myRef.child(mContex.getString(R.string.dbname_user_account_settings))
                    .child(userID)
                    .child(mContex.getString(R.string.field_phone_number))
                    .setValue(phoneNumber);

            myRef.child(mContex.getString(R.string.dbname_users))
                    .child(userID)
                    .child(mContex.getString(R.string.field_phone_number))
                    .setValue(phoneNumber);
        }
    }

    /**
     * Uaktualniamy username w user i user_account_settings
     *
     * @param username
     */
    public void updateUsername(String username) {
        Log.d(TAG, "updateUsername: uaktualniamy username na :" + username);
        myRef.child(mContex.getString(R.string.dbname_users))
                .child(userID)
                .child(mContex.getString(R.string.field_username))
                .setValue(username);

        myRef.child(mContex.getString(R.string.dbname_user_account_settings))
                .child(userID)
                .child(mContex.getString(R.string.field_username))
                .setValue(username);
    }

    /**
     * Uuaktualniamy email w user's node
     *
     * @param email
     */
    public void updateEmail(String email) {
        Log.d(TAG, "updateEmail: uaktualniamy email na :" + email);
        myRef.child(mContex.getString(R.string.dbname_users))
                .child(userID)
                .child(mContex.getString(R.string.field_email))
                .setValue(email);
    }

//    public boolean checkIfUsernameExists(String username, DataSnapshot datasnapshot) {
//        Log.d(TAG, "checkIfUsernameExists: sprawdzamy czy " + username + "juz istnieje");
//
//        User user = new User();
//
//        for (DataSnapshot ds : datasnapshot.child(userID).getChildren()) {
//            Log.d(TAG, "checkIfUsernameExists: datasnapshot : " + ds);
//            user.setUsername(ds.getValue(User.class).getUsername());
//
//
//            if (StringManipulation.expandUsername(user.getUsername()).equals(username)) {
//                Log.d(TAG, "checkIfUsernameExists: found a match: " + user.getUsername());
//                return true;
//            }
//        }
//
//        return false;
//    }

    /**
     * Register new email and password to firebase Auth
     *
     * @param email
     * @param password
     * @param username
     */
    public void registerNewEmail(final String email, String password, final String username) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //wysyłanie veryfikującego emaila
                            sendVerificationEmail();

                            // Sign in success, update UI with the signed-in user's information
                            userID = mAuth.getCurrentUser().getUid();
                            Log.d(TAG, "createUserWithEmail:success: userID = " + userID);


                        } else {
                            Toast.makeText(mContex, "Failed to register", Toast.LENGTH_SHORT).show();
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());

                        }

                    }
                });
    }

    /*Veryfikacja poprzez Email*/
    public void sendVerificationEmail() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                            } else {
                                Toast.makeText(mContex, "couldn't send verification email", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }

    /**
     * Dodawanie nowego usera do bazy
     * Dodawanie informacji o user_account_settings
     *
     * @param email
     * @param username
     * @param desctiption
     * @param website
     * @param profile_photo
     */
    public void addNewUser(String email, String username, String desctiption, String website, String profile_photo) {
        User user = new User(userID, 1, email, StringManipulation.condenseUsername(username));

        myRef.child(mContex.getString(R.string.dbname_users))
                .child(userID)
                .setValue(user);

        UserAccountSettings settings = new UserAccountSettings(
                desctiption,
                username,
                0,
                0,
                0,
                profile_photo,
                StringManipulation.condenseUsername(username),
                website);

        myRef.child(mContex.getString(R.string.dbname_user_account_settings))
                .child(userID)
                .setValue(settings);

    }

    /**
     * Odbieranie Account Setting dla obecnie zalogowanego Usera
     * Database: user_account_settings node
     *
     * @param dataSnapshot
     * @return
     */
    public UserSettings getUserSettings(DataSnapshot dataSnapshot) {
        Log.d(TAG, "getUserAccountSettings: odbieranie userAccountSettings z firebase");

        UserAccountSettings settings = new UserAccountSettings();
        User user = new User();

        for (DataSnapshot ds : dataSnapshot.getChildren()) {


            /*user_account_setting node*/
            if (ds.getKey().equals(mContex.getString(R.string.dbname_user_account_settings))) {
                Log.d(TAG, "getUserAccountSettings: dataSnapshot: " + ds);

                try {
                    settings.setDisplay_name(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getDisplay_name()
                    );
                    settings.setUsername(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getUsername()
                    );
                    settings.setWebsite(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getWebsite()
                    );
                    settings.setDescription(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getDescription()
                    );
                    settings.setProfile_photo(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getProfile_photo()
                    );
                    settings.setPosts(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getPosts()
                    );
                    settings.setFollowing(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getFollowing()
                    );
                    settings.setFollowers(
                            ds.child(userID)
                                    .getValue(UserAccountSettings.class)
                                    .getFollowers()
                    );

                    Log.d(TAG, "getUserAccountSettings: Otrzymane informacje z user_account_settings: " + settings.toString());
                } catch (NullPointerException e) {
                    Log.e(TAG, "getUserAccountSettings: NullPoincerException: " + e.getMessage());
                }

            }
            /*users node*/
            if (ds.getKey().equals(mContex.getString(R.string.dbname_users))) {
                Log.d(TAG, "getUserAccountSettings: dataSnapshot: " + ds);
                user.setUsername(
                        ds.child(userID)
                                .getValue(User.class)
                                .getUsername()
                );
                user.setEmail(
                        ds.child(userID)
                                .getValue(User.class)
                                .getEmail()
                );
                user.setPhone_number(
                        ds.child(userID)
                                .getValue(User.class)
                                .getPhone_number()
                );
                user.setUser_id(
                        ds.child(userID)
                                .getValue(User.class)
                                .getUser_id()
                );
                Log.d(TAG, "getUserAccountSettings: Otrzymane informacje z user: " + user.toString());


            }


        }
        return new UserSettings(user, settings);
    }
}
