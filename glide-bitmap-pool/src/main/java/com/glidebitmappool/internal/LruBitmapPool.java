/*
 *    Copyright (C) 2016 Amit Shekhar
 *    Copyright (C) 2011 Android Open Source Project
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.glidebitmappool.internal;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by amitshekhar on 17/06/16.
 */
public class LruBitmapPool implements BitmapPool {
    private static final String TAG = LruBitmapPool.class.getSimpleName();
    private static final Bitmap.Config DEFAULT_CONFIG = Bitmap.Config.ARGB_8888;

    private final LruPoolStrategy strategy;
    private final Set<Bitmap.Config> allowedConfigs;
    private final int initialMaxSize;
    private final BitmapTracker tracker;

    private int maxSize;
    private int currentSize;
    private int hits;
    private int misses;
    private int puts;
    private int evictions;

    private LruBitmapPool(int maxSize, LruPoolStrategy strategy, Set<Bitmap.Config> allowedConfigs) {
        this.initialMaxSize = maxSize;
        this.maxSize = maxSize;
        this.strategy = strategy;
        this.allowedConfigs = allowedConfigs;
        this.tracker = new NullBitmapTracker();
    }

    public LruBitmapPool(int maxSize) {
        this(maxSize, getDefaultStrategy(), getDefaultAllowedConfigs());
    }

    public LruBitmapPool(int maxSize, Set<Bitmap.Config> allowedConfigs) {
        this(maxSize, getDefaultStrategy(), allowedConfigs);
    }

    @Override
    public int getMaxSize() {
        return maxSize;
    }

    @Override
    public synchronized void setSizeMultiplier(float sizeMultiplier) {
        maxSize = Math.round(initialMaxSize * sizeMultiplier);
        evict();
    }

    @Override
    public synchronized void put(Bitmap bitmap) {
        if (bitmap == null) {
            throw new NullPointerException("Bitmap must not be null");
        }
        if (bitmap.isRecycled()) {
            throw new IllegalStateException("Cannot pool recycled bitmap");
        }

        if (!bitmap.isMutable() || strategy.getSize(bitmap) > maxSize
                || !allowedConfigs.contains(bitmap.getConfig())) {
            if (Log.isLoggable(TAG, Log.VERBOSE)) {
                Log.v(TAG, "Reject bitmap from pool"
                        + ", bitmap: " + strategy.logBitmap(bitmap)
                        + ", is mutable: " + bitmap.isMutable()
                        + ", is allowed config: " + allowedConfigs.contains(bitmap.getConfig()));
            }
            bitmap.recycle();
            return;
        }

        final int size = strategy.getSize(bitmap);
        strategy.put(bitmap);
        tracker.add(bitmap);

        puts++;
        currentSize += size;

        if (Log.isLoggable(TAG, Log.VERBOSE)) {
            Log.v(TAG, "Put bitmap in pool=" + strategy.logBitmap(bitmap));
        }
        dump();

        evict();
    }

    private void evict() {
        trimToSize(maxSize);
    }

    @Override
    public Bitmap get(int width, int height, Bitmap.Config config) {
        Bitmap result = getDirtyOrNull(width, height, config);
        if (result != null) {
            result.eraseColor(Color.TRANSPARENT);
        } else {
            result = Bitmap.createBitmap(width, height, config);
        }
        return result;
    }

    @Override
    public Bitmap getDirty(int width, int height, Bitmap.Config config) {
        Bitmap result = getDirtyOrNull(width, height, config);
        if (result == null) {
            result = Bitmap.createBitmap(width, height, config);
        }
        return result;
    }

    private synchronized Bitmap getDirtyOrNull(int width, int height, Bitmap.Config config) {
        // Config will be null for non public config types, which can lead to transformations naively
        // passing in null as the requested config here. See issue #194.
        final Bitmap result = strategy.get(width, height, config != null ? config : DEFAULT_CONFIG);
        if (result == null) {
            if (Log.isLoggable(TAG, Log.DEBUG)) {
                Log.d(TAG, "Missing bitmap=" + strategy.logBitmap(width, height, config));
            }
            misses++;
        } else {
            hits++;
            currentSize -= strategy.getSize(result);
            tracker.remove(result);
            normalize(result);
        }
        if (Log.isLoggable(TAG, Log.VERBOSE)) {
            Log.v(TAG, "Get bitmap=" + strategy.logBitmap(width, height, config));
        }
        dump();

        return result;
    }

    // Setting these two values provides Bitmaps that are essentially equivalent to those returned
    // from Bitmap.createBitmap.
    private static void normalize(Bitmap bitmap) {
        maybeSetAlpha(bitmap);
        maybeSetPreMultiplied(bitmap);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    private static void maybeSetAlpha(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            bitmap.setHasAlpha(true);
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static void maybeSetPreMultiplied(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            bitmap.setPremultiplied(true);
        }
    }

    @Override
    public void clearMemory() {
        if (Log.isLoggable(TAG, Log.DEBUG)) {
            Log.d(TAG, "clearMemory");
        }
        trimToSize(0);
    }

    @SuppressLint("InlinedApi")
    @Override
    public void trimMemory(int level) {
        if (Log.isLoggable(TAG, Log.DEBUG)) {
            Log.d(TAG, "trimMemory, level=" + level);
        }
        if (level >= android.content.ComponentCallbacks2.TRIM_MEMORY_BACKGROUND) {
            clearMemory();
        } else if (level >= android.content.ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
            trimToSize(maxSize / 2);
        }
    }

    private synchronized void trimToSize(int size) {
        while (currentSize > size) {
            final Bitmap removed = strategy.removeLast();
            if (removed == null) {
                if (Log.isLoggable(TAG, Log.WARN)) {
                    Log.w(TAG, "Size mismatch, resetting");
                    dumpUnchecked();
                }
                currentSize = 0;
                return;
            }
            tracker.remove(removed);
            currentSize -= strategy.getSize(removed);
            evictions++;
            if (Log.isLoggable(TAG, Log.DEBUG)) {
                Log.d(TAG, "Evicting bitmap=" + strategy.logBitmap(removed));
            }
            dump();
            removed.recycle();
        }
    }

    private void dump() {
        if (Log.isLoggable(TAG, Log.VERBOSE)) {
            dumpUnchecked();
        }
    }

    private void dumpUnchecked() {
        Log.v(TAG, "Hits=" + hits + ", misses=" + misses + ", puts=" + puts + ", evictions=" + evictions
                + ", currentSize=" + currentSize + ", maxSize=" + maxSize + "\nStrategy=" + strategy);
    }

    private static LruPoolStrategy getDefaultStrategy() {
        final LruPoolStrategy strategy;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            strategy = new SizeConfigStrategy();
        } else {
            strategy = new AttributeStrategy();
        }
        return strategy;
    }

    private static Set<Bitmap.Config> getDefaultAllowedConfigs() {
        Set<Bitmap.Config> configs = new HashSet<>();
        configs.addAll(Arrays.asList(Bitmap.Config.values()));
        if (Build.VERSION.SDK_INT >= 19) {
            configs.add(null);
        }
        return Collections.unmodifiableSet(configs);
    }

    private interface BitmapTracker {
        void add(Bitmap bitmap);

        void remove(Bitmap bitmap);
    }

    @SuppressWarnings("unused")
    // Only used for debugging
    private static class ThrowingBitmapTracker implements BitmapTracker {
        private final Set<Bitmap> bitmaps = Collections.synchronizedSet(new HashSet<Bitmap>());

        @Override
        public void add(Bitmap bitmap) {
            if (bitmaps.contains(bitmap)) {
                throw new IllegalStateException(
                        "Can't add already added bitmap: " + bitmap + " [" + bitmap.getWidth() + "x" + bitmap
                                .getHeight() + "]");
            }
            bitmaps.add(bitmap);
        }

        @Override
        public void remove(Bitmap bitmap) {
            if (!bitmaps.contains(bitmap)) {
                throw new IllegalStateException("Cannot remove bitmap not in tracker");
            }
            bitmaps.remove(bitmap);
        }
    }

    private static class NullBitmapTracker implements BitmapTracker {
        @Override
        public void add(Bitmap bitmap) {
            // Do nothing.
        }

        @Override
        public void remove(Bitmap bitmap) {
            // Do nothing.
        }
    }
}
