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

    public String createCachePicture(String mPictureBLOB) throws IOException {
        //Creating a temporary File to store the picture in
        File mCacheDir = mContext.getCacheDir(), mTempFile;

        try {
            mTempFile = File.createTempFile("PictureFile" + String.valueOf(System.currentTimeMillis()), ".png", mCacheDir);
        } catch (IOException e) {
            mTempFile = new File(mCacheDir, "PictureFile" + String.valueOf(System.currentTimeMillis()) + ".png");
        }

        //Compressing the Picture into the created File
        Bitmap mBitmap = decodeBASE64(mPictureBLOB);
        FileOutputStream mOut = new FileOutputStream(mTempFile);

        mBitmap.compress(Bitmap.CompressFormat.PNG, 100, mOut);

        mOut.close();

        return mTempFile.getPath();
    }

    //PICTURE CONVERSIONS

    private Bitmap decodeBASE64(String BASE64) {
        byte[] decodedInput = Base64.decode(BASE64, 0);
        return BitmapFactory.decodeByteArray(decodedInput, 0, decodedInput.length);
    }
}
