package com.t28.android.example.test.matcher;

import android.support.v4.view.ViewPager;
import android.view.View;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.lang.ref.WeakReference;

public class PageMatcher extends TypeSafeMatcher<View> {
    private static final String DESCRIPTION_FORMAT = "at index: %d";

    private final WeakReference<ViewPager> mViewPager;
    private final int mPageIndex;

    public PageMatcher(ViewPager pager, int index) {
        mViewPager = new WeakReference<>(pager);
        mPageIndex = index;
    }

    @Override
    public boolean matchesSafely(View view) {
        final ViewPager pager = getViewPager();
        if (pager == null) {
            return false;
        }

        final View expected = pager.getChildAt(mPageIndex);
        return view.equals(expected);
    }

    @Override
    public void describeTo(Description description) {
        final String text = String.format(DESCRIPTION_FORMAT, mPageIndex);
        description.appendText(text);
    }

    private ViewPager getViewPager() {
        return mViewPager.get();
    }
}
