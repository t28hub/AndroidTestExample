package com.t28.android.example.test.matcher;

import android.support.annotation.IdRes;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class ChildIdMatcher extends TypeSafeMatcher<View> {
    private static final String DESCRIPTION = "with child id: ";

    private final Matcher<View> mParentMatcher;
    @IdRes
    private final int mChildViewId;

    public ChildIdMatcher(Matcher<View> parentMatcher, @IdRes int childViewId) {
        if (parentMatcher == null) {
            throw new NullPointerException("parentMatcher == null");
        }
        mParentMatcher = parentMatcher;
        mChildViewId = childViewId;

    }
    @Override
    public boolean matchesSafely(View view) {
        final ViewParent parent = view.getParent();
        if (!(parent instanceof ViewGroup)) {
            return mParentMatcher.matches(parent);
        }

        final ViewGroup container = (ViewGroup) parent;
        final View child = container.findViewById(mChildViewId);
        return mParentMatcher.matches(container) && view.equals(child);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(DESCRIPTION);
    }
}
