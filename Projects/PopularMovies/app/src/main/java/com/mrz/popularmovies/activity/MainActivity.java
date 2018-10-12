/*
 * Author: M. Rzucidlo
 * Created as part of the Google - Udacity scholarship (Android Developer Nanodegree program).
 * Project 3: Popular Movies, Stage 2.
 * Copyright (c) 2018. All rights reserved.
 * Last modified: 7/28/18 12:52 AM
 *
 */
package com.mrz.popularmovies.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.mrz.popularmovies.adapter.MovieAdapter;
import com.mrz.popularmovies.R;
import com.mrz.popularmovies.database.MainViewModel;
import com.mrz.popularmovies.thirdparty.RetrofitInstance;
import com.mrz.popularmovies.thirdparty.TMDBAPIService;
import com.mrz.popularmovies.database.AppDatabase;
import com.mrz.popularmovies.database.AppExecutors;
import com.mrz.popularmovies.database.FavoriteMovie;
import com.mrz.popularmovies.databinding.ActivityMainBinding;
import com.mrz.popularmovies.model.TMDBInfo;
import com.mrz.popularmovies.model.TMDBResults;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements MovieAdapter.ListItemClickListener,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private static final int POSTER_SIZE_LARGE = 1;
    private static final int POSTER_SIZE_MEDIUM = 2;
    private static final int POSTER_SIZE_SMALL = 4;
    private static final String MOVIE_LIST = "movielist";

    private static final String TAG = MainActivity.class.getSimpleName();
    private MovieAdapter mMovieAdapter;
    private RecyclerView.LayoutManager mGridLayoutManager;
    private int mGridViewColumns;
    private String mSortOrder;
    private List<TMDBInfo> mTMDBInfoList;
    private int index = 0;
    private TMDBResults mTMDBResults;
    ActivityMainBinding mBinding;
    private AppDatabase mDb;
    List<FavoriteMovie> favoriteMovies;
    private MainViewModel mMainViewModel;
    boolean retrieveMovies = false;

    /**
     *
     * @param sharedPreferences
     */
    private void ChangePosterSize(SharedPreferences sharedPreferences) {
        Log.v(TAG, "ChangePosterSize");
        String poster_size = sharedPreferences.getString(
                getString(R.string.pref_poster_size_key),
                getString(R.string.pref_poster_size_medium_value));
        if (poster_size.equals(getString(R.string.pref_poster_size_small_value))) {
            mGridViewColumns = POSTER_SIZE_SMALL;
        }
        else if (poster_size.equals(getString(R.string.pref_poster_size_large_value))) {
            mGridViewColumns = POSTER_SIZE_LARGE;
        }
        else {
            mGridViewColumns = POSTER_SIZE_MEDIUM;
        }
    }

    /**
     *
     * @param sharedPreferences
     */
    private void ChangeSortOrder(SharedPreferences sharedPreferences) {
        Log.v(TAG, "ChangeSortOrder");
        mSortOrder = sharedPreferences.getString(
                getString(R.string.pref_sort_order_key),
                getString(R.string.pref_sort_order_most_popular_value));
        Log.d(TAG, "Changed sort order to " + mSortOrder);
        setTitle(mSortOrder);
        // TODO: no need to wait to load movie posters
        if (retrieveMovies) {
            loadMoviePosters();
        } else {
            retrieveMovies = true;
        }
    }

    /**
     *
     * @param sharedPreferences
     * @param key
     */
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            Log.v(TAG, "onSharedPreferenceChanged");
            if (key.equals(getString(R.string.pref_poster_size_key))) {
            ChangePosterSize(sharedPreferences);
            // update view
            ((GridLayoutManager) mBinding.rvMoviePosters
                    .getLayoutManager())
                    .setSpanCount(mGridViewColumns);
        }
        else if (key.equals(getString(R.string.pref_sort_order_key))) {
            ChangeSortOrder(sharedPreferences);
        }
    }

    /**
     *
     */
    @Override
    protected void onDestroy() {
        Log.v(TAG, "onDestroy");
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    /**
     *
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.v(TAG, "onSaveInstanceState");
        // save movie list for only non-favorite
        if (!mSortOrder.equals(getString(R.string.pref_sort_order_favorites_value))) {
            outState.putParcelableArrayList(MOVIE_LIST, new ArrayList<Parcelable>(mTMDBInfoList));
        }
        // TODO: did not work for orientation changes, not sure why
        outState.putParcelable(getString(R.string.movie_position),  mGridLayoutManager.onSaveInstanceState());
        super.onSaveInstanceState(outState);
    }

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.v(TAG, "onRestoreInstanceState");
        super.onRestoreInstanceState(savedInstanceState);
        // restore list for only non-favorite
        if (savedInstanceState != null) {
            // TODO: did not work for orientation changes, not sure why
            Parcelable state = savedInstanceState.getParcelable(getString(R.string.movie_position));
            mGridLayoutManager.onRestoreInstanceState(state);

            if (!mSortOrder.equals(getString(R.string.pref_sort_order_favorites_value))) {
                getSavedMovieListData(savedInstanceState);
                mMovieAdapter.setCachedResults(mTMDBInfoList);
            }
        }
    }

     /**
     *
     * @param savedInstanceState
     * @return
     */
    private boolean getSavedMovieListData(Bundle savedInstanceState) {
        Log.v(TAG, "getSavedMovieListData");
        if (savedInstanceState != null) {
            mTMDBInfoList = savedInstanceState.getParcelableArrayList(MOVIE_LIST);
            return true;
        }
        return false;
    }

    /**
     *
     */
    private void setupSharedPreferences() {
        Log.v(TAG, "setupSharedPreferences");
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        ChangePosterSize(sharedPreferences);
        ChangeSortOrder(sharedPreferences);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    /**
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.v(TAG, "onCreateOptionsMenu");
        // Inflate menu resource file.
        getMenuInflater().inflate(R.menu.settings_menu, menu);

        // Return true to display menu
        return true;
    }

    /**
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.v(TAG, "onOptionsItemSelected");
        int id = item.getItemId();
        if (id == R.id.activity_settings) {
            Intent startSettingsActivity = new Intent(this, SettingsActivity.class);
            startActivity(startSettingsActivity);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     *
     */
    private void loadMoviePosters() {
        Log.v(TAG, "loadMoviePosters");
        Log.v(TAG, mSortOrder);
        if (mSortOrder.equals(getString(R.string.pref_sort_order_favorites_value))) {
            loadFavoriteMoviePosters();
        } else {
            loadTMDBMoviePosters();
        }
    }

    /**
     *
     */
    private void loadTMDBMoviePosters() {
        Log.v(TAG, "loadTMDBMoviePosters");
        TMDBAPIService tmdbapiService = RetrofitInstance
                .getRetrofitInstance(getBaseContext())
                .create(TMDBAPIService.class);


        // TODO: I tried using BuildConfig.ApiKey but it broke gradle
        final Call<TMDBResults> tmdbResultsCall =
                tmdbapiService.getListOfMoviesBasedOnPreference(mSortOrder, getString(R.string.api_key));

        tmdbResultsCall.enqueue(new Callback<TMDBResults>() {
            /**
             *
             * @param call
             * @param response
             */
            @Override
            public void onResponse(Call<TMDBResults> call, Response<TMDBResults> response) {
                Log.v(TAG, "onResponse");
                int responseCode = response.code();
                if (response.isSuccessful()) {
                    mTMDBResults = response.body();
                    mMovieAdapter.setResults(mTMDBResults);
                    mMovieAdapter.notifyDataSetChanged();
                    mTMDBInfoList = mTMDBResults.getResults();
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
            public void onFailure(Call<TMDBResults> call, Throwable t) {
                Log.e(TAG, "onFailure");
                t.printStackTrace();
            }
        });
    }

    /**
     *
     */
    private void loadFavoriteMoviePosters() {
        Log.v(TAG, "loadFavoriteMoviePosters");
        // query database
        AppExecutors.getInstance()
                .diskIO()
                .execute(new Runnable() {
                    @Override
                    public void run() {
                        favoriteMovies = mDb.favoriteMovieDao().loadAllFavorites();
                        populateUI(favoriteMovies);
                    }
                });
    }

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        mDb = AppDatabase.getInstance(getApplicationContext());
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setupSharedPreferences();
        setupRecyclerViews(savedInstanceState);
        setupViewModel();

        if (savedInstanceState != null) {
            if (!getSavedMovieListData(savedInstanceState)) {
                loadMoviePosters();
            }
        } else {
            loadMoviePosters();
        }
    }

    /**
     *
     */
    private void setupRecyclerViews(Bundle savedInstanceState) {
        /** this setting improves performance since changes in the content do not change the layout
         *  size of the RecyclerView
         */
        mBinding.rvMoviePosters.setHasFixedSize(true);
        /** Requirement is to use a grid layout */
        mGridLayoutManager = new GridLayoutManager(this, mGridViewColumns);
        mBinding.rvMoviePosters.setLayoutManager(mGridLayoutManager);

        /** Specify an adapter*/
        mMovieAdapter = new MovieAdapter(this, this);
        mBinding.rvMoviePosters.setAdapter(mMovieAdapter);
        mBinding.rvMoviePosters.setSaveEnabled(true);
        if (savedInstanceState != null) {
            Parcelable state = savedInstanceState.getParcelable(getString(R.string.movie_position));
            mGridLayoutManager.onRestoreInstanceState(state);
        }
    }

    /**
     *
     */
    private void setupViewModel() {
        Log.v(TAG, "setupViewModel");
        mMainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        mMainViewModel.getFavorites().observe(this, new Observer<List<FavoriteMovie>>() {
            @Override
            public void onChanged(@Nullable final List<FavoriteMovie> favoriteMovies) {
                if (mSortOrder.equals(getString(R.string.pref_sort_order_favorites_value))) {
                    populateUI(favoriteMovies);
                }
            }
        });
    }

    /**
     *
     * @param favoriteMovies
     */
    private void populateUI(@Nullable List<FavoriteMovie> favoriteMovies) {
        Log.v(TAG, "populateUI");
        final List<TMDBInfo> movieInfoList = new ArrayList<TMDBInfo>();

        // TODO: Can a pojo be used for multiple purposes (network + db) or
        // do the declarations have to be separate? It would be more convenient to
        // have one pojo to maintain and less costly. This is why I have a loop that
        // I really do not like. It seems like very hacky with a lot of overhead.
        for (FavoriteMovie favoriteMovie : favoriteMovies) {
            Log.v(TAG, favoriteMovie.getTitle());
            TMDBInfo movieInfo = new TMDBInfo();

            movieInfo.setTitle(favoriteMovie.getTitle());
            movieInfo.setBackdropPath(favoriteMovie.getBackdropPath());
            movieInfo.setPosterPath(favoriteMovie.getPosterPath());
            movieInfo.setOverview(favoriteMovie.getOverview());
            movieInfo.setReleaseDate(favoriteMovie.getReleaseDate());
            movieInfo.setVoteAverage(favoriteMovie.getVoteAverage());
            movieInfo.setVoteCount(favoriteMovie.getVoteCount());
            movieInfo.setId(favoriteMovie.getMovieId());
            movieInfoList.add(movieInfo);
        }
        // This is necessary to update view on UI thread, otherwise will not compile
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (movieInfoList.isEmpty()) {
                    setTitle(getString(R.string.no_movies_in_list));
                } else {
                    setTitle(mSortOrder);
                }
                final int pageNumber = 1;
                final int totalPages = 1;
                mTMDBResults = new TMDBResults(movieInfoList, pageNumber, movieInfoList.size(), totalPages);
                mMovieAdapter.setResults(mTMDBResults);
                mMovieAdapter.notifyDataSetChanged();
                mTMDBInfoList = mTMDBResults.getResults();
             }
        });
    }

    /**
     * This is where we receive our callback from
     * {@link MovieAdapter.ListItemClickListener}
     *
     * This callback is invoked when you click on an item in the list.
     *
     * @param clickedItemIndex Index in the list of the item that was clicked.
     */
    @Override
    public void onListItemClick(int clickedItemIndex) {
        Log.v(TAG, "onListItemClick");
        /**
         * One of the posters was clicked, so create an intent to start the details activity
         */
        index = clickedItemIndex;
        Intent intent = new Intent(MainActivity.this, MovieDetailsActivity.class);
        intent.putExtra(Intent.EXTRA_TEXT, mTMDBInfoList.get(clickedItemIndex));
        startActivity(intent);
    }
}
