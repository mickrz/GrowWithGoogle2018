/*
 * Author: M. Rzucidlo
 * Project: Popular Movies, Stage 1
 * Date: 2018/06/24
 * Company: Udacity Android Developer Nanodegree
 *
 */
package com.mrz.popularmovies.model;

import com.google.gson.annotations.SerializedName;
import com.mrz.popularmovies.model.TMDBInfo;

import java.util.List;

public class TMDBResults {
    @SerializedName("results")
    private List<TMDBInfo> mMovieInfoResults;

    @SerializedName("page")
    private int mPageNumber;

    @SerializedName("total_results")
    private int mTotalResults;

    @SerializedName("total_pages")
    private int mTotalPages;

    // was private, but need it public for copying from favoriteMovie pojo
    public TMDBResults(List<TMDBInfo> movieInfoList, int pageNumber, int totalResults, int totalPages) {
        mMovieInfoResults = movieInfoList;
        mPageNumber = pageNumber;
        mTotalResults = totalResults;
        mTotalPages = totalPages;
    }

    public List<TMDBInfo> getResults() {
        return mMovieInfoResults;
    }

    public int getPageNumber() {
        return mPageNumber;
    }

    public int getSize() { return mMovieInfoResults.size(); }

    public int getTotalSize() {
        return mTotalResults;
    }

    public int getTotalPages() {
        return mTotalPages;
    }
}
