# Glide Bitmap Pool 

<img src=https://raw.githubusercontent.com/amitshekhariitbhu/GlideBitmapPool/master/assets/glidebitmappool.png width=1000 height=140 />

### About Glide Bitmap Pool

Glide Bitmap Pool is a memory management library for reusing the bitmap memory. As it reuses bitmap memory , so 
no more GC calling again and again , hence smooth running application. It uses inBitmap while decoding the bitmap
on the supported android versions. All the version use-cases has been handled to optimize it better.

* [Glide](https://github.com/bumptech/glide) , [Fresco](https://github.com/facebook/fresco) and [Fast Android Networking](https://github.com/amitshekhariitbhu/Fast-Android-Networking) uses Bitmap Pool Concept to 
load images efficiently.

### Why use this library ?

An Image heavy Application decodes many images , so there will be continuous allocation and deallocation 
of memory in application , and that results in very frequent calling of GC(Garbage Collector). And 
finally because of very frequent calling of GC , the application UI freezes.
Use Bitmap pool to avoid continuous allocation and deallocation of memory in application 
and reduce GC overhead that will result in smooth running application.
Suppose we have to load few bitmap in Android Application.
When we load bitmapOne , it will allocate the memory for bitmapOne.
Then if we don’t need bitmapOne , do not recycle bitmap (as if you recycle, it will make GC to be called) , 
so use this bitmapOne as an inBitmap for bitmapTwo so that , the same memory can be reused for bitmapTwo.
In this way , we can avoid continuous allocation and deallocation of memory in application and reduce GC overhead.
But the problem is that there are few restrictions as android version less than Honeycomb does not supports it , 
few android version less than Kitkat only when we use inSampleSize = 1 , above that it supports 
completely and few other issues.
So , all these types of cases are handled in this library

* [Refer this blog for detailed explanation](https://amitshekhar.me/blog/android-image-loading-library-use-bitmap-pool-for-responsive-ui)

## [My Personal Blog - amitshekhar.me](https://amitshekhar.me/blog) - High-quality content to learn Android concepts.

#### GET RID OF : GC_FOR_ALLOC freed 1568K, 23% free 37664K/48844K, paused 141ms, total 143ms - (whenever you see this log , your application is lagging)

<img src=https://raw.githubusercontent.com/amitshekhariitbhu/GlideBitmapPool/master/assets/gcsamplelog.png width=500 height=150 />

## Requirements

Glide Bitmap Pool can be included in any Android or Java application. 

Glide Bitmap Pool supports Android 2.3 (Gingerbread) and later. 

## Using Glide Bitmap Pool in your application

Add this in your build.gradle
```groovy
compile 'com.amitshekhar.android:glide-bitmap-pool:0.0.1'
```
Then initialize it in onCreate() Method of application class, :
```java
GlideBitmapPool.initialize(10 * 1024 * 1024); // 10mb max memory size
```

### Decoding the bitmap from file path
```java
Bitmap bitmap = GlideBitmapFactory.decodeFile(filePath);
```

### Decoding the bitmap from resources
```java
Bitmap bitmap = GlideBitmapFactory.decodeResource(getResources(), R.drawable.testImage);
```

### Decoding the down sample bitmap
```java
Bitmap bitmap = GlideBitmapFactory.decodeFile(filePath,100,100);
```

### Making the bitmap available for recycle or reuse
```java
GlideBitmapPool.putBitmap(bitmap);
```

### Getting the empty bitmap from the pool
```java
Bitmap bitmap = GlideBitmapPool.getBitmap(width, height, config);
```

### Clearing or Trimming Memory
```java
GlideBitmapPool.clearMemory();
GlideBitmapPool.trimMemory(level);
```

### Migrating to Glide Bitmap Pool
```java
// ------   decoding -------

// old code 
Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test1);

// new code 
Bitmap bitmap = GlideBitmapFactory.decodeResource(getResources(), R.drawable.test1);

// ------   recycling ------- 

// old code
bitmap.recycle();

// new code
GlideBitmapPool.putBitmap(bitmap);

//  ------   creating a bitmap -------

// old code 
Bitmap bitmap = Bitmap.create(width, height, config);

// new code
Bitmap bitmap = GlideBitmapPool.getBitmap(width, height, config);
```

### Important
```
// Do not use bitmap.recycle();
// use GlideBitmapPool.putBitmap(bitmap); as it will put bitmap in the pool for further reuse.

// Do not use Bitmap.create(width, height, config);
// use GlideBitmapPool.getBitmap(width, height, config); as it returns bitmap from the pool that can be reused.
```

### Find this project useful ? :heart:
* Support it by clicking the :star: button on the upper right of this page. :v:

### Credits and references
* [Glide](https://github.com/bumptech/glide) - As it uses the same bitmap pool that is used by [Glide](https://github.com/bumptech/glide)
* [Managing Bitmap Memory](https://developer.android.com/training/displaying-bitmaps/manage-memory.html)
* [Loading Large Bitmaps Efficiently](https://developer.android.com/training/displaying-bitmaps/load-bitmap.html)

### TODO
* More Optimization with further updates.

### Check out another awesome library for fast and simple networking in Android.
* [Fast Android Networking Library](https://github.com/amitshekhariitbhu/Fast-Android-Networking)

### Another awesome library for debugging databases and shared preferences.
* [Android Debug Database](https://github.com/amitshekhariitbhu/Android-Debug-Database)

You can connect with me on:

- [Twitter](https://twitter.com/amitiitbhu)
- [LinkedIn](https://www.linkedin.com/in/amit-shekhar-iitbhu)
- [GitHub](https://github.com/amitshekhariitbhu)
- [Facebook](https://www.facebook.com/amit.shekhar.iitbhu)

[**Read all of my blogs here.**](https://amitshekhar.me/blog)

### License
```
   Copyright (C) 2022 Amit Shekhar

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```

### Contributing to Glide Bitmap Pool
All pull requests are welcome, make sure to follow the [contribution guidelines](CONTRIBUTING.md)
when you submit pull request.
