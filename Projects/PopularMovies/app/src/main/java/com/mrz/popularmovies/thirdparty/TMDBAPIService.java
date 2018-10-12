/*
 * Author: M. Rzucidlo
 * Created as part of the Google - Udacity scholarship (Android Developer Nanodegree program).
 * Project 3: Popular Movies, Stage 2.
 * Copyright (c) 2018. All rights reserved.
 * Last modified: 7/28/18 7:33 PM
 *
 */
package com.mrz.popularmovies.thirdparty;

import com.mrz.popularmovies.model.TMDBReviewResults;
import com.mrz.popularmovies.model.TMDBInfo;
import com.mrz.popularmovies.model.TMDBResults;
import com.mrz.popularmovies.model.TMDBTrailerResults;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TMDBAPIService {
    @GET("movie/{preference}")
    Call<TMDBResults> getListOfMoviesBasedOnPreference(@Path("preference") String pref, @Query("api_key") String key);

    @GET("movie/{id}")
    Call<TMDBInfo> getMovieDetails(@Path("id") String id, @Query("api_key") String key);

    @GET("movie/{id}/videos")
    Call<TMDBTrailerResults> getMovieTrailers(@Path("id") String id, @Query("api_key") String key);

    @GET("movie/{id}/reviews")
    Call<TMDBReviewResults> getMovieReviews(@Path("id") String id, @Query("api_key") String key);
}
