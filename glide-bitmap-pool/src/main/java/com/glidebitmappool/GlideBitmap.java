package com.glidebitmappool;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.os.Build;

/**
 * Created by amitshekhar on 18/06/16.
 */
public class GlideBitmap {

    public static Bitmap decodeFile(String pathName) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inSampleSize = 1;
        BitmapFactory.decodeFile(pathName, options);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            options.inMutable = true;
            Bitmap inBitmap = GlideBitmapPool.getBitmap(options.outWidth, options.outHeight, options.inPreferredConfig);
            if (inBitmap != null && canUseForInBitmap(inBitmap, options)) {
                options.inBitmap = inBitmap;
            }
        }
        options.inJustDecodeBounds = false;
        try {
            return BitmapFactory.decodeFile(pathName, options);
        } catch (Exception e) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
                options.inBitmap = null;
            }
            return BitmapFactory.decodeFile(pathName, options);
        }
    }

    public static Bitmap decodeResource(Resources res, int id) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inSampleSize = 1;
        BitmapFactory.decodeResource(res, id, options);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            options.inMutable = true;
            Bitmap inBitmap = GlideBitmapPool.getBitmap(options.outWidth, options.outHeight, options.inPreferredConfig);
            if (inBitmap != null && canUseForInBitmap(inBitmap, options)) {
                options.inBitmap = inBitmap;
            }
        }
        options.inJustDecodeBounds = false;
        try {
            return BitmapFactory.decodeResource(res, id, options);
        } catch (Exception e) {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
                options.inBitmap = null;
            }
            return BitmapFactory.decodeResource(res, id, options);
        }
    }


    private static boolean canUseForInBitmap(
            Bitmap candidate, BitmapFactory.Options targetOptions) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // From Android 4.4 (KitKat) onward we can re-use if the byte size of
            // the new bitmap is smaller than the reusable bitmap candidate
            // allocation byte count.
            int width = targetOptions.outWidth / targetOptions.inSampleSize;
            int height = targetOptions.outHeight / targetOptions.inSampleSize;
            int byteCount = width * height * getBytesPerPixel(candidate.getConfig());

            try {
                return byteCount <= candidate.getAllocationByteCount();
            } catch (NullPointerException e) {
                return byteCount <= candidate.getHeight() * candidate.getRowBytes();
            }
        }
        // On earlier versions, the dimensions must match exactly and the inSampleSize must be 1
        return candidate.getWidth() == targetOptions.outWidth
                && candidate.getHeight() == targetOptions.outHeight
                && targetOptions.inSampleSize == 1;
    }

    /**
     * A helper function to return the byte usage per pixel of a bitmap based on its configuration.
     */
    private static int getBytesPerPixel(Config config) {
        if (config == Config.ARGB_8888) {
            return 4;
        } else if (config == Config.RGB_565) {
            return 2;
        } else if (config == Config.ARGB_4444) {
            return 2;
        } else if (config == Config.ALPHA_8) {
            return 1;
        }
        return 1;
    }

}
