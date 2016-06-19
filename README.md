# Glide Bitmap Pool 

[![License](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](https://github.com/amitshekhariitbhu/AndroidNetworking/blob/master/LICENSE)

### About Glide Bitmap Pool

Glide Bitmap Pool is a memory management library for reusing the bitmap memory. As it reuses bitmap memory , so 
no more GC calling again and again which leads to smooth running application. It uses inBitmap while decoding the bitmap
on the supported android versions. All the version use-cases has been handled to optimize it better.

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
Bitmap bitmap1 = GlideBitmapFactory.decodeResource(getResources(), R.drawable.testImage);
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

### Important
```
// Do not use bitmap.recycle();
// use GlideBitmapPool.putBitmap(bitmap); as it will put in the pool for further reuse.

// Do not use Bitmap.create(width, height, config);
// use GlideBitmapPool.getBitmap(width, height, config); as it returns from the pool that can be reused.
```

### Credits and references
* [Glide](https://github.com/bumptech/glide) - As it uses the same bitmap pool that is used by [Glide](https://github.com/bumptech/glide)
* [Managing Bitmap Memory](https://developer.android.com/training/displaying-bitmaps/manage-memory.html)
* [Loading Large Bitmaps Efficiently](https://developer.android.com/training/displaying-bitmaps/load-bitmap.html)

### TODO
* More Optimization with further updates.

### License
```
   Copyright (C) 2016 Amit Shekhar
   Copyright (C) 2011 Android Open Source Project

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
Just make pull request. You are in!