package com.t28.android.example.volley;

/**
 * ステータスコード
 * <p>
 * NOTE: ApacheのHttpStatusが非推奨になったため本クラスを採用する。
 * </p>
 */
public enum StatusCode {
    OK(200),
    CREATED(201),
    ACCEPTED(202),
    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    FORBIDDEN(403),
    NOT_FOUND(404),
    INTERNAL_SERVER_ERROR(500),
    SERVICE_UNAVAILABLE(503);

    private final int mCode;

    /**
     * コンストラクタ
     *
     * @param code ステータスコードの整数表現
     */
    StatusCode(int code) {
        mCode = code;
    }

    /**
     * 整数表現に変換する
     *
     * @return ステータスコードの整数表現
     */
    public final int toInt() {
        return mCode;
    }
}
