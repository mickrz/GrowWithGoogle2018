/*
 * Author: M. Rzucidlo
 * Created as part of the Google - Udacity scholarship (Android Developer Nanodegree program).
 * Project 4: Baking App.
 * Copyright (c) 2018. All rights reserved.
 * Last modified: 10/3/18 8:55 PM
 */

package com.mrz.bake.ui.recipesteps;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mrz.bake.utils.OnRecipeSharingListener;
import com.mrz.bake.R;
import com.mrz.bake.data.database.model.Recipe;

public class StepsActivity extends AppCompatActivity implements OnRecipeSharingListener {
    private static final String TAG = StepsActivity.class.getSimpleName();
    Recipe mRecipe;
    int mStepIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);

        Intent intent = getIntent();
        if (savedInstanceState == null) {
            if (intent == null) {
                finish();
            } else {
                mRecipe = intent.getParcelableExtra(getString(R.string.recipe_blob));
                mStepIndex = intent.getIntExtra(getString(R.string.recipe_step), 0);
            }
        }
    }

    @Override
    public void onShareRecipe(Recipe recipe) {
        // nothing to do here...
    }

    @Override
    public void OnShareRecipeWithStepIndex(Recipe recipe, int stepIndex) {
        // nothing to do here...
    }
}
