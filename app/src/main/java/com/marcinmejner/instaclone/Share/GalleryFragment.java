package com.marcinmejner.instaclone.Share;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.marcinmejner.instaclone.Profile.AccountSettingsActivity;
import com.marcinmejner.instaclone.R;
import com.marcinmejner.instaclone.Utils.FilePaths;
import com.marcinmejner.instaclone.Utils.FileSearch;
import com.marcinmejner.instaclone.Utils.GridImageAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

/**
 * Created by Marc on 15.03.2018.
 */

public class GalleryFragment extends Fragment {
    private static final String TAG = "GalleryFragment";

    //stałe
    private static final int NUM_GRID_COLUMNS = 3;

    //widgets
    private GridView gridView;
    private ImageView galleryImage;
    private ProgressBar progressBar;
    private Spinner directorySpinner;

    //vars
    private ArrayList<String> directories;
    public String mAppend = "file:/";
    private String mSelectedImage;


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

        directories = new ArrayList<>();

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
                if (isRootTask()) {
                    Intent intent = new Intent(getActivity(), NextActivity.class);
                    intent.putExtra(getString(R.string.selected_image), mSelectedImage);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), AccountSettingsActivity.class);
                    intent.putExtra(getString(R.string.selected_image), mSelectedImage);
                    intent.putExtra(getString(R.string.return_to_fragment), getString(R.string.edit_profile_fragment));
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        });

        init();

        return view;
    }

    private boolean isRootTask() {

        if (((ShareActivity) getActivity()).getTask() == 0) {
            return true;
        } else {
            return false;
        }
    }

    private void init() {
        FilePaths filePaths = new FilePaths();

        //sprawdzamy inne foldery w "/storage/emulated/0/pictures
        if (FileSearch.getDirectoryPaths(filePaths.PICRURES) != null) {
            directories = FileSearch.getDirectoryPaths(filePaths.PICRURES);
        }
        directories.add(filePaths.CAMERA);
        directories.add(filePaths.DOWNLOADS);

        ArrayList<String> directoryNames = new ArrayList<>();
        for (int i = 0; i < directories.size(); i++) {
            int index = directories.get(i).lastIndexOf("/");
            String string = directories.get(i).substring(index);
            directoryNames.add(string);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, directoryNames);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        directorySpinner.setAdapter(adapter);

        directorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG, "onItemClick: wybrano " + directories.get(i));

                //ustawiamy nasz image grid dla wybranego katalogu
                setupGridView(directories.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    private void setupGridView(String selectedDirectory) {
        Log.d(TAG, "setupGridView: wybrany katalog to: " + selectedDirectory);
        final ArrayList<String> imgURLs = FileSearch.getFilePaths(selectedDirectory);

        //Ustawiamy szerokość kolumn
        int gridWidth = getResources().getDisplayMetrics().widthPixels;
        int imageWidth = gridWidth / NUM_GRID_COLUMNS;
        gridView.setColumnWidth(imageWidth);

        //uzywamy grid adaptera
        GridImageAdapter adapter = new GridImageAdapter(getActivity(), R.layout.layout_grid_imageview, mAppend, imgURLs);
        gridView.setAdapter(adapter);

        //ustawiamy pierwszy obrazek do wyświetlenia po tym jak aktivity jest inflated
        try {
            if(imgURLs.size() > 0) {
                setImage(imgURLs.get(0), galleryImage, mAppend);
                mSelectedImage = imgURLs.get(0);
            }
        }catch (ArrayIndexOutOfBoundsException e){
            Log.e(TAG, "setupGridView: ArrayIndexOutOfBoundsException" + e.getMessage() );
        }


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.d(TAG, "onItemClick: wybrano obrazek: " + imgURLs.get(i));
                setImage(imgURLs.get(i), galleryImage, mAppend);
                mSelectedImage = imgURLs.get(i);
            }
        });
    }

    private void setImage(String imgURL, ImageView image, String append) {
        Log.d(TAG, "setImage: ustawiamy obrazek");
        ImageLoader imageLoader = ImageLoader.getInstance();

        imageLoader.displayImage(append + imgURL, image, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
}







