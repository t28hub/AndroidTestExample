package com.t28.android.example.test.assertion;

import android.net.Uri;

/**
 * {@link com.t28.android.example}のテストで利用するアサーションのエントリポイント
 */
public class Assertions extends org.assertj.core.api.Assertions {
    private Assertions() {
    }

    /**
     * <code>{@link com.t28.android.example.test.assertion.UriAssert}</code>の生成
     *
     * @param actual 検証する値
     * @return Uriのアサーションオブジェクト
     */
    public static UriAssert assertThat(Uri actual) {
        return new UriAssert(actual);
    }
}
