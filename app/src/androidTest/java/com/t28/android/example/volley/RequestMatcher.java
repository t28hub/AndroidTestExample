package com.t28.android.example.volley;

import android.support.annotation.NonNull;

import com.android.volley.Request;

/**
 * リクエストの判定器
 */
public interface RequestMatcher {
    /**
     * 引数で与えられたリクエストがマッチするか判定する
     *
     * @param request 判定対象のリクエスト
     * @return <em>true</em>の場合はマッチする
     */
    boolean match(@NonNull Request<?> request);
}
