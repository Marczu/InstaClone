package com.marcinmejner.instaclone.Utils;

import java.io.File;
import java.util.ArrayList;

public class FileSearch {


    /**
     * przeszukuje katalog i zwraca wszystkie katalogi ktore znajdują sie w środku
     * @param directory
     * @return
     */
    public static ArrayList<String> getDirectoryPaths(String directory){
        ArrayList<String> pathArray = new ArrayList<>();
        File file = new File(directory);

        File[] listfiles = file.listFiles();
        for (int i = 0; i < listfiles.length; i++) {
            if(listfiles[i].isDirectory()){
                pathArray.add(listfiles[i].getAbsolutePath());
            }
        }
        return pathArray;
    }

    /**
     * Przeszukuje katalog i zwraca wszystkie pliki w tym katalogu
     * @param directory
     * @return
     */
    public static ArrayList<String> getFilePaths(String directory){
        ArrayList<String> pathArray = new ArrayList<>();
        File file = new File(directory);

        File[] listfiles = file.listFiles();
        for (int i = 0; i < listfiles.length; i++) {
            if(listfiles[i].isFile()){
                pathArray.add(listfiles[i].getAbsolutePath());
            }
        }
        return pathArray;
    }
}
