package com.wag.challenge.service;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;
import android.support.v4.util.LruCache;

import com.wag.challenge.util.CryptoUtil;
import com.wag.challenge.util.LogCatLogger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by PGomez on 9/21/2017.
 */

public class ImageCacheService {

    //region constants
    private static final String TAG = "ImageCacheService";
    //endregion

    //region variables
    private final Context context;
    private final File cacheDir;
    private LruCache<String, Bitmap> bitMapCache = new LruCache<String, Bitmap>(2 * 1024 * 1024) {
        @Override
        protected int sizeOf(String key, Bitmap bitmap) {
            return bitmap.getByteCount() / 1024;
        }
    };
    //endregion

    public ImageCacheService(Context context) {
        this.context = context;
        this.cacheDir = context.getCacheDir();
    }

    /**
     * This method saves a bitmap into a file in the cache dir of the app.
     * @param bitmap the bitmaps to save
     * @param url url to use as file name when hashed
     * @return true if it was successful false it wasn't
     */
    @WorkerThread
    public boolean saveBitmap(Bitmap bitmap, String url){
        String md5 = CryptoUtil.getMd5(url);
        File bitmapFile = new File(cacheDir.getAbsolutePath().concat(":").concat(md5));
        LogCatLogger.debug(TAG, "bitmapFile " + bitmapFile.getAbsolutePath());
        if(bitmapFile.exists()) {
            bitmapFile.delete();
        }
        OutputStream out = null;
        try {
            out = new FileOutputStream(bitmapFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            addBitmapToMemoryCache(url, bitmap);
            return true;
        }catch (Exception e) {
            LogCatLogger.error(TAG, e);
        }
        finally {
            if(out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    LogCatLogger.error(TAG, e);
                }
            }
        }
        return false;
    }

    /**
     * This method checks if the file exist on cache based on an url
     * @param url url to use as file name when hashed
     * @return true if it was successful false it wasn't
     */
    @WorkerThread
    public boolean doesBitmapExistOnCache(String url) {
        Bitmap bitmapFromMemCache = getBitmapFromMemCache(url);
        if(bitmapFromMemCache == null) {
            String md5 = CryptoUtil.getMd5(url);
            File bitmapFile = new File(cacheDir.getAbsolutePath().concat(md5));

            return bitmapFile.exists() && bitmapFile.length() > 0;
        } else {
            return true;
        }
    }


    /**
     * This method tries to fetch the bitmap from memory cache if it fails it checks disk cache.
     * @param url the url of the user avatar.
     * @return decode bitmap or null if it fails to retrieve it from disk.
     */
    @WorkerThread
    public @Nullable Bitmap getBitmapFromCache(String url) {
        Bitmap bitmapFromMemCache = getBitmapFromMemCache(url);
        if(bitmapFromMemCache == null) {
            String md5 = CryptoUtil.getMd5(url);
            File bitmapFile = new File(cacheDir.getAbsolutePath().concat(md5));
            if (bitmapFile.exists()) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                Bitmap bitmap = BitmapFactory.decodeFile(bitmapFile.getAbsolutePath(), options);
                return bitmap;
            } else {
                return null;
            }
        } else {
            return bitmapFromMemCache;
        }
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            bitMapCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return bitMapCache.get(key);
    }
}
