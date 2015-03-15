package com.t28.android.example.api.parser;

public interface Parser<T> {
    T parse(byte[] data) throws ParseException;
}
