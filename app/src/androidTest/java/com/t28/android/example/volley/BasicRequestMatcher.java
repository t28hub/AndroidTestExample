package com.t28.android.example.volley;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 基本的なリクエストの判定が可能なRequestMatcher
 */
public class BasicRequestMatcher implements RequestMatcher {
    private static final Pattern ANY_PATTERN = Pattern.compile(".*");

    private final MethodMatcher mMethodMatcher;
    private final Pattern mUrlPattern;
    private final Map<String, Pattern> mHeaderPatterns;
    private final Pattern mBodyPattern;

    private BasicRequestMatcher(Builder builder) {
        if (builder.mMethodMatcher == null) {
            mMethodMatcher = MethodMatcher.ANY;
        } else {
            mMethodMatcher = builder.mMethodMatcher;
        }

        mUrlPattern = compile(builder.mUrlPattern);
        mHeaderPatterns = new HashMap<>();
        mBodyPattern = compile(builder.mBodyPattern);

        final Set<Map.Entry<String, String>> entries = builder.mHeaderPatterns.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            final String name = entry.getKey();
            final String pattern = entry.getValue();
            mHeaderPatterns.put(name, compile(pattern));
        }
    }

    private static Pattern compile(String pattern) {
        if (TextUtils.isEmpty(pattern)) {
            return ANY_PATTERN;
        }
        return Pattern.compile(pattern);
    }

    @Override
    public boolean match(@NonNull Request<?> request) {
        try {
            return matchInternal(request);
        } catch (AuthFailureError authFailureError) {
            return false;
        }
    }

    private boolean matchInternal(Request<?> request) throws AuthFailureError {
        final int method = request.getMethod();
        if (!matchMethod(method)) {
            return false;
        }

        final String url = request.getUrl();
        if (!matchUrl(url)) {
            return false;
        }

        final Map<String, String> headers = request.getHeaders();
        if (!matchHeaders(headers)) {
            return false;
        }

        final byte[] body = request.getBody();
        if (!matchBody(body)) {
            return false;
        }
        return true;
    }

    private boolean matchMethod(int method) {
        return mMethodMatcher.match(method);
    }

    private boolean matchUrl(String url) {
        final Matcher matcher = mUrlPattern.matcher(url);
        return matcher.find();
    }

    private boolean matchHeaders(Map<String, String> headers) {
        final Set<Map.Entry<String, Pattern>> entries = mHeaderPatterns.entrySet();
        for (Map.Entry<String, Pattern> entry : entries) {
            final String name = entry.getKey();
            final Pattern pattern = entry.getValue();

            final String value = headers.get(name);
            final Matcher matcher = pattern.matcher(value);
            if (!matcher.find()) {
                return false;
            }
        }
        return true;
    }

    private boolean matchBody(byte[] body) {
        final String textBody;
        if (body == null) {
            textBody = "";
        } else {
            textBody = new String(body);
        }
        final Matcher matcher = mBodyPattern.matcher(textBody);
        return matcher.find();
    }

    /**
     * {@link com.t28.android.example.volley.BasicRequestMatcher}の生成器
     */
    public static final class Builder {
        private final Map<String, String> mHeaderPatterns;

        private MethodMatcher mMethodMatcher;
        private String mUrlPattern;
        private String mBodyPattern;

        /**
         * コンストラクタ
         */
        public Builder() {
            mHeaderPatterns = new HashMap<>();
        }

        /**
         * メソッドのパターンを設定
         *
         * @param matcher マッチャー
         * @return 自身のインスタンス
         */
        public Builder setMethodMatcher(MethodMatcher matcher) {
            mMethodMatcher = matcher;
            return this;
        }

        /**
         * URLのパターン設定
         *
         * @param pattern 正規表現
         * @return 自身のインスタンス
         */
        public Builder setUrlPattern(String pattern) {
            mUrlPattern = pattern;
            return this;
        }

        /**
         * ヘッダーのパターン設定
         *
         * @param name    ヘッダー名
         * @param pattern 正規表現
         * @return 自身のインスタンス
         */
        public Builder addHeaderPattern(String name, String pattern) {
            mHeaderPatterns.put(name, pattern);
            return this;
        }

        /**
         * ボディのパターン設定
         *
         * @param pattern 正規表現
         * @return 自身のインスタンス
         */
        public Builder setBodyPattern(String pattern) {
            mBodyPattern = pattern;
            return this;
        }

        /**
         * RequestMatcherの生成
         *
         * @return RequestMatcherのインスタンス
         */
        public BasicRequestMatcher build() {
            return new BasicRequestMatcher(this);
        }
    }
}
