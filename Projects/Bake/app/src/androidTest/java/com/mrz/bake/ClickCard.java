/*
 * Author: M. Rzucidlo
 * Created as part of the Google - Udacity scholarship (Android Developer Nanodegree program).
 * Project 4: Baking App.
 * Copyright (c) 2018. All rights reserved.
 * Last modified: 10/7/18 10:33 PM
 */

package com.mrz.bake;

import com.mrz.bake.ui.recipelist.CardsActivity;

import org.hamcrest.core.IsNot;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;


@RunWith(AndroidJUnit4.class)
public class ClickCard {

    private static final String NUTELLA_PIE = "Nutella Pie";
    private static final String BROWNIES = "Brownies";
    private static final String CHEESECAKE = "Cheesecake";
    private static final String LONG_DESCRIPTION = "8. Remove the pan from the oven and let cool until" +
            " room temperature. If you want to speed this up, you can feel free to put the pan in a " +
            "freezer for a bit.";

    @Rule
    public ActivityTestRule<CardsActivity> mActivityTestRule = new ActivityTestRule<>(CardsActivity.class);

    @Test
    public void findNutellaPieInRecipeList() {
        onView(withId(R.id.recycler_view))
                .perform(RecyclerViewActions
                        .scrollToPosition(0));
        onView(withText(NUTELLA_PIE))
                .check(matches(isDisplayed()));
    }

    @Test
    public void findCheesecakeInRecipeList() {
        onView(withId(R.id.recycler_view))
                .perform(RecyclerViewActions
                        .scrollToPosition(2));
        onView(withText(IsNot.not(CHEESECAKE)))
                .check(matches(isDisplayed()));
    }

    @Test
    public void viewRecipe() {
        onView(withId(R.id.recycler_view))
                .perform(RecyclerViewActions
                        .scrollToPosition(2));

        onView(withText(BROWNIES))
                .check(matches(isDisplayed()));

        onView(withId(R.id.recycler_view))
                .perform(RecyclerViewActions
                        .actionOnItemAtPosition(1, click()));

        onView(withId(R.id.steps_recycler_view))
                .perform(RecyclerViewActions
                        .actionOnItemAtPosition(8, click()));

        onView(withId(R.id.recipe_step_long_description))
                .check(matches(withText(startsWith(LONG_DESCRIPTION))));

        onView(withId(R.id.previous_button))
                .perform(click());

        onView(withId(R.id.next_button))
                .perform(click());

        onView(withId(R.id.recipe_step_long_description))
                .check(matches(withText(startsWith(LONG_DESCRIPTION))));
    }
}
