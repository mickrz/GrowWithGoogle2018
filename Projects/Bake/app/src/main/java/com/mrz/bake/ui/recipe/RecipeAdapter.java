/*
 * Author: M. Rzucidlo
 * Created as part of the Google - Udacity scholarship (Android Developer Nanodegree program).
 * Project 4: Baking App.
 * Copyright (c) 2018. All rights reserved.
 * Last modified: 8/13/18 7:01 PM
 */

package com.mrz.bake.ui.recipe;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mrz.bake.R;
import com.mrz.bake.data.database.model.Recipe;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {
    private static final String TAG = RecipeAdapter.class.getSimpleName();
    final private ListItemClickListener mOnClickListener;
    private Recipe mRecipe;

    /**
     * The interface that receives onClick messages.
     */
    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public RecipeAdapter(ListItemClickListener listener) {
        Log.v(TAG, Thread.currentThread().getStackTrace()[5].getMethodName());
        mOnClickListener = listener;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.v(TAG, Thread.currentThread().getStackTrace()[5].getMethodName());
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.step_list_item, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        Log.v(TAG, Thread.currentThread().getStackTrace()[5].getMethodName());
        Log.d(TAG, "#" + position);
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        Log.v(TAG, Thread.currentThread().getStackTrace()[5].getMethodName());
        if (mRecipe.getSteps() != null) {
            return mRecipe.getSteps().size();
        }
        return 0;
    }

    /**
     *
     * @param recipe
     */
    public void setResults(Recipe recipe) {
        mRecipe = recipe;
        notifyDataSetChanged();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView recipeStep;

        public RecipeViewHolder(@NonNull View view) {
            super(view);
            Log.v(TAG, Thread.currentThread().getStackTrace()[5].getMethodName());
            recipeStep = view.findViewById(R.id.recipe_step);
            view.setOnClickListener(this);
        }

        void bind(int position) {
            recipeStep.setText(mRecipe.getSteps().get(position).getShortDescription());
        }

        /**
         * Called whenever a user clicks on an item in the list.
         * @param v The View that was clicked
         */
        @Override
        public void onClick(View v) {
            Log.v(TAG, "onClick");
            int clickedPosition = getAdapterPosition();
            Log.v(TAG, "position in onClick: " + String.valueOf(clickedPosition));
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }
}
