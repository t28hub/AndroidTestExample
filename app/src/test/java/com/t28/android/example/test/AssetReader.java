package com.t28.android.example.test;

import android.content.res.AssetManager;

import com.t28.android.example.util.IoUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class AssetReader {
    private static final int END_OF_FILE = -1;
    private static final int DEFAULT_BUFFER_SIZE = 1024;

    private final AssetManager mManager;
    private final int mBufferSize;

    public AssetReader(AssetManager manager) {
        this(manager, DEFAULT_BUFFER_SIZE);
    }

    public AssetReader(AssetManager manager, int bufferSize) {
        if (manager == null) {
            throw new NullPointerException("manager == null");
        }
        if (bufferSize <= 0) {
            throw new IllegalArgumentException("bufferSize must not be equal or less than 0");
        }
        mManager = manager;
        mBufferSize = bufferSize;
    }

    public byte[] read(String fileName) throws IOException {
        BufferedInputStream input = null;
        ByteArrayOutputStream output = null;
        try {
            input = new BufferedInputStream(mManager.open(fileName));
            output = new ByteArrayOutputStream();
            final byte[] buffer = new byte[mBufferSize];
            while (input.read(buffer) != END_OF_FILE) {
                output.write(buffer);
            }
            return output.toByteArray();
        } finally {
            IoUtils.close(input);
            IoUtils.close(output);
        }
    }
}
