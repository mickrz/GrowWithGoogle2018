/*
 * Author: M. Rzucidlo
 * Created as part of the Google - Udacity scholarship (Android Developer Nanodegree program).
 * Project 3: Popular Movies, Stage 2.
 * Copyright (c) 2018. All rights reserved.
 * Last modified: 7/28/18 8:00 PM
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
import android.widget.Toast;

import com.mrz.popularmovies.R;
import com.mrz.popularmovies.model.TMDBInfo;
import com.mrz.popularmovies.model.TMDBResults;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.StatsSnapshot;

import java.util.List;

import static com.mrz.popularmovies.thirdparty.PicassoInstance.getPicassoInstance;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MoviePosterViewHolder> {
    private static final String TAG = MovieAdapter.class.getSimpleName();
    /**
     * An on-click handler that we've defined to make it easy for an Activity to interface with
     * our RecyclerView
     */
    final private ListItemClickListener mOnClickListener;
    private Context mContext;
    private TMDBResults mTMDBResults;

    /**
     * The interface that receives onClick messages.
     */
    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    /**
     * Constructor for MovieAdapter that accepts a number of items to display and the specification
     * for the ListItemClickListener.
     *
     */
    public MovieAdapter(ListItemClickListener listener, Context context) {
        Log.v(TAG, "MovieAdapter");
        mOnClickListener = listener;
        mContext = context;
        try {
            Picasso.setSingletonInstance(getPicassoInstance(mContext));
        } catch (IllegalStateException e) {
          Log.v(TAG, e.getMessage());
        }
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
     * @return A new MoviePosterViewHolder that holds the View for each list item
     */
    @Override
    public MoviePosterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Log.v(TAG, "onCreateViewHolder");
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.movie_poster_list_item, viewGroup, false);
        MoviePosterViewHolder viewHolder = new MoviePosterViewHolder(view);

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
    public void onBindViewHolder(MoviePosterViewHolder holder, int position) {
        Log.d(TAG, "#" + position);
        TMDBInfo tmdbInfo = mTMDBResults.getResults().get(position);
        load(holder, tmdbInfo);
    }

    /**
     *
     * @param holder
     * @param tmdbInfo
     */
    private void load(MoviePosterViewHolder holder, TMDBInfo tmdbInfo) {
        StatsSnapshot statsSnapshot = Picasso.with(mContext).getSnapshot();
        String cacheHits = "cache hits: " + String.valueOf(statsSnapshot.cacheHits);
        String cacheMisses = ", cache misses: " + String.valueOf(statsSnapshot.cacheMisses);
        String downloadCount = ", download count: " + String.valueOf(statsSnapshot.downloadCount);
        String totalDownloadSize = ", total download size: " + String.valueOf(statsSnapshot.totalDownloadSize);
        String size = ", size: " + String.valueOf(statsSnapshot.size);
        String timeStamp = ", timeStamp: " + String.valueOf(statsSnapshot.timeStamp);
        Log.v(TAG, cacheHits + cacheMisses + downloadCount + totalDownloadSize + size + timeStamp);

        String url = mContext.getString(R.string.image_base_url) + tmdbInfo.getPosterPath();
        Picasso.with(mContext).invalidate(url);
        Picasso.with(mContext).cancelRequest(holder.listItemMovieImageView);
        Picasso.with(mContext).load(url)
                .error(R.drawable.ic_twotone_error_24px)
                .placeholder(R.drawable.ic_twotone_image_24px)
                .noFade()
                .priority(Picasso.Priority.HIGH)
                .memoryPolicy(MemoryPolicy.NO_STORE)
                .into(holder.listItemMovieImageView, new Callback() {
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
    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our forecast
     */
    @Override
    public int getItemCount() {
        if (mTMDBResults != null) {
            return mTMDBResults.getSize();
        }
        return 0;
    }

    /**
     *
     * @param tmdbResults
     */
    public void setResults(TMDBResults tmdbResults) {
        mTMDBResults = tmdbResults;
        notifyDataSetChanged();
    }

    public void setCachedResults(List<TMDBInfo> tmdbInfo) {
        mTMDBResults = new TMDBResults(tmdbInfo, 1,1,1);
        notifyDataSetChanged();
    }

    /**
     * Cache of the children views for a list item.
     */
    public class MoviePosterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView listItemMovieImageView;

        /**
         * Constructor for our ViewHolder. Within this constructor, we get a reference to our
         * TextViews and set an onClickListener to listen for clicks. Those will be handled in the
         * onClick method below.
         * @param itemView The View that you inflated in
         *                 {@linkMovieAdapter#onCreateViewHolder(ViewGroup, int)}
         */
        public MoviePosterViewHolder(View itemView) {
            super(itemView);
            Log.v(TAG, "MoviePosterViewHolder");
            listItemMovieImageView = itemView.findViewById(R.id.iv_movie_poster);
            itemView.setOnClickListener(this);
        }

        /**
         * Called whenever a user clicks on an item in the list.
         * @param v The View that was clicked
         */
        @Override
        public void onClick(View v) {
            Log.v(TAG, "onClick");
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }
}
