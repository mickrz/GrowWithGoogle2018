/*
 * Author: M. Rzucidlo
 * Created as part of the Google - Udacity scholarship (Android Developer Nanodegree program).
 * Project 3: Popular Movies, Stage 2.
 * Copyright (c) 2018. All rights reserved.
 * Last modified: 7/18/18 11:04 PM
 *
 */

package com.mrz.popularmovies.database;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;

import java.util.PropertyResourceBundle;

// TODO: Why is this not used?
public class AddFavoriteViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final AppDatabase mDb;
    private final int mFavoriteId;

    public AddFavoriteViewModelFactory(AppDatabase appDatabase, int favoriteId) {
        mDb = appDatabase;
        mFavoriteId = favoriteId;
    }

    // Note: This can be reused with minor modifications
    /*@Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new AddFavoriteViewModelFactory(mDb, mFavoriteId);
    }*/
}
