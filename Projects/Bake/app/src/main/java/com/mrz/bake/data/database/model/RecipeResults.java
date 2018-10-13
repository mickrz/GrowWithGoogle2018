/*
 * Author: M. Rzucidlo
 * Created as part of the Google - Udacity scholarship (Android Developer Nanodegree program).
 * Project 4: Baking App.
 * Copyright (c) 2018. All rights reserved.
 * Last modified: 8/4/18 7:08 PM
 */

package com.mrz.bake.data.database.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RecipeResults {
    @SerializedName("results")
    private List<Recipe> mRecipeResults;

    public RecipeResults(List<Recipe> recipeList) {
        mRecipeResults = recipeList;
    }

    public List<Recipe> getResults() {
        return mRecipeResults;
    }

    public int getSize() { return mRecipeResults.size(); }

}
