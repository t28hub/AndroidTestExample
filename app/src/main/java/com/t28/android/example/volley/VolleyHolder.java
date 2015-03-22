package com.t28.android.example.volley;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;

public class VolleyHolder {
    private static RequestQueueFactory sRequestQueueFactory = new DefaultRequestQueueFactory();
    private static RequestQueue sRequestQueue;
    private static ImageLoader sImageLoader;

    private VolleyHolder() {
    }

    public static VolleyHolder get() {
        return InstanceHolder.INSTANCE;
    }

    public static void setRequestQueueFactory(@NonNull RequestQueueFactory factory) {
        sRequestQueueFactory = factory;
    }

    public ImageLoader getImageLoader(Context context) {
        if (sImageLoader == null) {
            sImageLoader = new ImageLoader(getRequestQueue(context), new NoImageCache());
        }
        return sImageLoader;
    }

    public RequestQueue getRequestQueue(Context context) {
        if (context == null) {
            throw new NullPointerException("context == null");
        }
        if (sRequestQueue == null) {
            sRequestQueue = sRequestQueueFactory.create(context);
        }
        return sRequestQueue;
    }

    private static class InstanceHolder {
        private static final VolleyHolder INSTANCE = new VolleyHolder();

        private InstanceHolder() {
        }
    }
}
