/*
 * Author: M. Rzucidlo
 * Created as part of the Google - Udacity scholarship (Android Developer Nanodegree program).
 * Project 4: Baking App.
 * Copyright (c) 2018. All rights reserved.
 * Last modified: 10/3/18 8:50 PM
 */

package com.mrz.bake.ui.recipe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.mrz.bake.ui.recipesteps.StepsFragment;
import com.mrz.bake.utils.OnRecipeSharingListener;
import com.mrz.bake.R;
import com.mrz.bake.data.database.model.Recipe;
import com.mrz.bake.ui.recipesteps.StepsActivity;

public class RecipeActivity extends AppCompatActivity implements OnRecipeSharingListener {
    private static final String TAG = RecipeActivity.class.getSimpleName();
    private boolean mTwoPane = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        Recipe mRecipe;

        Intent intent = getIntent();
        if (savedInstanceState == null) {
            if (intent == null) {
                finish();
            } else {
                mRecipe = intent.getParcelableExtra(getString(R.string.recipe_blob));
                if ((findViewById(R.id.tabletrecipefragment)) != null) {
                    mTwoPane = true;
                }
            }
        }
    }

    @Override
    public void onShareRecipe(Recipe recipe) {
        // nothing to do here...
    }

    @Override
    public void OnShareRecipeWithStepIndex(Recipe recipe, int stepIndex) {
        Log.d(TAG, "Recipe name: " + recipe.getName() + ", stepIndex: " + String.valueOf(stepIndex));
        if (mTwoPane) {
            StepsFragment stepsFragment = (StepsFragment) getSupportFragmentManager().findFragmentById(R.id.tablestepsfragment);
            stepsFragment.populateUI(recipe, stepIndex, mTwoPane);
        } else {
            Intent intent = new Intent(RecipeActivity.this, StepsActivity.class);
            intent.putExtra(getString(R.string.recipe_blob), recipe);
            intent.putExtra(getString(R.string.recipe_step), stepIndex);
            startActivity(intent);
        }
    }
}
