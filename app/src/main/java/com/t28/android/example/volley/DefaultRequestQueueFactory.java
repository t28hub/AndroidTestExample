package com.t28.android.example.volley;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class DefaultRequestQueueFactory implements RequestQueueFactory {

    public DefaultRequestQueueFactory() {
    }

    @NonNull
    @Override
    public RequestQueue create(Context context) {
        return Volley.newRequestQueue(context);
    }
}
