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
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.marcinmejner.instaclone.Login.LoginActivity;
import com.marcinmejner.instaclone.R;

/**
 * Created by Marc on 18.03.2018.
 */

public class SignOutFragment extends Fragment {
    private static final String TAG = "SignOutFragment";

    //Firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private ProgressBar progressBar;
    private TextView tvSignout;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signout, container, false);

        tvSignout = view.findViewById(R.id.tcConfirmSighout);
        progressBar = view.findViewById(R.id.progressbar_signout);
        Button btnConfirmSignout = view.findViewById(R.id.btnConfirmSignOut);

        progressBar.setVisibility(View.GONE);
        setupFirebaseAuth();

        btnConfirmSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: proba wylogowania się");
                progressBar.setVisibility(View.VISIBLE);
                mAuth.signOut();

                getActivity().finish();
            }
        });
        return view;
    }

      /*
        ------------------------------FIREBASE -----------------------------------------
    */
    private void setupFirebaseAuth(){
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();


                if(user!=null){
                    Log.d(TAG, "user signed_in:  " + user.getUid());
                }else{
                    Log.d(TAG, "onAuthStateChanged: user signed_out");
                    Log.d(TAG, "onAuthStateChanged: wracamy do ekranu logowania");
                    //Wracamy do ekranu logowania
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

}
