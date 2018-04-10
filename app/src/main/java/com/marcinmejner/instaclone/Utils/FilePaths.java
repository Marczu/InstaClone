package com.marcinmejner.instaclone.Utils;

import android.os.Environment;

public class FilePaths {

    //"storage/emulated/0"
    public String ROOT_DIR = Environment.getExternalStorageDirectory().getPath();

    public String PICRURES = ROOT_DIR + "/Pictures";
    public String DOWNLOADS = ROOT_DIR + "/Download";
    public String CAMERA = ROOT_DIR + "/DCIM/camera";
}
