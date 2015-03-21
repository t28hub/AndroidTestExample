package com.t28.android.example.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.t28.android.example.R;
import com.t28.android.example.api.request.FeedRequest;
import com.t28.android.example.data.model.Feed;
import com.t28.android.example.view.StatefulFrameLayout;
import com.t28.android.example.volley.VolleyHolder;

import java.util.concurrent.TimeUnit;

public class EntryListFragment extends Fragment {
    private static final String EXTRA_FEED_URL = "feed_url";

    private String mFeedUrl;
    private Request mFeedRequest;

    /**
     * コンストラクタ
     *
     * @see #newInstance(String)
     */
    public EntryListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Bundle arguments = getArguments();
        if (arguments != null) {
            mFeedUrl = arguments.getString(EXTRA_FEED_URL);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_entry_list, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isRefreshNeeded()) {
            getLayout().changeState(StatefulFrameLayout.State.LOADING);
            refresh();
            return;
        }
        getLayout().changeState(StatefulFrameLayout.State.SUCCESS);
    }

    @Override
    public void onPause() {
        super.onPause();
        cancel();
    }

    private boolean isRefreshNeeded() {
        if (mFeedRequest == null) {
            return true;
        }

        final Cache cache = getRequestQueue().getCache();
        final Cache.Entry entry = cache.get(mFeedRequest.getCacheKey());
        return entry == null || entry.refreshNeeded();
    }

    private void refresh() {
        mFeedRequest = new FeedRequest.Builder(mFeedUrl)
                .setNumber(100)
                .setSoftTimeToLive(TimeUnit.MINUTES.toMillis(10))
                .setTimeToLive(TimeUnit.HOURS.toMillis(1))
                .setListener(new Response.Listener<Feed>() {
                    @Override
                    public void onResponse(Feed response) {
                    }
                })
                .setErrorListener(new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                })
                .build();
        getRequestQueue().add(mFeedRequest);
    }

    private void cancel() {
        if (mFeedRequest != null) {
            mFeedRequest.cancel();
        }
    }

    private StatefulFrameLayout getLayout() {
        return (StatefulFrameLayout) getView();
    }

    private RequestQueue getRequestQueue() {
        return VolleyHolder.getRequestQueue(getActivity());
    }

    /**
     * インスタンスを生成する
     *
     * @param feedUrl 表示するフィードのURL
     * @return インスタンス
     */
    public static EntryListFragment newInstance(String feedUrl) {
        final EntryListFragment instance = new EntryListFragment();
        final Bundle arguments = new Bundle();
        arguments.putString(EXTRA_FEED_URL, feedUrl);
        instance.setArguments(arguments);
        return instance;
    }
}
