/*
 * Author: M. Rzucidlo
 * Created as part of the Google - Udacity scholarship (Android Developer Nanodegree program).
 * Project 4: Baking App.
 * Copyright (c) 2018. All rights reserved.
 * Last modified: 8/13/18 11:18 PM
 */

package com.mrz.bake.ui.recipe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mrz.bake.utils.OnRecipeSharingListener;
import com.mrz.bake.R;
import com.mrz.bake.data.database.model.Ingredient;
import com.mrz.bake.data.database.model.Recipe;

import java.util.List;

public class RecipeFragment extends Fragment implements RecipeAdapter.ListItemClickListener {
    private static final String TAG = RecipeFragment.class.getSimpleName();
    private static final String RECIPE = "recipe";
    private RecyclerView mRecipeFragmentRecyclerView;
    private Recipe mRecipe;
    private OnRecipeSharingListener mOnRecipeSharingListener;
    private GridLayoutManager mGridLayoutManager;
    private RecipeAdapter mRecipeAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.v(TAG, Thread.currentThread().getStackTrace()[5].getMethodName());
        View view = inflater.inflate(R.layout.recipe_fragment, container, false);

        setupRecyclerview(view);

        if (savedInstanceState == null) {
            Intent intent = getActivity().getIntent();
            mRecipe = intent.getParcelableExtra(getString(R.string.recipe_blob));
        } else {
            Parcelable state = savedInstanceState.getParcelable(getString(R.string.steps_position));
            mGridLayoutManager.onRestoreInstanceState(state);
            mRecipe = savedInstanceState.getParcelable(RECIPE);
        }

        populateUI(view);
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(RECIPE, mRecipe);
        outState.putParcelable(getString(R.string.steps_position),  mGridLayoutManager.onSaveInstanceState());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    private void setupRecyclerview(View view) {
        Log.v(TAG, Thread.currentThread().getStackTrace()[5].getMethodName());
        mRecipeFragmentRecyclerView = view.findViewById(R.id.steps_recycler_view);

        /** this setting improves performance since changes in the content do not change the layout
         *  size of the RecyclerView
         */
        // TODO: make spanCount configurable
        mRecipeFragmentRecyclerView.setHasFixedSize(true);
        mGridLayoutManager = new GridLayoutManager(getContext(), 1,
                GridLayoutManager.VERTICAL, false);
        mRecipeFragmentRecyclerView.setLayoutManager(mGridLayoutManager);

        /** Specify an adapter*/
        Log.v(TAG,"Creating new RecipeAdapter");
        mRecipeAdapter = new RecipeAdapter(this);
        mRecipeFragmentRecyclerView.setAdapter(mRecipeAdapter);
        Log.v(TAG,"Set RecipeAdapter to RecyclerView");
    }

    private void populateUI(View view) {
        TextView servings = view.findViewById(R.id.recipe_servings);
        TextView ingredients = view.findViewById(R.id.recipe_ingredients);
        String ingredient_text = "";

        if (mRecipe != null) {
            getActivity().setTitle(mRecipe.getName());
            String servings_text = getString(R.string.servings) + " " + String.valueOf(mRecipe.getServings());
            servings.setText(servings_text);
            List<Ingredient> ingredients_data = mRecipe.getIngredients();
            for (Ingredient ingredient : ingredients_data) {
              String formatQuantity = String.valueOf(ingredient.getQuantity()).replace(".0", "");
              ingredient_text += formatQuantity + " "
                      + ingredient.getMeasure().toLowerCase() + "\t"
                      + ingredient.getIngredient() + "\n";
            }

            ingredients.setText(ingredient_text);

            PreferenceManager.getDefaultSharedPreferences(getContext())
                    .edit()
                    .putString(getContext()
                    .getString(R.string.widget_recipe_name), mRecipe.getName())
                    .apply();
            PreferenceManager.getDefaultSharedPreferences(getContext())
                    .edit()
                    .putString(getContext()
                    .getString(R.string.widget_recipe_ingredients), ingredient_text)
                    .apply();

            mRecipeAdapter.setResults(mRecipe);
        } else {
            getActivity().setTitle(getString(R.string.no_recipe_found));
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mOnRecipeSharingListener = (OnRecipeSharingListener) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, Thread.currentThread().getStackTrace()[5].getMethodName());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.v(TAG, Thread.currentThread().getStackTrace()[5].getMethodName());
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Log.v(TAG, Thread.currentThread().getStackTrace()[5].getMethodName());
        Log.d(TAG, "Recipe Name: " + mRecipe.getName());
        mOnRecipeSharingListener.OnShareRecipeWithStepIndex(mRecipe, clickedItemIndex);
    }
}
