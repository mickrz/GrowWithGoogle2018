/*
 * Author: M. Rzucidlo
 * Created as part of the Google - Udacity scholarship (Android Developer Nanodegree program).
 * Project 3: Popular Movies, Stage 2.
 * Copyright (c) 2018. All rights reserved.
 * Last modified: 7/21/18 5:29 PM
 *
 */

package com.mrz.popularmovies.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "favorite")
public class FavoriteMovie {

    @PrimaryKey(autoGenerate = true)
    private int key;
    private String title;
    private String backdropPath;
    private String posterPath;
    private String overview;
    private String releaseDate;
    private Double voteAverage;
    private Integer voteCount;
    @ColumnInfo(name = "id")
    private int movieId;

    @Ignore
    public FavoriteMovie(String title, String backdropPath, String posterPath, String overview,
                         String releaseDate, Double voteAverage, Integer voteCount, int movieId) {
        this.title = title;
        this.backdropPath = backdropPath;
        this.posterPath = posterPath;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
        this.movieId = movieId;
    }

    public FavoriteMovie(int key, String title, String backdropPath, String posterPath,
                         String overview, String releaseDate, Double voteAverage,
                         Integer voteCount, int movieId) {
        this.key = key;
        this.title = title;
        this.backdropPath = backdropPath;
        this.posterPath = posterPath;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
        this.movieId = movieId;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }
}
