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

package com.glidebitmappoolsample;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.glidebitmappool.GlideBitmapFactory;
import com.glidebitmappool.GlideBitmapPool;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GlideBitmapPool.initialize(20 * 1024 * 1024);
    }

    public void normalResource(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.test1);
                bitmap1.recycle();
                Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.test2);
                bitmap2.recycle();
                Bitmap bitmap3 = BitmapFactory.decodeResource(getResources(), R.drawable.test3);
                bitmap3.recycle();
                Bitmap bitmap4 = BitmapFactory.decodeResource(getResources(), R.drawable.test4);
                bitmap4.recycle();
                Bitmap bitmap5 = BitmapFactory.decodeResource(getResources(), R.drawable.test5);
                bitmap5.recycle();
                Bitmap bitmap6 = BitmapFactory.decodeResource(getResources(), R.drawable.test6);
                bitmap6.recycle();
                Bitmap bitmap7 = BitmapFactory.decodeResource(getResources(), R.drawable.test7);
                bitmap7.recycle();
                Bitmap bitmap8 = BitmapFactory.decodeResource(getResources(), R.drawable.test8);
                bitmap8.recycle();
                Bitmap bitmap9 = BitmapFactory.decodeResource(getResources(), R.drawable.test9);
                bitmap9.recycle();
                Bitmap bitmap10 = BitmapFactory.decodeResource(getResources(), R.drawable.test10);
                bitmap10.recycle();
            }
        }).start();
    }

    public void normalFile(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Download" + File.separator + "test";
                for (int i = 1; i <= 10; i++) {
                    Bitmap bitmap = BitmapFactory.decodeFile(path + i + ".png");
                    bitmap.recycle();
                }
            }
        }).start();
    }

    public void resourceOptimized(View view) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap1 = GlideBitmapFactory.decodeResource(getResources(), R.drawable.test1);
                GlideBitmapPool.putBitmap(bitmap1);

                Bitmap bitmap2 = GlideBitmapFactory.decodeResource(getResources(), R.drawable.test2);
                GlideBitmapPool.putBitmap(bitmap2);

                Bitmap bitmap3 = GlideBitmapFactory.decodeResource(getResources(), R.drawable.test3);
                GlideBitmapPool.putBitmap(bitmap3);

                Bitmap bitmap4 = GlideBitmapFactory.decodeResource(getResources(), R.drawable.test4);
                GlideBitmapPool.putBitmap(bitmap4);

                Bitmap bitmap5 = GlideBitmapFactory.decodeResource(getResources(), R.drawable.test5);
                GlideBitmapPool.putBitmap(bitmap5);

                Bitmap bitmap6 = GlideBitmapFactory.decodeResource(getResources(), R.drawable.test6);
                GlideBitmapPool.putBitmap(bitmap6);

                Bitmap bitmap7 = GlideBitmapFactory.decodeResource(getResources(), R.drawable.test7);
                GlideBitmapPool.putBitmap(bitmap7);

                Bitmap bitmap8 = GlideBitmapFactory.decodeResource(getResources(), R.drawable.test8);
                GlideBitmapPool.putBitmap(bitmap8);

                Bitmap bitmap9 = GlideBitmapFactory.decodeResource(getResources(), R.drawable.test9);
                GlideBitmapPool.putBitmap(bitmap9);

                Bitmap bitmap10 = GlideBitmapFactory.decodeResource(getResources(), R.drawable.test10);
                GlideBitmapPool.putBitmap(bitmap10);
            }
        }).start();
    }

    public void fileOptimized(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Download" + File.separator + "test";
                for (int i = 1; i <= 10; i++) {
                    Bitmap bitmap = GlideBitmapFactory.decodeFile(path + i + ".png");
                    GlideBitmapPool.putBitmap(bitmap);
                }
            }
        }).start();
    }

    public void downSample(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "Download" + File.separator + "test";
                for (int i = 1; i <= 10; i++) {
                    Bitmap bitmap = GlideBitmapFactory.decodeFile(path + i + ".png", 100, 100);
                    GlideBitmapPool.putBitmap(bitmap);
                }
            }
        }).start();
    }

    public void clearMemory(View view) {
        GlideBitmapPool.clearMemory();
    }
}
