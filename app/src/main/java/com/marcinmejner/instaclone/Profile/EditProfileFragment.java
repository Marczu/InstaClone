package com.marcinmejner.instaclone.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.marcinmejner.instaclone.R;
import com.marcinmejner.instaclone.Share.ShareActivity;
import com.marcinmejner.instaclone.Utils.FirebaseMethods;
import com.marcinmejner.instaclone.Utils.UniversalImageLoader;
import com.marcinmejner.instaclone.dialogs.ConfirmPasswordDialog;
import com.marcinmejner.instaclone.models.User;
import com.marcinmejner.instaclone.models.UserAccountSettings;
import com.marcinmejner.instaclone.models.UserSettings;
import com.nostra13.universalimageloader.core.ImageLoader;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Marc on 18.03.2018.
 */

public class EditProfileFragment extends Fragment implements ConfirmPasswordDialog.OnConfirmPasswordListener {
    private static final String TAG = "EditProfileFragment";

    //Firebase Auth
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseMethods mFirebaseMethods;
    private String userID;

    //EditProfile Fragment Widgets
    private EditText mDisplayName, mUsername, mWebsite, mDescription, mEmail, mPhoneNumber;
    private TextView mChangeProfilePhoto;
    private CircleImageView mProfilePhoto;

    //vars
    private UserSettings mUserSetting;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_editprofile, container, false);
        mProfilePhoto = view.findViewById(R.id.profile_photo);
        mDisplayName = view.findViewById(R.id.display_name);
        mUsername = view.findViewById(R.id.username);
        mWebsite = view.findViewById(R.id.website);
        mDescription = view.findViewById(R.id.description);
        mEmail = view.findViewById(R.id.email);
        mPhoneNumber = view.findViewById(R.id.phoneNumber);
        mChangeProfilePhoto = view.findViewById(R.id.changeProfilePhoto);
        mFirebaseMethods = new FirebaseMethods(getActivity());


        setupFirebaseAuth();

        //ustawienie backarrow
        ImageView backArrow = view.findViewById(R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        ImageView checkmark = view.findViewById(R.id.saveChanges);
        checkmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: Proba zapisania zmian");
                saveProfileSettings();
            }
        });
        return view;
    }

    /*
     *Obieramy dane z widgetów i zapisujemy je w bazie danych
     *Zanim tego dokonamy sprawdzamy czy username jest unikalny
     * */
    private void saveProfileSettings() {
        final String displayName = mDisplayName.getText().toString();
        final String userName = mUsername.getText().toString();
        final String website = mWebsite.getText().toString();
        final String description = mDescription.getText().toString();
        final String email = mEmail.getText().toString();
        final long phoneNumber = Long.parseLong(mPhoneNumber.getText().toString());

        //przypadek1: jesli user dokonał zmieny username
        if (!mUserSetting.getUser().getUsername().equals(userName)) {
            checkIfUsernameExists(userName);
        }

        //przypadek2: user zmienił swój email

        if (!mUserSetting.getUser().getEmail().equals(email)) {

            //krok1 : reautenticate
            ConfirmPasswordDialog dialog = new ConfirmPasswordDialog();
            dialog.show(getFragmentManager(), getString(R.string.confirm_password_dialog));
            dialog.setTargetFragment(EditProfileFragment.this, 1);

            //krok2 : sprawdzamy czy email juz istnieje

            //krok3 : zmieniamy enail
        }

        /*
        * Zmiana pozostałych ustawień, ktore nie wymagają zeby były unique
        * */
        if(!mUserSetting.getSettings().getDisplay_name().equals(displayName)){
            //uaktualniamy display_name
            mFirebaseMethods.updateUserAccountSetting(displayName, null,null, 0);
        }

        if(!mUserSetting.getSettings().getWebsite().equals(website)){
            //uaktualniamy website
            mFirebaseMethods.updateUserAccountSetting(null, website,null, 0);
        }

        if(!mUserSetting.getSettings().getDescription().equals(description)){
            //uaktualniamy description
            mFirebaseMethods.updateUserAccountSetting(null, null, description, 0);
        }

        if(mUserSetting.getUser().getPhone_number() != phoneNumber){
            //uaktualniamy profilePhoto
            mFirebaseMethods.updateUserAccountSetting(null, null,null, phoneNumber);
        }




    }

    /**
     * sprawdzamy czy @param username juz istnieje w bazie danych
     *
     * @param username
     */
    private void checkIfUsernameExists(final String username) {
        Log.d(TAG, "checkIfUsernameExists: sprawdzamy czy " + username + "juz istnieje w bazie");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference
                .child(getString(R.string.dbname_users))
                .orderByChild(getString(R.string.field_username))
                .equalTo(username);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (!dataSnapshot.exists()) {
                    //dodajemy username
                    mFirebaseMethods.updateUsername(username);

                    Toast.makeText(getActivity(), "Saved username", Toast.LENGTH_SHORT).show();
                }
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    if (dataSnapshot.exists()) {
                        Log.d(TAG, "checkIfUsernameExists FOUND A MATCH " + dataSnapshot.getValue(User.class).getUsername());
                        Toast.makeText(getActivity(), "That username already exists", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void setProfileWidgets(UserSettings userSettings) {
        Log.d(TAG, "setProfileWidgets: ustawianie wigdetów z uzyciem bazy z firebase" + userSettings.toString());

        mUserSetting = userSettings;

        // User user = userSettings.getUser();
        UserAccountSettings settings = userSettings.getSettings();

        UniversalImageLoader.setImage(settings.getProfile_photo(), mProfilePhoto, null, "");

        mDisplayName.setText(settings.getDisplay_name());
        mUsername.setText(settings.getUsername());
        mWebsite.setText(settings.getWebsite());
        mDescription.setText(settings.getDescription());
        mEmail.setText(userSettings.getUser().getEmail());
        mPhoneNumber.setText(String.valueOf(userSettings.getUser().getPhone_number()));

        //zmieniamy zdjecie profilowe
        mChangeProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick:  zmieniamy zdjecie profilowe");
                Intent intent = new Intent(getActivity(), ShareActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getActivity().startActivity(intent);
                getActivity().finish();
            }
        });
    }

    /*
        ------------------------------FIREBASE -----------------------------------------
    */

    private void setupFirebaseAuth() {
        Log.d(TAG, "setupFirebaseAuth: setting up firebase");

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();
        userID = mAuth.getCurrentUser().getUid();


        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();


                if (user != null) {
                    Log.d(TAG, "user signed_in, with userUID:  " + user.getUid());
                } else {
                    Log.d(TAG, "onAuthStateChanged: user signed_out");
                }
            }
        };
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                /*Pozyskiwanie danych o userze z bazy danych*/
                setProfileWidgets(mFirebaseMethods.getUserSettings(dataSnapshot));

//                setProfileWidgets(mFirebaseMethods.getUserSettings(dataSnapshot));


                /*Pozyskiwanie obrazków usera z bazy danych*/

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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

    @Override
    public void onConfirmPassword(String password) {
        Log.d(TAG, "onConfirmPassword: hasło to" + password);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        AuthCredential credential = EmailAuthProvider
                .getCredential(mAuth.getCurrentUser().getEmail(), password);


        mAuth.getCurrentUser().reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: user reauthtenticated.");

                            ////////////Sprawdzamy czy eimail juz nie istnieje w bazie danych
                            mAuth.fetchProvidersForEmail(mEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
                                @Override
                                public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                                    if (task.isSuccessful()) {

                                        try {
                                            if (task.getResult().getProviders().size() == 1) {
                                                Log.d(TAG, "onComplete: ten email jest juz uzywany");
                                                Toast.makeText(getActivity(), "That email is already in use", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Log.d(TAG, "onComplete: ten email nie jest zajęty");

                                                //////////Email jest dostepny, uaktualniamy go
                                                mAuth.getCurrentUser().updateEmail(mEmail.getText().toString())
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    Log.d(TAG, "onComplete: sukces, email został uaktualniony");
                                                                    Toast.makeText(getActivity(), "Email was updated", Toast.LENGTH_LONG).show();
                                                                    mFirebaseMethods.updateEmail(mEmail.getText().toString());
                                                                }
                                                            }
                                                        });
                                            }
                                        } catch (NullPointerException e) {
                                            Log.e(TAG, "onComplete: " + e.getMessage());
                                        }

                                    }
                                }
                            });


                        } else {
                            Log.d(TAG, "onComplete: authentication failed");
                        }
                    }
                });
    }
}
