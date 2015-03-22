package com.t28.android.example.volley;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.volley.RequestQueue;

public interface RequestQueueFactory {
    @NonNull
    RequestQueue create(Context context);
}
