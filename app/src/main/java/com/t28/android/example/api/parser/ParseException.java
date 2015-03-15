package com.t28.android.example.api.parser;

public class ParseException extends Exception {
    private static final long serialVersionUID = 4241998555954298353L;

    public ParseException(String message) {
        super(message);
    }

    public ParseException(Throwable throwable) {
        super(throwable);
    }
}
