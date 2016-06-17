package com.glidebitmappool;

import android.graphics.Bitmap;
import android.os.Build;

import com.glidebitmappool.internal.BitmapPool;
import com.glidebitmappool.internal.BitmapPoolAdapter;
import com.glidebitmappool.internal.LruBitmapPool;

import java.util.Set;

/**
 * Created by amitshekhar on 17/06/16.
 */
public class GlideBitmapPool {

    private static final int DEFAULT_MAX_SIZE = 6 * 1024 * 1024;
    private BitmapPool bitmapPool;
    private static GlideBitmapPool sInstance;

    private GlideBitmapPool(int maxSize) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            bitmapPool = new LruBitmapPool(maxSize);
        } else {
            bitmapPool = new BitmapPoolAdapter();
        }
    }

    private GlideBitmapPool(int maxSize, Set<Bitmap.Config> allowedConfigs) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            bitmapPool = new LruBitmapPool(maxSize, allowedConfigs);
        } else {
            bitmapPool = new BitmapPoolAdapter();
        }
    }

    public void initialize(int maxSize) {
        sInstance = new GlideBitmapPool(maxSize);
    }

    public void initialize(int maxSize, Set<Bitmap.Config> allowedConfigs) {
        sInstance = new GlideBitmapPool(maxSize, allowedConfigs);
    }

    private static GlideBitmapPool getInstance() {
        if (sInstance == null) {
            sInstance = new GlideBitmapPool(DEFAULT_MAX_SIZE);
        }
        return sInstance;
    }

    public static void putBitmap(Bitmap bitmap) {
        getInstance().bitmapPool.put(bitmap);
    }

    public static Bitmap getBitmap(int width, int height, Bitmap.Config config) {
        return getInstance().bitmapPool.get(width, height, config);
    }

    public static Bitmap getDirtyBitmap(int width, int height, Bitmap.Config config) {
        return getInstance().bitmapPool.getDirty(width, height, config);
    }

    public static void clearMemory() {
        getInstance().bitmapPool.clearMemory();
    }

    public static void trimMemory(int level) {
        getInstance().bitmapPool.trimMemory(level);
    }

    public static void shutDown() {
        if (sInstance != null) {
            sInstance.bitmapPool.clearMemory();
            sInstance = null;
        }

    }

}
