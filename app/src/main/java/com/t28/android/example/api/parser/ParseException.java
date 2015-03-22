package com.t28.android.example.api.parser;

/**
 * パースに失敗した時に送出する例外クラス
 */
public class ParseException extends Exception {
    private static final long serialVersionUID = 4241998555954298353L;

    /**
     * コンストラクタ
     *
     * @param message 詳細メッセージ
     */
    public ParseException(String message) {
        super(message);
    }

    /**
     * コンストラクタ
     *
     * @param throwable 原因となった例外
     */
    public ParseException(Throwable throwable) {
        super(throwable);
    }
}
