/*
 * Author: M. Rzucidlo
 * Created as part of the Google - Udacity scholarship (Android Developer Nanodegree program).
 * Project 3: Popular Movies, Stage 2.
 * Copyright (c) 2018. All rights reserved.
 * Last modified: 7/28/18 6:58 PM
 *
 */
package com.mrz.popularmovies.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.PersistableBundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.mrz.popularmovies.R;
import com.mrz.popularmovies.thirdparty.RetrofitInstance;
import com.mrz.popularmovies.adapter.ReviewAdapter;
import com.mrz.popularmovies.thirdparty.TMDBAPIService;
import com.mrz.popularmovies.adapter.TrailerAdapter;
import com.mrz.popularmovies.database.AppDatabase;
import com.mrz.popularmovies.database.AppExecutors;
import com.mrz.popularmovies.database.FavoriteMovie;
import com.mrz.popularmovies.databinding.ActivityMovieDetailsBinding;
import com.mrz.popularmovies.model.TMDBReview;
import com.mrz.popularmovies.model.TMDBReviewResults;
import com.mrz.popularmovies.model.TMDBInfo;
import com.mrz.popularmovies.model.TMDBTrailer;
import com.mrz.popularmovies.model.TMDBTrailerResults;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.StatsSnapshot;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class MovieDetailsActivity extends AppCompatActivity implements
        ReviewAdapter.ListItemClickListener, TrailerAdapter.ListItemClickListener {

    private static final String MOVIE_DETAILS = "moviedetails";
    private static final String TAG = MovieDetailsActivity.class.getSimpleName();

    private ReviewAdapter mReviewAdapter;
    private RecyclerView.LayoutManager mReviewLinearLayoutManager;
    private TrailerAdapter mTrailerAdapter;
    private RecyclerView.LayoutManager mTrailerLinearLayoutManager;
    private TMDBInfo mTMDBInfo;
    ActivityMovieDetailsBinding mBinding;
    private TMDBTrailerResults mTMDBTrailerResults;
    private TMDBReviewResults mTMDBReviewResults;
    private List<TMDBTrailer> mTMDBTrailerList;
    private List<TMDBReview> mTMDBReviewList;
    private boolean mFavorite = false;
    private AppDatabase mDb;
    private Context mContext;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        mDb = AppDatabase.getInstance(getApplicationContext());
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_details);

        Intent intent = getIntent();
        if (intent == null) {
            finish();
        }

        mContext = getApplicationContext();

        //Picasso.setSingletonInstance(getPicassoInstance(mContext));
        if (intent.hasExtra(Intent.EXTRA_TEXT)) {
            mTMDBInfo = intent.getParcelableExtra(Intent.EXTRA_TEXT);

            populateUI();
            loadMovieDetails(mTMDBInfo.getId());
            loadMovieTrailers(mTMDBInfo.getId());
            loadMovieReviews(mTMDBInfo.getId());
            setupRecyclerViews();

            // Check if this is in database to determine if it's a favorite (initial condition)
            checkIfFavoriteMovie();
            setupFavoriteonClickListener();
            setupFABonClickListener();
        }
    }

    /**
     *
     * @param outState
     * @param outPersistentState
     */
    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putParcelable(MOVIE_DETAILS, mTMDBInfo);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mTMDBInfo = savedInstanceState.getParcelable(MOVIE_DETAILS);
    }

    /**
     *
     */
    private void setupFavoriteonClickListener() {
        mBinding.favoriteIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final FavoriteMovie favoriteMovie = new FavoriteMovie(mTMDBInfo.getTitle(),
                        mTMDBInfo.getBackdropPath(), mTMDBInfo.getPosterPath(), mTMDBInfo.getOverview(),
                        mTMDBInfo.getReleaseDate(), mTMDBInfo.getVoteAverage(), mTMDBInfo.getVoteCount(),
                        mTMDBInfo.getId());

                // Write/Insert to database
                AppExecutors.getInstance()
                        .diskIO()
                        .execute(new Runnable() {
                             @Override
                             public void run() {
                                 if (!mFavorite) {
                                     mDb.favoriteMovieDao().insertFavorite(favoriteMovie);
                                     Log.v(TAG, "Added to Favorites");
                                 } else {
                                     mDb.favoriteMovieDao().deleteFavorite(mTMDBInfo.getId());
                                     Log.v(TAG, "Removed from Favorites");
                                 }
                                 runOnUiThread(new Runnable() {
                                     @Override
                                     public void run() {
                                         if (!mFavorite) {
                                             mBinding.favoriteIv.setImageResource(R.drawable.ic_round_favorite_24px);
                                             Snackbar.make(v, getString(R.string.added_to_favorites), Snackbar.LENGTH_LONG)
                                                     .setAction(getString(R.string.action), null).show();
                                             mFavorite = true;
                                         } else {
                                             mBinding.favoriteIv.setImageResource(R.drawable.ic_round_favorite_border_24px);
                                             Snackbar.make(v, getString(R.string.removed_from_favorites), Snackbar.LENGTH_LONG)
                                                     .setAction(getString(R.string.action), null).show();
                                             mFavorite = false;
                                         }
                                     }
                                 });
                             }
                             });
            }
        });
    }

    /**
     *
     */
    private void checkIfFavoriteMovie() {
        AppExecutors.getInstance()
            .diskIO()
            .execute(new Runnable() {
                @Override
                public void run() {
                    final FavoriteMovie favoriteMovier = mDb.favoriteMovieDao().loadFavoriteById(mTMDBInfo.getId());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (favoriteMovier != null) {
                                Log.v(TAG, "found favorite");
                                mBinding.favoriteIv.setImageResource(R.drawable.ic_round_favorite_24px);
                                mFavorite = true;
                            } else {
                                Log.v(TAG, "not a favorite yet");
                                mBinding.favoriteIv.setImageResource(R.drawable.ic_round_favorite_border_24px);
                            }
                        }
                    });
                }
            });
    }

    // TODO: Force FAB to not move, lauchmode/minimize network calls

    /**
     *
     */
    private void setupFABonClickListener() {
        mBinding.fab.setOnClickListener(new View.OnClickListener() {
            /**
             *
             * @param view
             */
            @Override
            public void onClick(View view) {
                if (mTMDBTrailerList.size() != 0) {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.youtube_base_url) +
                            mTMDBTrailerList.get(0).getKey());
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(Intent.createChooser(intent, getString(R.string.share_trailer_to)));
                    }
                } else {
                    Snackbar.make(view, getString(R.string.no_trailers_available_for_this_movie), Snackbar.LENGTH_LONG)
                            .setAction(getString(R.string.action), null).show();
                }
            }
        });
    }

    /**
     *
     */
    private void setupRecyclerViews() {
        mBinding.reviewsRv.setHasFixedSize(true);
        mReviewLinearLayoutManager = new LinearLayoutManager(this);
        mBinding.reviewsRv.setLayoutManager(mReviewLinearLayoutManager);
        mReviewAdapter = new ReviewAdapter(this);
        mBinding.reviewsRv.setAdapter(mReviewAdapter);

        mBinding.trailersRv.setHasFixedSize(true);
        mTrailerLinearLayoutManager = new LinearLayoutManager(this);
        mBinding.trailersRv.setLayoutManager(mTrailerLinearLayoutManager);
        mTrailerAdapter = new TrailerAdapter(this);
        mBinding.trailersRv.setAdapter(mTrailerAdapter);
    }

    /**
     *
     */
    private void populateUI() {
        setTitle(mTMDBInfo.getOriginalTitle());

        StatsSnapshot statsSnapshot = Picasso.with(mContext).getSnapshot();
        String cacheHits = "cache hits: " + String.valueOf(statsSnapshot.cacheHits);
        String cacheMisses = ", cache misses: " + String.valueOf(statsSnapshot.cacheMisses);
        String downloadCount = ", download count: " + String.valueOf(statsSnapshot.downloadCount);
        String totalDownloadSize = ", total download size: " + String.valueOf(statsSnapshot.totalDownloadSize);
        String size = ", size: " + String.valueOf(statsSnapshot.size);
        String timeStamp = ", timeStamp: " + String.valueOf(statsSnapshot.timeStamp);
        Log.v(TAG, cacheHits + cacheMisses + downloadCount + totalDownloadSize + size + timeStamp);

        String backdropUrl = getBaseContext().getString(R.string.backdrop_base_url) + mTMDBInfo.getBackdropPath();
        String imageUrl = getBaseContext().getString(R.string.image_base_url) + mTMDBInfo.getPosterPath();
        if (mTMDBInfo.getBackdropPath() != null && !mTMDBInfo.getBackdropPath().isEmpty()) {
            load(backdropUrl);

        } else if (mTMDBInfo.getPosterPath() != null && !mTMDBInfo.getPosterPath().isEmpty()) {
            load(imageUrl);
        }

        mBinding.overviewTv.setText(mTMDBInfo.getOverview());
        mBinding.releaseTv.setText(mTMDBInfo.getReleaseDate());

        String voteAvg = String.valueOf(mTMDBInfo.getVoteAverage());
        String voteCnt = String.valueOf(mTMDBInfo.getVoteCount());
        String userRating = voteAvg + " (" + voteCnt + ")";
        mBinding.userRatingTv.setText(userRating);
    }

    /**
     *
     * @param imageUrl
     */
    private void load(String imageUrl) {
        Picasso.with(mContext).invalidate(imageUrl);
        Picasso.with(mContext).cancelRequest(mBinding.imageIv);
        Picasso.with(this).load(imageUrl)
                .error(R.drawable.ic_twotone_error_24px)
                .placeholder(R.drawable.ic_twotone_image_24px)
                .noFade()
                .priority(Picasso.Priority.HIGH)
                .memoryPolicy(MemoryPolicy.NO_STORE)
                .into(mBinding.imageIv, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        Log.v(TAG, "onSuccess");
                    }

                    @Override
                    public void onError() {
                        Log.v(TAG, "onError");
                        Toast.makeText(mContext, "An error occurred", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /** create retro instance to get reviews and trailers
    // get total # of reviews and trailers to determine how big to make recyclerview
    // need adapter
     */
    private void loadMovieDetails(int id) {
        TMDBAPIService tmdbapiService = RetrofitInstance
                .getRetrofitInstance(getBaseContext())
                .create(TMDBAPIService.class);
        // TODO: I tried using BuildConfig.ApiKey but it broke gradle
        final Call<TMDBInfo> tmdbResultsCall =
                tmdbapiService.getMovieDetails(Integer.toString(id), getString(R.string.api_key));

        tmdbResultsCall.enqueue(new retrofit2.Callback<TMDBInfo>() {
            /**
             *
             * @param call
             * @param response
             */
            @Override
            public void onResponse(Call<TMDBInfo> call, Response<TMDBInfo> response) {
                Log.v(TAG, "onResponse");
                int responseCode = response.code();
                if (response.isSuccessful()) {
                    mTMDBInfo = response.body();
                    Log.v(TAG, mTMDBInfo.getOriginalTitle());
                    //mMovieAdapter.setResults(mTMDBInfo);
                    //mMovieAdapter.notifyDataSetChanged();
                    //mTMDBInfoList = mTMDBInfo.getResults();
                } else {
                    Log.v(TAG, "onResponse - failure: " + responseCode);
                }
            }

            /**
             *
             * @param call
             * @param t
             */
            @Override
            public void onFailure(Call<TMDBInfo> call, Throwable t) {
                Log.e(TAG, "onFailure");
                t.printStackTrace();
            }
        });
    }

    /**
     *
     * @param id
     */
    private void loadMovieTrailers(int id) {
        TMDBAPIService tmdbapiService = RetrofitInstance
                .getRetrofitInstance(getBaseContext())
                .create(TMDBAPIService.class);
        // TODO: I tried using BuildConfig.ApiKey but it broke gradle
        final Call<TMDBTrailerResults> tmdbResultsCall =
                tmdbapiService.getMovieTrailers(Integer.toString(id), getString(R.string.api_key));

        tmdbResultsCall.enqueue(new retrofit2.Callback<TMDBTrailerResults>() {
            /**
             *
             * @param call
             * @param response
             */
            @Override
            public void onResponse(Call<TMDBTrailerResults> call, Response<TMDBTrailerResults> response) {
                Log.v(TAG, "onResponse");
                int responseCode = response.code();
                if (response.isSuccessful()) {
                    mTMDBTrailerResults = response.body();
                    Log.v(TAG, Integer.toString(mTMDBTrailerResults.getId()));
                    mTrailerAdapter.setResults(mTMDBTrailerResults);
                    mTrailerAdapter.notifyDataSetChanged();
                    mTMDBTrailerList = mTMDBTrailerResults.getResults();
                    if (mTMDBTrailerList.isEmpty()) {
                        mBinding.trailersRv.setVisibility(View.GONE);
                        mBinding.trailersTv.setVisibility(View.GONE);
                    }
                } else {
                    Log.v(TAG, "onResponse - failure: " + responseCode);
                }
            }

            /**
             *
             * @param call
             * @param t
             */
            @Override
            public void onFailure(Call<TMDBTrailerResults> call, Throwable t) {
                Log.e(TAG, "onFailure");
                t.printStackTrace();
            }
        });
    }

    /**
     *
     * @param id
     */
    private void loadMovieReviews(int id) {
        TMDBAPIService tmdbapiService = RetrofitInstance
                .getRetrofitInstance(getBaseContext())
                .create(TMDBAPIService.class);
        // TODO: I tried using BuildConfig.ApiKey but it broke gradle
        final Call<TMDBReviewResults> tmdbResultsCall =
                tmdbapiService.getMovieReviews(Integer.toString(id), getString(R.string.api_key));

        tmdbResultsCall.enqueue(new retrofit2.Callback<TMDBReviewResults>() {
            /**
             *
             * @param call
             * @param response
             */
            @Override
            public void onResponse(Call<TMDBReviewResults> call, Response<TMDBReviewResults> response) {
                Log.v(TAG, "onResponse");
                int responseCode = response.code();
                if (response.isSuccessful()) {
                    mTMDBReviewResults = response.body();
                    Log.v(TAG, Integer.toString(mTMDBReviewResults.getId()));
                    mReviewAdapter.setResults(mTMDBReviewResults);
                    mReviewAdapter.notifyDataSetChanged();
                    mTMDBReviewList = mTMDBReviewResults.getResults();
                    if (mTMDBReviewList.isEmpty()) {
                        mBinding.reviewsRv.setVisibility(View.GONE);
                        mBinding.reviewsTv.setVisibility(View.GONE);
                    }
                } else {
                    Log.v(TAG, "onResponse - failure: " + responseCode);
                }
            }

            /**
             *
             * @param call
             * @param t
             */
            @Override
            public void onFailure(Call<TMDBReviewResults> call, Throwable t) {
                Log.e(TAG, "onFailure");
                t.printStackTrace();
            }
        });
    }

    /**
     *
     * @param clickedItemIndex
     */
    public void onTrailerListItemClick(int clickedItemIndex) {
        Log.i(TAG, "onTrailerListItemClick");
        String url = getString(R.string.youtube_base_url) + mTMDBTrailerList.get(clickedItemIndex).getKey();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(Intent.createChooser(intent, url));
        }
    }

    /**
     *
     * @param clickedItemIndex
     */
    public void onReviewListItemClick(int clickedItemIndex) {
        Log.i(TAG, "onReviewListItemClick");
        Intent intent = new Intent(MovieDetailsActivity.this, MovieReviewActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, mTMDBReviewList.get(clickedItemIndex));
        startActivity(intent);
    }
}
