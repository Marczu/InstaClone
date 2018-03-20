package com.marcinmejner.instaclone.Profile;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.marcinmejner.instaclone.R;
import com.marcinmejner.instaclone.Utils.UniversalImageLoader;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Marc on 18.03.2018.
 */

public class EditProfileFragment extends Fragment {
    private static final String TAG = "EditProfileFragment";

    private ImageView profileImage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_editprofile, container, false);
        profileImage = view.findViewById(R.id.profile_photo);


        setProfileImage();

        //ustawienie backarrow
        ImageView backArrow = view.findViewById(R.id.backArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();

            }
        });

        return view;
    }



    private void setProfileImage() {
        String imageURL = "https://www.famousbirthdays.com/headshots/tori-amos-2.jpg";
        UniversalImageLoader.setImage(imageURL, profileImage, null, "");
    }
}
