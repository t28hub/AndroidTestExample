package com.t28.android.example.volley;

import android.graphics.Bitmap;

import com.android.volley.toolbox.ImageLoader;

public class NoImageCache implements ImageLoader.ImageCache {
    @Override
    public Bitmap getBitmap(String url) {
        return null;
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
    }
}
