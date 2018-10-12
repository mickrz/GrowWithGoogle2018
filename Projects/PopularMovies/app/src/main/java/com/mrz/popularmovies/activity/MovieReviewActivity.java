/*
 * Author: M. Rzucidlo
 * Created as part of the Google - Udacity scholarship (Android Developer Nanodegree program).
 * Project 3: Popular Movies, Stage 2.
 * Copyright (c) 2018. All rights reserved.
 * Last modified: 7/28/18 6:03 PM
 *
 */

package com.mrz.popularmovies.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mrz.popularmovies.R;
import com.mrz.popularmovies.databinding.ActivityMovieReviewBinding;
import com.mrz.popularmovies.model.TMDBReview;

public class MovieReviewActivity extends AppCompatActivity {
    private static final String MOVIE_REVIEW = "moviereview";

    private TMDBReview mTMDBReview;
    ActivityMovieReviewBinding mBinding;

    // TODO: Is there a way to just overlay this activity and not destroy MovieDetailsActivity?

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_movie_review);

        Intent intent = getIntent();
        if (intent == null) {
            finish();
        }

        if (intent.hasExtra(Intent.EXTRA_TEXT)) {
            mTMDBReview = intent.getParcelableExtra(Intent.EXTRA_TEXT);
            setTitle(getString(R.string.review_by) + mTMDBReview.getAuthor());
            mBinding.reviewTv.setText(mTMDBReview.getContent());
        }
    }

    /**
     *
     * @param outState
     * @param outPersistentState
     */
    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        outState.putParcelable(MOVIE_REVIEW, mTMDBReview);
        super.onSaveInstanceState(outState, outPersistentState);
    }

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mTMDBReview = savedInstanceState.getParcelable(MOVIE_REVIEW);
    }
}
