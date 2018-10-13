/*
 * Author: M. Rzucidlo
 * Created as part of the Google - Udacity scholarship (Android Developer Nanodegree program).
 * Project 4: Baking App.
 * Copyright (c) 2018. All rights reserved.
 * Last modified: 8/13/18 11:18 PM
 */

package com.mrz.bake.ui.recipesteps;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.mrz.bake.R;
import com.mrz.bake.data.database.model.Recipe;
import com.mrz.bake.data.database.model.Step;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.StatsSnapshot;

public class StepsFragment extends Fragment {
    private static final String TAG = StepsFragment.class.getSimpleName();
    private static final String RECIPE = "recipe";
    private static final String STEP = "step";
    private static final String TWOPANE = "twopane";
    private SimpleExoPlayer mSimpleExoPlayer;
    private PlayerView mPlayerView;
    private ImageView mImageView;
    private TextView step_tv;
    private Button mPrevious;
    private Button mNext;
    private long mPlaybackPosition;
    private int mCurrentWindow;
    private boolean mPlayWhenReady = true;
    Recipe mRecipe;
    private int mPosition;
    private boolean mTwoPane;
    private String videoUrl;
    private boolean videoAvailable = true;
    private int numSteps;
    Step steps;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.v(TAG, Thread.currentThread().getStackTrace()[5].getMethodName());
        View view = inflater.inflate(R.layout.steps_fragment, container, false);
        mPlayerView = view.findViewById(R.id.video_view);
        mImageView = view.findViewById(R.id.step_image);
        step_tv = view.findViewById(R.id.recipe_step_long_description);
        mPrevious = view.findViewById(R.id.previous_button);
        mNext = view.findViewById(R.id.next_button);

        if (savedInstanceState == null) {
            Intent intent = getActivity().getIntent();
            mRecipe = intent.getParcelableExtra(getString(R.string.recipe_blob));
            mPosition = intent.getIntExtra(getString(R.string.recipe_step), 0);
            mTwoPane = intent.getBooleanExtra(getString(R.string.two_pane), false);

        } else {
            mRecipe = savedInstanceState.getParcelable(RECIPE);
            mPosition = savedInstanceState.getInt(STEP);
            mTwoPane = savedInstanceState.getBoolean(TWOPANE);
        }

        numSteps = mRecipe.getSteps().size();

        mPlayerView.setDefaultArtwork(BitmapFactory.decodeResource(getResources(), R.drawable.ic_twotone_image_24px));
        populateUI(mRecipe, mPosition, mTwoPane);

        mPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                releasePlayer();
                if (mPosition > 0 ){
                    mPosition--;
                } else {
                    mPosition =  numSteps - 1;
                }
                populateUI(mRecipe, mPosition, mTwoPane);
                initializePlayer();
            }
        });

        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                releasePlayer();
                if (mPosition < numSteps - 1)
                {
                    mPosition++;
                } else {
                    mPosition = 0;
                }

                populateUI(mRecipe, mPosition, mTwoPane);
                initializePlayer();
            }
        });
        return view;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(RECIPE, mRecipe);
        outState.putInt(STEP, mPosition);
        outState.putBoolean(TWOPANE, mTwoPane);
        super.onSaveInstanceState(outState);
    }

    public void populateUI(Recipe recipe, int stepIndex, boolean isTwoPane) {
        Log.v(TAG, Thread.currentThread().getStackTrace()[5].getMethodName());

        if (recipe != null) {
            if (!isTwoPane) {
                getActivity().setTitle(recipe.getName());
            }

            steps = recipe.getSteps().get(stepIndex);
            step_tv.setText(steps.getDescription());

            videoUrl = steps.getVideoURL();
            if (videoUrl.isEmpty()) {
                videoAvailable = false;
                mImageView.setVisibility(View.VISIBLE);
                mPlayerView.setVisibility(View.GONE);
                String imgUrl = steps.getThumbnailURL();
                Log.v(TAG, "image url: " + imgUrl);
                if (imgUrl.isEmpty()) {
                    mImageView.setVisibility(View.GONE);
                } else {
                    loadThumbnail(imgUrl);
                }
            } else {
                videoAvailable = true;
                Log.v(TAG, "video url: " + videoUrl);
                mImageView.setVisibility(View.GONE);
                mPlayerView.setVisibility(View.VISIBLE);
            }

        } else {
            getActivity().setTitle(getString(R.string.no_recipe_found));
        }
    }

    /**
     *
     * @param url
     */
    private void loadThumbnail(String url) {
        Context context = getContext();
        StatsSnapshot statsSnapshot = Picasso.with(context).getSnapshot();
        String cacheHits = "cache hits: " + String.valueOf(statsSnapshot.cacheHits);
        String cacheMisses = ", cache misses: " + String.valueOf(statsSnapshot.cacheMisses);
        String downloadCount = ", download count: " + String.valueOf(statsSnapshot.downloadCount);
        String totalDownloadSize = ", total download size: " + String.valueOf(statsSnapshot.totalDownloadSize);
        String size = ", size: " + String.valueOf(statsSnapshot.size);
        String timeStamp = ", timeStamp: " + String.valueOf(statsSnapshot.timeStamp);
        Log.v(TAG, cacheHits + cacheMisses + downloadCount + totalDownloadSize + size + timeStamp);

        if (!url.isEmpty()) {
            Picasso.with(context).invalidate(url);
            Picasso.with(context).cancelRequest(mImageView);
            Picasso.with(context).load(url)
                    .error(R.drawable.ic_twotone_error_24px)
                    .placeholder(R.drawable.ic_twotone_image_24px)
                    .priority(Picasso.Priority.HIGH)
                    .memoryPolicy(MemoryPolicy.NO_STORE)
                    .into(mImageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            Log.v(TAG, "onSuccess");
                        }

                        @Override
                        public void onError() {
                            Log.v(TAG, "onError");
                            Toast.makeText(context, context.getString(R.string.an_error_occurred), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Log.v(TAG, context.getString(R.string.no_pictures_available));
        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.v(TAG, Thread.currentThread().getStackTrace()[5].getMethodName());
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.v(TAG, Thread.currentThread().getStackTrace()[5].getMethodName());
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(TAG, Thread.currentThread().getStackTrace()[5].getMethodName());
        hideSystemUi();
        if ((Util.SDK_INT <= 23 || mSimpleExoPlayer == null)) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.v(TAG, Thread.currentThread().getStackTrace()[5].getMethodName());
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.v(TAG, Thread.currentThread().getStackTrace()[5].getMethodName());
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    private void initializePlayer() {
        Log.v(TAG, Thread.currentThread().getStackTrace()[5].getMethodName());
        if(videoAvailable) {
            if (mSimpleExoPlayer == null) {
                mSimpleExoPlayer = ExoPlayerFactory.newSimpleInstance(
                        new DefaultRenderersFactory(getContext()), new DefaultTrackSelector(),
                        new DefaultLoadControl());
                mPlayerView.setPlayer(mSimpleExoPlayer);
                mSimpleExoPlayer.setPlayWhenReady(mPlayWhenReady);
                mSimpleExoPlayer.seekTo(mCurrentWindow, mPlaybackPosition);
            }

            MediaSource mediaSource = buildMediaSource(Uri.parse(videoUrl));
            mSimpleExoPlayer.prepare(mediaSource, true, false);
        }
    }

    private void releasePlayer() {
        Log.v(TAG, Thread.currentThread().getStackTrace()[5].getMethodName());
        if (videoAvailable) {
            if (mSimpleExoPlayer != null) {
                mPlaybackPosition = mSimpleExoPlayer.getCurrentPosition();
                mCurrentWindow = mSimpleExoPlayer.getCurrentWindowIndex();
                mPlayWhenReady = mSimpleExoPlayer.getPlayWhenReady();
                mSimpleExoPlayer.release();
                mSimpleExoPlayer = null;
            }
        }
    }

    private MediaSource buildMediaSource(Uri uri) {
        Log.v(TAG, Thread.currentThread().getStackTrace()[5].getMethodName());
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory(getString(R.string.app_name))).createMediaSource(uri);
    }

    private void hideSystemUi() {
        Log.v(TAG, Thread.currentThread().getStackTrace()[5].getMethodName());
        mPlayerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }
}
