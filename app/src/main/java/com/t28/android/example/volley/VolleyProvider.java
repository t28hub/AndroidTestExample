package com.t28.android.example.volley;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;

public class VolleyProvider {
    private static final RequestQueueFactory DEFAULT_REQUEST_QUEUE_FACTORY = new DefaultRequestQueueFactory();

    private static RequestQueueFactory sRequestQueueFactory = DEFAULT_REQUEST_QUEUE_FACTORY;
    private static RequestQueue sRequestQueue;
    private static ImageLoader sImageLoader;

    private VolleyProvider() {
    }

    public static VolleyProvider get() {
        return InstanceHolder.INSTANCE;
    }

    public static void injectRequestQueueFactory(@NonNull RequestQueueFactory factory) {
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
            sRequestQueue.start();
        }
        return sRequestQueue;
    }

    private static class InstanceHolder {
        private static final VolleyProvider INSTANCE = new VolleyProvider();

        private InstanceHolder() {
        }
    }
}
