package com.t28.android.example.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.t28.android.example.R;
import com.t28.android.example.view.StatefulFrameLayout;

public class EntryListFragment extends Fragment {

    public EntryListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_entry_list, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getLayout().changeState(StatefulFrameLayout.State.FAILURE);
            }
        }, 5000);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getLayout().changeState(StatefulFrameLayout.State.LOADING);
            }
        }, 10000);
    }

    private StatefulFrameLayout getLayout() {
        return (StatefulFrameLayout) getView();
    }
}
