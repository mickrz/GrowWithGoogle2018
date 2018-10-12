/*
 * Author: M. Rzucidlo
 * Created as part of the Google - Udacity scholarship (Android Developer Nanodegree program).
 * Project 3: Popular Movies, Stage 2.
 * Copyright (c) 2018. All rights reserved.
 * Last modified: 7/28/18 7:15 PM
 *
 */

package com.mrz.popularmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mrz.popularmovies.R;
import com.mrz.popularmovies.model.TMDBTrailer;
import com.mrz.popularmovies.model.TMDBTrailerResults;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {
    private static final String TAG = TrailerAdapter.class.getSimpleName();
    /**
     * An on-click handler that we've defined to make it easy for an Activity to interface with
     * our RecyclerView
     */
    final private ListItemClickListener mOnClickListener;
    private TMDBTrailerResults mTMDBTrailerResults;

    /**
     * The interface that receives onClick messages.
     */
    public interface ListItemClickListener {
        void onTrailerListItemClick(int clickedItemIndex);
    }

    /**
     * Constructor for TrailerAdapter that accepts a number of items to display and the specification
     * for the ListItemClickListener.
     *
     */
    public TrailerAdapter(ListItemClickListener listener) {
        Log.v(TAG, "TrailerAdapter");
        mOnClickListener = listener;
    }

    /**
     *
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param viewGroup The ViewGroup that these ViewHolders are contained within.
     * @param viewType  If your RecyclerView has more than one type of item you
     *                  can use this viewType integer to provide a different layout. See
     *                  {@link android.support.v7.widget.RecyclerView.Adapter#getItemViewType(int)}
     *                  for more details.
     * @return A new TrailerViewHolder that holds the View for each list item
     */
    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Log.v(TAG, "onCreateViewHolder");
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.trailer_list_item, viewGroup, false);
        TrailerViewHolder viewHolder = new TrailerViewHolder(view);

        return viewHolder;
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the correct
     * indices in the list for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        Log.d(TAG, "#" + position);
        holder.bind(position);
    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our forecast
     */
    @Override
    public int getItemCount() {
        if (mTMDBTrailerResults != null) {
            return mTMDBTrailerResults.getSize();
        }
        return 0;
    }

    public void setResults(TMDBTrailerResults tmdbResults) {
        mTMDBTrailerResults = tmdbResults;
        notifyDataSetChanged();
    }

    /**
     * Cache of the children views for a list item.
     */
    public class TrailerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView listItemTrailer_tv;

        /**
         * Constructor for our ViewHolder. Within this constructor, we get a reference to our
         * TextViews and set an onClickListener to listen for clicks. Those will be handled in the
         * onClick method below.
         * @param itemView The View that you inflated in
         *                 {@linkTrailerAdapter#onCreateViewHolder(ViewGroup, int)}
         */
        public TrailerViewHolder(View itemView) {
            super(itemView);
            Log.v(TAG, "TrailerViewHolder");
            listItemTrailer_tv = itemView.findViewById(R.id.trailer_tv);
            itemView.setOnClickListener(this);
        }

        /**
         *
         * @param listIndex
         */
        void bind(int listIndex) {
            listItemTrailer_tv.setText(mTMDBTrailerResults.getResults().get(listIndex).getName());
        }

        /**
         * Called whenever a user clicks on an item in the list.
         * @param v The View that was clicked
         */
        @Override
        public void onClick(View v) {
            Log.v(TAG, "onClick");
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onTrailerListItemClick(clickedPosition);
        }
    }
}
