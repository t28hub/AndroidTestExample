package com.t28.android.example.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.t28.android.example.R;

public class StatefulFrameLayout extends FrameLayout {
    private static final int DEFAULT_STYLE_ATTR = 0;

    private final AttributeSet mAttrs;

    private State mState = State.SUCCESS;
    private View mLoadingView;
    private View mSuccessView;
    private View mFailureView;

    public StatefulFrameLayout(Context context) {
        this(context, null, DEFAULT_STYLE_ATTR);
    }

    public StatefulFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, DEFAULT_STYLE_ATTR);
    }

    public StatefulFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mAttrs = attrs;
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

        final Context context = getContext();
        final TypedArray typedArray = context.obtainStyledAttributes(mAttrs, R.styleable.StatefulFrameLayout);
        final int loadingLayoutId = typedArray.getResourceId(R.styleable.StatefulFrameLayout_loadingLayout, View.NO_ID);
        final int successLayoutId = typedArray.getResourceId(R.styleable.StatefulFrameLayout_successLayout, View.NO_ID);
        final int failureLayoutId = typedArray.getResourceId(R.styleable.StatefulFrameLayout_failureLayout, View.NO_ID);
        typedArray.recycle();

        final LayoutInflater inflater = LayoutInflater.from(context);
        mLoadingView = inflater.inflate(loadingLayoutId, this, true);
        mSuccessView = inflater.inflate(successLayoutId, this, true);
        mFailureView = inflater.inflate(failureLayoutId, this, true);
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
