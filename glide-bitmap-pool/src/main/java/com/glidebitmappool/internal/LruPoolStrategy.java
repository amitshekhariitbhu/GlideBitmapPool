package com.glidebitmappool.internal;

import android.graphics.Bitmap;

/**
 * Created by amitshekhar on 17/06/16.
 */
interface LruPoolStrategy {
    void put(Bitmap bitmap);

    Bitmap get(int width, int height, Bitmap.Config config);

    Bitmap removeLast();

    String logBitmap(Bitmap bitmap);

    String logBitmap(int width, int height, Bitmap.Config config);

    int getSize(Bitmap bitmap);
}