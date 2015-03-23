package com.t28.android.example.test;

import android.content.res.AssetManager;

import com.t28.android.example.util.IoUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * assetsディレクトリ以下のファイルを読み込むクラス
 */
public class AssetReader {
    private static final int END_OF_FILE = -1;
    private static final int DEFAULT_BUFFER_SIZE = 1024;

    private final AssetManager mManager;
    private final int mBufferSize;

    /**
     * コンストラクタ
     *
     * @param manager 該当assetsに対応するAssetsManager
     */
    public AssetReader(AssetManager manager) {
        this(manager, DEFAULT_BUFFER_SIZE);
    }

    /**
     * コンストラクタ
     *
     * @param manager    該当assetsに対応するAssetsManager
     * @param bufferSize ファイル読み込み時のバッファサイズ
     */
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

    /**
     * ファイルの読込
     *
     * @param fileName ファイル名
     * @return ファイルの内容
     * @throws IOException ファイル読込に失敗した場合
     */
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
