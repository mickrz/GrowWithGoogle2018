/*
 * Author: M. Rzucidlo
 * Created as part of the Google - Udacity scholarship (Android Developer Nanodegree program).
 * Project 4: Baking App.
 * Copyright (c) 2018. All rights reserved.
 * Last modified: 8/13/18 11:30 PM
 */
package com.mrz.bake.data.network;


import com.mrz.bake.data.database.model.Recipe;
import com.mrz.bake.data.database.model.RecipeResults;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

/** http://go.udacity.com/android-baking-app-json */
public interface RecipeRetrievalService {
    @GET("android-baking-app-json/")
    Call<List<Recipe>> getListOfRecipes();
}
