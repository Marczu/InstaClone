package com.marcinmejner.instaclone.Share;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.marcinmejner.instaclone.R;
import com.marcinmejner.instaclone.Utils.Permissions;

/**
 * Created by Marc on 15.03.2018.
 */

public class PhotoFragment extends Fragment{
    private static final String TAG = "PhotoFragment";

    //stałe
    public static final int PHOTO_FRAGMENT_NUM = 1;
    public static final int GALLERY_FRAGMENT_NUM = 2;
    private static final int CAMERA_REQUEST_CODE = 10;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: started");
        View view = inflater.inflate(R.layout.fragment_photo, container, false);

        Button btnLaunchCamera = view.findViewById(R.id.btnLaunchCamera);
        btnLaunchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: launching camera");

                if(((ShareActivity)getActivity()).getCurrentTabNumber() == PHOTO_FRAGMENT_NUM){

                    if(((ShareActivity)getActivity()).checkPermisions(Permissions.CAMERA_PERMISSION[0])){
                        Log.d(TAG, "onClick: starting camera");
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
                    } else{
                        Intent intent = new Intent(getActivity(), ShareActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                }
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CAMERA_REQUEST_CODE){
            Log.d(TAG, "onActivityResult: wychwycilismy photo");
            //nawigujemy do final share screen zeby opublikować photo

        }
    }
}






