package com.t28.android.example.pages;

import android.support.test.espresso.ViewInteraction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewParent;
import android.widget.TextView;

import com.t28.android.example.R;
import com.t28.android.example.activity.MainActivity;
import com.t28.android.example.view.SlidingTabLayout;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;

public class Main {
    @InjectView(R.id.main_toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.main_sliding_tab)
    SlidingTabLayout mSlidingTabLayout;
    @InjectView(R.id.main_view_pager)
    ViewPager mViewPager;

    private final MainActivity mActivity;

    public Main(MainActivity activity) {
        if (activity == null) {
            throw new NullPointerException("activity == null");
        }
        mActivity = activity;
        ButterKnife.inject(this, mActivity);
    }

    public void showOptionsMenu() {
        mToolbar.showOverflowMenu();
    }

    public void hideOptionsMenu() {
        mToolbar.hideOverflowMenu();
    }

    public void clickTabAt(int position) {
        final ViewInteraction tabInteraction = findTabAt(position);
        tabInteraction.perform(click());
    }

    private ViewInteraction findTabAt(int position) {
        final PagerAdapter adapter = getPagerAdapter();
        final CharSequence title = adapter.getPageTitle(position);
        return onView(new SlidingTabTitleMatcher(title));
    }

    private PagerAdapter getPagerAdapter() {
        return mViewPager.getAdapter();
    }

    static class SlidingTabTitleMatcher extends TypeSafeMatcher<View> {
        private final CharSequence mTitle;

        SlidingTabTitleMatcher(CharSequence title) {
            if (TextUtils.isEmpty(title)) {
                throw new IllegalArgumentException("'title' must not be empty");
            }
            mTitle = title;
        }

        @Override
        public boolean matchesSafely(View view) {
            final ViewParent parent = view.getParent();
            if (!(parent instanceof SlidingTabLayout)) {
                return false;
            }

            if (!(view instanceof TextView)) {
                return false;
            }

            final TextView textView = (TextView) view;
            return TextUtils.equals(mTitle, textView.getText());
        }

        @Override
        public void describeTo(Description description) {
        }
    }
}
