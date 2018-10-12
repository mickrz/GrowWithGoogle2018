/*
 * Author: M. Rzucidlo
 * Created as part of the Google - Udacity scholarship (Android Developer Nanodegree program).
 * Project 3: Popular Movies, Stage 2.
 * Copyright (c) 2018. All rights reserved.
 * Last modified: 7/21/18 6:46 PM
 *
 */

package com.mrz.popularmovies.database;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

// TODO: why is this not used?
public class AddFavoriteViewModel extends ViewModel {

    private LiveData<FavoriteMovie> favorite;

    public AddFavoriteViewModel(AppDatabase database, int movieId) {
        favorite = database.favoriteMovieDao().loadLiveFavoriteById(movieId);
    }

    public LiveData<FavoriteMovie> getFavorite() {
        return favorite;
    }
}
