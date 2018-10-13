/*
 * Author: M. Rzucidlo
 * Created as part of the Google - Udacity scholarship (Android Developer Nanodegree program).
 * Project 4: Baking App.
 * Copyright (c) 2018. All rights reserved.
 * Last modified: 10/5/18 11:06 AM
 */

package com.mrz.bake.utils;

import com.mrz.bake.data.database.model.Recipe;

/**
 * The interface that receives onShareRecipe messages.
 */
public interface OnRecipeSharingListener {
    void onShareRecipe(Recipe recipe);

    void OnShareRecipeWithStepIndex(Recipe recipe, int stepIndex);
}
