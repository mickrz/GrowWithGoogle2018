/*
 * Author: M. Rzucidlo
 * Created as part of the Google - Udacity scholarship (Android Developer Nanodegree program).
 * Project 3: Popular Movies, Stage 2.
 * Copyright (c) 2018. All rights reserved.
 * Last modified: 7/28/18 12:52 AM
 *
 */

package com.mrz.popularmovies.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface FavoriteMovieDao {

    @Query("SELECT * FROM favorite ORDER BY id")
    LiveData<List<FavoriteMovie>> loadAllLiveFavorites();

    @Query("SELECT * FROM favorite ORDER BY id")
    List<FavoriteMovie> loadAllFavorites();

    @Insert
    void insertFavorite(FavoriteMovie favoriteMovie);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateFavorite(FavoriteMovie favoriteMovie);

    //@Delete

    @Query("DELETE FROM favorite WHERE id = :id")
    void deleteFavorite(int id);


    @Query("SELECT * FROM favorite WHERE id = :id")
    LiveData<FavoriteMovie> loadLiveFavoriteById(int id);

    @Query("SELECT * FROM favorite WHERE id = :id")
    FavoriteMovie loadFavoriteById(int id);
}
