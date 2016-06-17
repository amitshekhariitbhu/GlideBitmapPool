package com.glidebitmappool.internal;

import android.graphics.Bitmap;

/**
 * Created by amitshekhar on 17/06/16.
 */
public interface BitmapPool {

    int getMaxSize();

    void setSizeMultiplier(float sizeMultiplier);

    void put(Bitmap bitmap);

    Bitmap get(int width, int height, Bitmap.Config config);

    Bitmap getDirty(int width, int height, Bitmap.Config config);

    void clearMemory();

    void trimMemory(int level);
}

