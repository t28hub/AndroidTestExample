package com.t28.android.example.test;

import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.matcher.ViewMatchers;

import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;

public class Assertions {
    private Assertions() {
    }

    public static ViewAssertion isVisible() {
        return hasVisibility(ViewMatchers.Visibility.VISIBLE);
    }

    public static ViewAssertion isInvisible() {
        return hasVisibility(ViewMatchers.Visibility.INVISIBLE);
    }

    public static ViewAssertion isGone() {
        return hasVisibility(ViewMatchers.Visibility.GONE);
    }

    private static ViewAssertion hasVisibility(ViewMatchers.Visibility visibility) {
        return matches(withEffectiveVisibility(visibility));
    }
}
