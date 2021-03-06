package com.t28.android.example.volley;

import android.support.annotation.Nullable;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.RequestQueue;

import java.util.LinkedList;
import java.util.Queue;

import javax.annotation.concurrent.NotThreadSafe;

/**
 * テスト用の{@link com.android.volley.RequestQueue}
 */
@NotThreadSafe
public class MockRequestQueue extends RequestQueue {
    private final NetworkDispatcher mNetworkDispatcher;
    private final Queue<Request<?>> mWaitingRequests;

    private boolean mIsPaused;
    private RequestAddedListener mRequestAddedListener;

    /**
     * コンストラクタ
     *
     * @param cache      キャッシュ
     * @param dispatcher ディスパッチャー
     */
    public MockRequestQueue(Cache cache, NetworkDispatcher dispatcher) {
        super(cache, new MockNetwork(dispatcher));
        mNetworkDispatcher = dispatcher;
        mWaitingRequests = new LinkedList<>();
    }

    @Override
    public <T> Request<T> add(Request<T> request) {
        if (mIsPaused) {
            mWaitingRequests.add(request);
            return request;
        }

        // pause/resume状態によらずリスナーを呼び出すと完了したリクエスト数と整合性が保てなくなるため
        if (mRequestAddedListener != null) {
            mRequestAddedListener.onRequestAdded(request);
        }
        return super.add(request);
    }

    /**
     * NetworkDispatcherの取得
     *
     * @return NetworkDispatcher
     */
    public NetworkDispatcher getNetworkDispatcher() {
        return mNetworkDispatcher;
    }

    /**
     * RequestQueueListenerを登録する
     *
     * @param listener 登録するリスナー
     */
    public void setRequestAddedListener(@Nullable RequestAddedListener listener) {
        mRequestAddedListener = listener;
    }

    /**
     * リクエスト処理の再開
     */
    public void resume() {
        if (!mIsPaused) {
            return;
        }
        mIsPaused = false;

        Request request;
        while ((request = mWaitingRequests.poll()) != null) {
            add(request);
        }
    }

    /**
     * リクエスト処理の一時停止
     */
    public void pause() {
        mIsPaused = true;
    }

    /**
     * 内部状態のクリーンアップ
     */
    public void clean() {
        mNetworkDispatcher.clear();
        mWaitingRequests.clear();
        cancelAll(new AnyRequestFilter());
    }

    /**
     * RequestQueueへのRequest追加を検知するリスナー
     */
    public interface RequestAddedListener {
        /**
         * リクエストが追加された時に呼び出される
         *
         * @param request 追加されたリクエスト
         */
        void onRequestAdded(Request request);
    }
}
