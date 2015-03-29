package com.t28.android.example.test;

import android.support.annotation.IdRes;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.t28.android.example.test.matcher.ChildIdMatcher;
import com.t28.android.example.test.matcher.PageMatcher;

import org.hamcrest.Matcher;

public class Matchers {
    private Matchers() {
    }

    public static Matcher<View> atPage(ViewPager pager, int index) {
        return new PageMatcher(pager, index);
    }

    public static Matcher<View> withChildId(Matcher<View> parentMatcher, @IdRes int childId) {
        return new ChildIdMatcher(parentMatcher, childId);
    }
}
