package com.t28.android.example.volley;

import com.android.volley.Request;
import com.android.volley.RequestQueue;

/**
 * 全てのリクエストをフィルタリングするRequestFilter
 */
public class AnyRequestFilter implements RequestQueue.RequestFilter {
    /**
     * コンストラクタ
     */
    public AnyRequestFilter() {
    }

    @Override
    public boolean apply(Request<?> request) {
        return true;
    }
}
