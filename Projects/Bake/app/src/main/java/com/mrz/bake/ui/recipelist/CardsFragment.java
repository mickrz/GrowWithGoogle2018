/*
 * Author: M. Rzucidlo
 * Created as part of the Google - Udacity scholarship (Android Developer Nanodegree program).
 * Project 4: Baking App.
 * Copyright (c) 2018. All rights reserved.
 * Last modified: 8/13/18 11:18 PM
 */

package com.mrz.bake.ui.recipelist;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mrz.bake.utils.OnRecipeSharingListener;
import com.mrz.bake.R;
import com.mrz.bake.data.network.RecipeRetrievalService;
import com.mrz.bake.data.network.RetrofitInstance;
import com.mrz.bake.data.database.model.Recipe;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CardsFragment extends Fragment implements CardViewAdapter.ListItemClickListener {

    private static final String TAG = CardsFragment.class.getSimpleName();
    private static final String RECIPE_LIST = "recipelist";
    private List<Recipe> mRecipeList;
    private Recipe mRecipe;
    private int mPosition = RecyclerView.NO_POSITION;
    private RecyclerView mCardsFragmentRecyclerView;
    private CardViewAdapter mCardViewAdapter;
    private GridLayoutManager mGridLayoutManager;

    OnRecipeSharingListener mOnRecipeSharingListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //Log.v(TAG, Thread.currentThread().getStackTrace()[5].getMethodName());
        Log.v(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.cards_fragment, container, false);

        setupRecyclerview(view);
        if (savedInstanceState == null) {
            retrieveRecipes();
        }

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mOnRecipeSharingListener = (OnRecipeSharingListener) context;
    }

    private void setupRecyclerview(View view) {
        Log.v(TAG, Thread.currentThread().getStackTrace()[5].getMethodName());

        mCardsFragmentRecyclerView = view.findViewById(R.id.recycler_view);

        /** this setting improves performance since changes in the content do not change the layout
         *  size of the RecyclerView
         */
        // TODO: make spanCount configurable
        mCardsFragmentRecyclerView.setHasFixedSize(true);
        mGridLayoutManager = new GridLayoutManager(getContext(), 1,
                GridLayoutManager.VERTICAL,false);
        mCardsFragmentRecyclerView.setLayoutManager(mGridLayoutManager);

        /** Specify an adapter*/
        Log.v(TAG,"Creating new CardAdapter");
        mCardViewAdapter = new CardViewAdapter(getContext(), this);
        mCardsFragmentRecyclerView.setAdapter(mCardViewAdapter);
        mCardsFragmentRecyclerView.setSaveEnabled(true);

        Log.v(TAG,"Set CardAdapter to RecyclerView");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, Thread.currentThread().getStackTrace()[5].getMethodName());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        //Log.v(TAG, Thread.currentThread().getStackTrace()[5].getMethodName());
        Log.v(TAG, "onViewStateRestored");
        if (savedInstanceState != null) {
            Parcelable state = savedInstanceState.getParcelable(getString(R.string.recipe_position));
            mGridLayoutManager.onRestoreInstanceState(state);
            mRecipeList = savedInstanceState.getParcelableArrayList(RECIPE_LIST);
            mCardViewAdapter.setResults(mRecipeList);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        Log.v(TAG, "onSaveInstanceState");
        outState.putParcelableArrayList(RECIPE_LIST, new ArrayList<Parcelable>(mRecipeList));
        outState.putParcelable(getString(R.string.recipe_position),  mGridLayoutManager.onSaveInstanceState());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.v(TAG, Thread.currentThread().getStackTrace()[5].getMethodName());
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Log.v(TAG, Thread.currentThread().getStackTrace()[5].getMethodName());
        Log.v(TAG, "position in onListItemClick: " + String.valueOf(clickedItemIndex));

        mRecipe = mRecipeList.get(clickedItemIndex);
        Log.d(TAG, "Recipe Name: " + mRecipe.getName());
        mOnRecipeSharingListener.onShareRecipe(mRecipe);
    }

    private void retrieveRecipes() {
        Log.v(TAG, Thread.currentThread().getStackTrace()[5].getMethodName());
        RecipeRetrievalService recipeRetrievalService = RetrofitInstance
                .getRetrofitInstance(getString(R.string.baking_json_url))
                .create(RecipeRetrievalService.class);

        final Call<List<Recipe>> recipeResultsCall = recipeRetrievalService.getListOfRecipes();

        recipeResultsCall.enqueue(new Callback<List<Recipe>>() {
            /**
             *
             * @param call
             * @param response
             */
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                int responseCode = response.code();
                Log.v(TAG, Thread.currentThread().getStackTrace()[5].getMethodName()
                        + "- Response Code: " + responseCode);

                if (response.isSuccessful()) {
                    mRecipeList = response.body();
                    mCardViewAdapter.setResults(mRecipeList);
                    mCardViewAdapter.notifyDataSetChanged();
                }
            }

            /**
             *
             * @param call
             * @param t
             */
            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.v(TAG, Thread.currentThread().getStackTrace()[5].getMethodName());
                t.printStackTrace();
            }
        });
    }
}
