package com.t28.android.example.volley;

import com.android.volley.Request;

/**
 * リクエストメソッドのパターン
 */
public enum MethodPattern {
    /**
     * 任意のメソッド
     */
    ANY {
        @Override
        boolean match(int method) {
            return true;
        }
    },

    /**
     * GETメソッド
     */
    GET {
        @Override
        boolean match(int method) {
            return method == Request.Method.GET;
        }
    },

    /**
     * POSTメソッド
     */
    POST {
        @Override
        boolean match(int method) {
            return method == Request.Method.POST;
        }
    },

    /**
     * GETメソッドまたはPOSTメソッド
     */
    GET_OR_POST {
        @Override
        boolean match(int method) {
            return method == Request.Method.GET || method == Request.Method.POST;
        }
    };

    /**
     * メソッドがマッチするか検証する
     *
     * @param method リクエストメソッド
     * @return マッチする場合<em>true</em>
     */
    abstract boolean match(int method);
}
