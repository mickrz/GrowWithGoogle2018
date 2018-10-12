/*
 * Author: M. Rzucidlo
 * Created as part of the Google - Udacity scholarship (Android Developer Nanodegree program).
 * Project 3: Popular Movies, Stage 2.
 * Copyright (c) 2018. All rights reserved.
 * Last modified: 7/28/18 7:33 PM
 *
 */

package com.mrz.popularmovies.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mrz.popularmovies.R;

public class SettingsActivity extends AppCompatActivity {

    // TODO: Is there a way to just overlay this activity and not destroy MainActivity?

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }
}
