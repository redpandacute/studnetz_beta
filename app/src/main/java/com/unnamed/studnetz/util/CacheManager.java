package com.unnamed.studnetz.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CacheManager {

    Context mContext;

    public CacheManager(Context mContext) {
        this.mContext = mContext;
    }

    public String createCachePicture(String pictureBLOB) throws IOException {
        //Creating a temporary File to store the picture in
        File cacheDir = mContext.getCacheDir(), tempFile;

        try {
            tempFile = File.createTempFile("PictureFile" + String.valueOf(System.currentTimeMillis()), ".png", cacheDir);
        } catch (IOException e) {
            tempFile = new File(cacheDir, "PictureFile" + String.valueOf(System.currentTimeMillis()) + ".png");
        }

        //Compressing the Picture into the created File
        Bitmap mBitmap = decodeBASE64(pictureBLOB);
        FileOutputStream mOut = new FileOutputStream(tempFile);

        mBitmap.compress(Bitmap.CompressFormat.PNG, 100, mOut);

        mOut.close();

        return tempFile.getPath();
    }

    //PICTURE CONVERSIONS

    private Bitmap decodeBASE64(String BASE64) {
        byte[] decodedInput = Base64.decode(BASE64, 0);
        return BitmapFactory.decodeByteArray(decodedInput, 0, decodedInput.length);
    }
}
