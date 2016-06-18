package com.glidebitmappoolsample;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.glidebitmappool.GlideBitmap;
import com.glidebitmappool.GlideBitmapPool;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GlideBitmapPool.initialize(20 * 1024 * 1024);
    }

    public void normal(View view) {
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

    public void resourceOptimized(View view) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap1 = GlideBitmap.decodeResource(getResources(), R.drawable.test1);
                GlideBitmapPool.putBitmap(bitmap1);

                Bitmap bitmap2 = GlideBitmap.decodeResource(getResources(), R.drawable.test2);
                GlideBitmapPool.putBitmap(bitmap2);

                Bitmap bitmap3 = GlideBitmap.decodeResource(getResources(), R.drawable.test3);
                GlideBitmapPool.putBitmap(bitmap3);

                Bitmap bitmap4 = GlideBitmap.decodeResource(getResources(), R.drawable.test4);
                GlideBitmapPool.putBitmap(bitmap4);

                Bitmap bitmap5 = GlideBitmap.decodeResource(getResources(), R.drawable.test5);
                GlideBitmapPool.putBitmap(bitmap5);

                Bitmap bitmap6 = GlideBitmap.decodeResource(getResources(), R.drawable.test6);
                GlideBitmapPool.putBitmap(bitmap6);

                Bitmap bitmap7 = GlideBitmap.decodeResource(getResources(), R.drawable.test7);
                GlideBitmapPool.putBitmap(bitmap7);

                Bitmap bitmap8 = GlideBitmap.decodeResource(getResources(), R.drawable.test8);
                GlideBitmapPool.putBitmap(bitmap8);

                Bitmap bitmap9 = GlideBitmap.decodeResource(getResources(), R.drawable.test9);
                GlideBitmapPool.putBitmap(bitmap9);

                Bitmap bitmap10 = GlideBitmap.decodeResource(getResources(), R.drawable.test10);
                GlideBitmapPool.putBitmap(bitmap10);
            }
        }).start();
    }

    public void fileOptimized(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "test.png";
                for (int i = 0; i < 10; i++) {
                    Bitmap bitmap1 = GlideBitmap.decodeFile(path);
                    GlideBitmapPool.putBitmap(bitmap1);
                }
            }
        }).start();
    }

    public void clearMemory(View view) {
        GlideBitmapPool.clearMemory();
    }
}
