package com.t28.android.example.volley;

public enum StatusCode {
    OK(200);

    private final int mCode;

    StatusCode(int code) {
        mCode = code;
    }

    public final int toInt() {
        return mCode;
    }
}
