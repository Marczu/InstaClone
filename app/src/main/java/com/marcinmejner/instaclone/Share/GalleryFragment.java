package com.marcinmejner.instaclone.Share;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.marcinmejner.instaclone.R;

/**
 * Created by Marc on 15.03.2018.
 */

public class GalleryFragment extends Fragment{
    private static final String TAG = "GalleryFragment";

    //widgets
    private GridView gridView;
    private ImageView galleryImage;
    private ProgressBar progressBar;
    private Spinner directorySpinner;


    //vars

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmeny_gallery, container, false);
        Log.d(TAG, "onCreateView: started");

        galleryImage = view.findViewById(R.id.galleryImageView);
        gridView = view.findViewById(R.id.gridView);
        directorySpinner = view.findViewById(R.id.spinnerDirectory);
        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        ImageView shareClose = view.findViewById(R.id.ivCloseShare);
        shareClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: zamykamy gallery fragment");
                getActivity().finish();
            }
        });

        TextView nextScreen = view.findViewById(R.id.tvNext);
        nextScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: nawigujemy do finalnego share screem");
            }
        });

        return view;
    }

    private void init(){

        //sprawdzamy inne foldery w "/storage/emulated/0/pictures

    }
}







