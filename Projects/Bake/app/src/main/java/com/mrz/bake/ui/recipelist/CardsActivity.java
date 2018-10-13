/*
 * Author: M. Rzucidlo
 * Created as part of the Google - Udacity scholarship (Android Developer Nanodegree program).
 * Project 4: Baking App.
 * Copyright (c) 2018. All rights reserved.
 * Last modified: 10/3/18 7:31 PM
 */

package com.mrz.bake.ui.recipelist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.mrz.bake.utils.OnRecipeSharingListener;
import com.mrz.bake.R;
import com.mrz.bake.data.database.model.Recipe;
import com.mrz.bake.ui.recipe.RecipeActivity;

public class CardsActivity extends AppCompatActivity implements OnRecipeSharingListener {
    private static final String TAG = CardsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards);
    }

    @Override
    public void onShareRecipe(Recipe recipe) {
        Log.d(TAG, "Recipe Name: " + recipe.getName());

        Intent intent = new Intent(CardsActivity.this, RecipeActivity.class);
        intent.putExtra(getString(R.string.recipe_blob), recipe);
        startActivity(intent);
    }

    @Override
    public void OnShareRecipeWithStepIndex(Recipe recipe, int stepIndex) {
      // nothing to do here...
    }
}
