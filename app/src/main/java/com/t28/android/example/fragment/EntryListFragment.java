package com.t28.android.example.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.t28.android.example.data.adapter.EntryListAdapter;
import com.t28.android.example.data.model.Entry;
import com.t28.android.example.data.model.Feed;
import com.t28.android.example.view.StatefulFrameLayout;
import com.t28.android.example.volley.VolleyProvider;

import java.util.concurrent.TimeUnit;

public class EntryListFragment extends Fragment implements EntryListAdapter.OnEntryClickListener {
    private static final String EXTRA_FEED_URL = "feed_url";
    private static final String EXTRA_ENTRY_COUNT = "entry_count";

    private String mFeedUrl;
    private int mEntryCount;
    private Request mFeedRequest;
    private EntryListAdapter mEntryListAdapter;

    /**
     * コンストラクタ
     *
     * @see #newInstance(String, int)
     */
    public EntryListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Bundle arguments = getArguments();
        if (arguments != null) {
            mFeedUrl = arguments.getString(EXTRA_FEED_URL);
            mEntryCount = arguments.getInt(EXTRA_ENTRY_COUNT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final StatefulFrameLayout layout = (StatefulFrameLayout) inflater.inflate(R.layout.fragment_entry_list, container, false);
        final RecyclerView entryListView = (RecyclerView) layout.getSuccessView();
        entryListView.setLayoutManager(new LinearLayoutManager(container.getContext()));

        mEntryListAdapter = new EntryListAdapter();
        mEntryListAdapter.setOnEntryClickListener(this);
        entryListView.setAdapter(mEntryListAdapter);
        return layout;
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

    @Override
    public void onEntryClick(Entry entry) {
        final Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(entry.getUrl());
        getActivity().startActivity(intent);
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
                .setNumber(mEntryCount)
                .setSoftTimeToLive(TimeUnit.MINUTES.toMillis(10))
                .setTimeToLive(TimeUnit.HOURS.toMillis(1))
                .setListener(new Response.Listener<Feed>() {
                    @Override
                    public void onResponse(Feed response) {
                        mEntryListAdapter.changeEntries(response.getEntries());
                        getLayout().changeState(StatefulFrameLayout.State.SUCCESS);
                    }
                })
                .setErrorListener(new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        getLayout().changeState(StatefulFrameLayout.State.FAILURE);
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
        return VolleyProvider.get().getRequestQueue(getActivity());
    }

    /**
     * インスタンスを生成する
     *
     * @param feedUrl    表示するフィードのURL
     * @param entryCount 取得するエントリー数
     * @return インスタンス
     */
    public static EntryListFragment newInstance(String feedUrl, int entryCount) {
        final EntryListFragment instance = new EntryListFragment();
        final Bundle arguments = new Bundle();
        arguments.putString(EXTRA_FEED_URL, feedUrl);
        arguments.putInt(EXTRA_ENTRY_COUNT, entryCount);
        instance.setArguments(arguments);
        return instance;
    }
}
