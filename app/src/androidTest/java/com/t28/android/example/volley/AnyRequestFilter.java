package com.t28.android.example.volley;

import com.android.volley.Request;
import com.android.volley.RequestQueue;

/**
 * 全てのリクエストをフィルタリングするRequestFilter
 */
class AnyRequestFilter implements RequestQueue.RequestFilter {
    /**
     * コンストラクタ
     */
    AnyRequestFilter() {
    }

    @Override
    public boolean apply(Request<?> request) {
        return true;
    }
}
