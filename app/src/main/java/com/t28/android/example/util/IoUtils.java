package com.t28.android.example.util;

import java.io.Closeable;
import java.io.IOException;

public class IoUtils {
    private IoUtils() {
    }

    public static void close(Closeable closeable) {
        if (closeable == null) {
            return;
        }

        try {
            closeable.close();
        } catch (IOException ignore) {
        }
    }
}
