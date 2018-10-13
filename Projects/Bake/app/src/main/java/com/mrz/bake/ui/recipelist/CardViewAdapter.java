/*
 * Author: M. Rzucidlo
 * Created as part of the Google - Udacity scholarship (Android Developer Nanodegree program).
 * Project 4: Baking App.
 * Copyright (c) 2018. All rights reserved.
 * Last modified: 8/13/18 10:59 PM
 */

package com.mrz.bake.ui.recipelist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mrz.bake.R;
import com.mrz.bake.data.database.model.Recipe;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.StatsSnapshot;

import java.util.List;

import static com.mrz.bake.utils.PicassoInstance.getPicassoInstance;

public class CardViewAdapter extends RecyclerView.Adapter<CardViewAdapter.CardViewHolder> {

    private static final String TAG = CardViewAdapter.class.getSimpleName();
    private List<Recipe> recipeList;
    private Context mContext;
    final private ListItemClickListener mOnClickListener;

    /**
     * The interface that receives onClick messages.
     */
    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    public CardViewAdapter(Context context, ListItemClickListener listener) {
        Log.v(TAG, Thread.currentThread().getStackTrace()[5].getMethodName());
        mContext = context;
        mOnClickListener = listener;
        if (getPicassoInstance(mContext) == null) {
            Picasso.setSingletonInstance(getPicassoInstance(mContext));
        }
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.v(TAG, Thread.currentThread().getStackTrace()[5].getMethodName());
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_card, parent, false);
        return new CardViewHolder(view, mOnClickListener);
    }

    /**
     *
     * @param holder
     * @param url
     */
    private void load(CardViewHolder holder, String url) {
        StatsSnapshot statsSnapshot = Picasso.with(mContext).getSnapshot();
        String cacheHits = "cache hits: " + String.valueOf(statsSnapshot.cacheHits);
        String cacheMisses = ", cache misses: " + String.valueOf(statsSnapshot.cacheMisses);
        String downloadCount = ", download count: " + String.valueOf(statsSnapshot.downloadCount);
        String totalDownloadSize = ", total download size: " + String.valueOf(statsSnapshot.totalDownloadSize);
        String size = ", size: " + String.valueOf(statsSnapshot.size);
        String timeStamp = ", timeStamp: " + String.valueOf(statsSnapshot.timeStamp);
        Log.v(TAG, cacheHits + cacheMisses + downloadCount + totalDownloadSize + size + timeStamp);

        if (!url.isEmpty()) {
            Picasso.with(mContext).invalidate(url);
            Picasso.with(mContext).cancelRequest(holder.recipeImage);
            Picasso.with(mContext).load(url)
                    .error(R.drawable.ic_twotone_error_24px)
                    .placeholder(R.drawable.ic_twotone_image_24px)
                    .priority(Picasso.Priority.HIGH)
                    .memoryPolicy(MemoryPolicy.NO_STORE)
                    .into(holder.recipeImage, new Callback() {
                        @Override
                        public void onSuccess() {
                            Log.v(TAG, "onSuccess");
                        }

                        @Override
                        public void onError() {
                            Log.v(TAG, "onError");
                            Toast.makeText(mContext, mContext.getString(R.string.an_error_occurred), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Log.v(TAG, mContext.getString(R.string.no_pictures_available));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder holder, int position) {
        Log.v(TAG, Thread.currentThread().getStackTrace()[5].getMethodName());
        if (recipeList != null && position < recipeList.size()) {
            Recipe recipe = recipeList.get(position);
            // As it turns out, there are no images from the json so need to load from
            // somewhere else or include in project. Logically, what if the json changes, it
            // is bad coding to assume it will never change.
            load(holder, recipe.getImage());
            holder.recipeName.setText(recipe.getName());
            Log.v(TAG, recipe.getName());
            String servings = mContext.getString(R.string.servings) +" " + String.valueOf(recipe.getServings());
            holder.recipeServings.setText(servings);
        }
    }


    @Override
    public int getItemCount() {
        Log.v(TAG, Thread.currentThread().getStackTrace()[5].getMethodName());
        if (recipeList != null) {
            return recipeList.size();
        }
        return 0;
    }

    /**
     *
     * @param recipeResults
     */
    public void setResults(List<Recipe> recipeResults) {
        recipeList = recipeResults;
        notifyDataSetChanged();
    }

    public class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final private CardViewAdapter.ListItemClickListener mOnClickListener;

        public ImageView recipeImage;
        public TextView recipeName;
        public TextView recipeServings;

        public CardViewHolder(@NonNull View view, CardViewAdapter.ListItemClickListener listItemClickListener) {
            super(view);
            Log.v(TAG, Thread.currentThread().getStackTrace()[5].getMethodName());
            recipeImage = view.findViewById(R.id.recipe_image);
            recipeName = view.findViewById(R.id.recipe_name);
            recipeServings = view.findViewById(R.id.recipe_servings);
            view.setOnClickListener(this);
            mOnClickListener = listItemClickListener;
        }

        // TODO: onClick to adapter
        @Override
        public void onClick(View view) {
            Log.v(TAG, Thread.currentThread().getStackTrace()[5].getMethodName());
            int clickedPosition = getAdapterPosition();
            Log.v(TAG, "position in onClick: " + String.valueOf(clickedPosition));
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }
}
