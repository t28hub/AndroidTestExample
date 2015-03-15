package com.t28.android.example.api.parser;

import android.support.annotation.NonNull;

public interface Parser<T> {
    T parse(@NonNull byte[] data) throws ParseException;
}
