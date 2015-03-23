package com.t28.android.example.volley;

import android.text.TextUtils;

import com.android.volley.NetworkResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * {@link com.android.volley.NetworkResponse}の生成器
 */
public class NetworkResponseBuilder {
    private static final byte[] EMPTY_BODY = "".getBytes();

    private final Map<String, String> mHeaders;

    private StatusCode mStatusCode = StatusCode.OK;
    private byte[] mBody = EMPTY_BODY;
    private boolean mNotModified;
    private long mNetworkTimeMs;

    /**
     * コンストラクタ
     */
    public NetworkResponseBuilder() {
        mHeaders = new HashMap<>();
    }

    /**
     * ステータスコードの設定
     *
     * @param statusCode ステータスコード
     * @return 自身のインスタンス
     */
    public NetworkResponseBuilder setStatusCode(StatusCode statusCode) {
        mStatusCode = statusCode;
        return this;
    }

    /**
     * ヘッダーの追加
     *
     * @param name  ヘッダー名
     * @param value ヘッダーの値
     * @return 自身のインスタンス
     * @see #addHeaders(java.util.Map)
     */
    public NetworkResponseBuilder addHeader(String name, String value) {
        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(value)) {
            mHeaders.put(name, value);
        }
        return this;
    }

    /**
     * ヘッダーの追加
     *
     * @param headers ヘッダー
     * @return 自身のインスタンス
     * @see #addHeader(String, String)
     */
    public NetworkResponseBuilder addHeaders(Map<String, String> headers) {
        if (headers != null) {
            mHeaders.putAll(headers);
        }
        return this;
    }

    /**
     * ボディの設定
     *
     * @param body ボディ
     * @return 自身のインスタンス
     */
    public NetworkResponseBuilder setBody(byte[] body) {
        if (body == null) {
            mBody = EMPTY_BODY;
        } else {
            mBody = body;
        }
        return this;
    }

    /**
     * レスポンスに変更があったか設定する
     * <p>
     * NOTE: サーバが304を返却した場合は変更がない。
     * </p>
     *
     * @param notModified <em>true</em>の場合は変更なし
     * @return 自身のインスタンス
     */
    public NetworkResponseBuilder setNotModified(boolean notModified) {
        mNotModified = notModified;
        return this;
    }

    /**
     * レスポンスが返却されるまでにかかった時間を設定する
     *
     * @param timeMs 時間（ミリ秒）
     * @return 自身のインスタンス
     */
    public NetworkResponseBuilder setNetworkTimeMs(long timeMs) {
        mNetworkTimeMs = timeMs;
        return this;
    }

    /**
     * {@link com.android.volley.NetworkResponse}の生成
     *
     * @return NetworkResponse
     */
    public NetworkResponse build() {
        final int statusCode = mStatusCode.toInt();
        final byte[] body = mBody.clone();
        final Map<String, String> headers = new HashMap<>(mHeaders);
        return new NetworkResponse(statusCode, body, headers, mNotModified, mNetworkTimeMs);
    }
}
