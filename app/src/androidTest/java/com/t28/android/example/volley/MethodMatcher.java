package com.t28.android.example.volley;

import com.android.volley.Request;

/**
 * リクエストメソッドの適合判定
 */
public enum MethodMatcher {
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
     * メソッドが適合するか検証する
     *
     * @param method リクエストメソッド
     * @return 適合する場合<em>true</em>
     */
    abstract boolean match(int method);
}
