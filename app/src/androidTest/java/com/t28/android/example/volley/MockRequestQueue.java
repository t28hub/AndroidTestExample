package com.t28.android.example.volley;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.RequestQueue;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * テスト用の{@link com.android.volley.RequestQueue}
 */
public class MockRequestQueue extends RequestQueue {
    private final AtomicBoolean mIsPaused;
    private final NetworkDispatcher mNetworkDispatcher;
    private final Queue<Request<?>> mWaitingRequests;

    /**
     * コンストラクタ
     *
     * @param cache      キャッシュ
     * @param dispatcher ディスパッチャー
     */
    public MockRequestQueue(Cache cache, NetworkDispatcher dispatcher) {
        super(cache, new MockNetwork(dispatcher));
        mIsPaused = new AtomicBoolean();
        mNetworkDispatcher = dispatcher;
        mWaitingRequests = new LinkedList<>();
    }

    @Override
    public <T> Request<T> add(Request<T> request) {
        if (mIsPaused.get()) {
            mWaitingRequests.add(request);
            return request;
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
     * リクエスト処理の再開
     */
    public void resume() {
        if (!mIsPaused.get()) {
            return;
        }
        mIsPaused.set(false);

        Request request;
        while ((request = mWaitingRequests.poll()) != null) {
            add(request);
        }
    }

    /**
     * リクエスト処理の一時停止
     */
    public void pause() {
        mIsPaused.set(true);
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
     * RequestQueueの振る舞いを検知するリスナー
     *
     * @param <T> リクエストのレスポンス型
     */
    public interface RequestQueueListener<T> {
        /**
         * リクエストが追加された時に呼び出される
         *
         * @param request 追加されたリクエスト
         */
        void onRequestAdded(Request<T> request);
    }
}
