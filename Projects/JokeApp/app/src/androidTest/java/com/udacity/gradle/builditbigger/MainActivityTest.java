package com.udacity.gradle.builditbigger;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsNot.not;

// Step 4:
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    // I went to the tellJoke method in MainActivity and Ctrl+Shift+T to generate the test
    // framework. Then I referenced the ButtonClickTest as a next step. Afterwards, I searched for
    // how to find the empty string with "withText". There was an example very similar though I did
    // not copy it from stackOverflow which described "not" to achieve this. Lastly for completeness
    // , I added null check as well.

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<>(
            MainActivity.class);

    @Test
    public void tellJoke() {
        onView(withId(R.id.tell_joke)).perform(click());
        onView(withId(R.id.show_joke)).check(matches(not(withText(""))));
        onView(withId(R.id.show_joke)).check(matches(not(withText((String) null))));
    }
}