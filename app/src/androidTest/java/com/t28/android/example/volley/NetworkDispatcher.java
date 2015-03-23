package com.t28.android.example.volley;

import android.support.annotation.NonNull;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class NetworkDispatcher {
    private final Map<RequestMatcher, NetworkResponse> mExpectedResponseMap;

    /**
     * コンストラクタ
     */
    public NetworkDispatcher() {
        // 追加順序を担保するためにLinkedHashMapを採用する
        mExpectedResponseMap = new LinkedHashMap<>();
    }

    /**
     * リクエストに該当するレスポンスを振り分ける
     *
     * @param request リクエスト
     * @return リクエストに該当するレスポンス（存在しない場合は<em>null</em>）
     */
    public NetworkResponse dispatch(@NonNull Request<?> request) {
        final Set<Map.Entry<RequestMatcher, NetworkResponse>> entries = mExpectedResponseMap.entrySet();
        for (Map.Entry<RequestMatcher, NetworkResponse> entry : entries) {
            final RequestMatcher matcher = entry.getKey();
            if (!matcher.match(request)) {
                continue;
            }
            return entry.getValue();
        }
        return null;
    }

    public void append(RequestMatcher matcher, NetworkResponse response) {
        if (matcher == null) {
            throw new NullPointerException("matcher == null");
        }
        if (response == null) {
            throw new NullPointerException("response == null");
        }
        mExpectedResponseMap.put(matcher, response);
    }

    public void clear() {
        mExpectedResponseMap.clear();
    }
}
