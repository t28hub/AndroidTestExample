package com.t28.android.example.volley;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

public class VolleyHolder {
    private static RequestQueue sRequestQueue;
    private static ImageLoader sImageLoader;

    private VolleyHolder() {
    }

    public static RequestQueue getRequestQueue(Context context) {
        if (context == null) {
            throw new NullPointerException("context == null");
        }
        if (sRequestQueue == null) {
            sRequestQueue = Volley.newRequestQueue(context);
        }
        return sRequestQueue;
    }

    public static ImageLoader getImageLoader(Context context) {
        if (sImageLoader == null) {
            sImageLoader = new ImageLoader(getRequestQueue(context), new NoImageCache());
        }
        return sImageLoader;
    }
}
