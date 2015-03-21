package com.t28.android.example.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.t28.android.example.R;

public class StatefulFrameLayout extends FrameLayout {
    private static final int DEFAULT_STYLE_ATTR = 0;

    private final int mLoadingLayoutId;
    private final int mSuccessLayoutId;
    private final int mFailureLayoutId;

    private State mState;
    private View mLoadingView;
    private View mSuccessView;
    private View mFailureView;

    public StatefulFrameLayout(Context context) {
        this(context, null);
    }

    public StatefulFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.StatefulFrameLayout);
        mLoadingLayoutId = typedArray.getResourceId(R.styleable.StatefulFrameLayout_loadingLayout, View.NO_ID);
        mSuccessLayoutId = typedArray.getResourceId(R.styleable.StatefulFrameLayout_successLayout, View.NO_ID);
        mFailureLayoutId = typedArray.getResourceId(R.styleable.StatefulFrameLayout_failureLayout, View.NO_ID);
        typedArray.recycle();
    }

    public State getState() {
        return mState;
    }

    public void changeState(State state) {
        if (state == null || state == mState) {
            return;
        }
        state.apply(this);
        mState = state;
    }

    public View getLoadingView() {
        return mLoadingView;
    }

    public View getSuccessView() {
        return mSuccessView;
    }

    public View getFailureView() {
        return mFailureView;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        detachViews(mLoadingView, mSuccessView, mFailureView);

        mLoadingView = findViewById(mLoadingLayoutId);
        mSuccessView = findViewById(mSuccessLayoutId);
        mFailureView = findViewById(mFailureLayoutId);

        changeState(mState);
    }

    private void checkViews(View... views) {
        for (View view: views) {
            if (view != null) {
                continue;
            }
        }
    }

    private void detachViews(View... views) {
        for (View view : views) {
            if (view == null) {
                continue;
            }
            removeView(view);
        }
    }

    public static enum State {
        LOADING {
            @Override
            void apply(StatefulFrameLayout layout) {
                layout.mLoadingView.setVisibility(View.VISIBLE);
                layout.mSuccessView.setVisibility(View.GONE);
                layout.mFailureView.setVisibility(View.GONE);
            }
        },
        SUCCESS {
            @Override
            void apply(StatefulFrameLayout layout) {
                layout.mLoadingView.setVisibility(View.GONE);
                layout.mSuccessView.setVisibility(View.VISIBLE);
                layout.mFailureView.setVisibility(View.GONE);
            }
        },
        FAILURE {
            @Override
            void apply(StatefulFrameLayout layout) {
                layout.mLoadingView.setVisibility(View.GONE);
                layout.mSuccessView.setVisibility(View.GONE);
                layout.mFailureView.setVisibility(View.VISIBLE);
            }
        };

        abstract void apply(StatefulFrameLayout layout);
    }
}
