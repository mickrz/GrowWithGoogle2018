/*
 * Author: M. Rzucidlo
 * Created as part of the Google - Udacity scholarship (Android Developer Nanodegree program).
 * Project 4: Baking App.
 * Copyright (c) 2018. All rights reserved.
 * Last modified: 8/14/18 10:54 AM
 */

package com.mrz.bake.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Request;


import static android.content.Context.ACTIVITY_SERVICE;

public class PicassoInstance {
    private static final String TAG = PicassoInstance.class.getSimpleName();
    private static Picasso picasso;
    /**
     *
     * @param context
     * @return
     */
    public static Picasso getPicassoInstance(Context context) {
        Log.v(TAG, Thread.currentThread().getStackTrace()[5].getMethodName());
        if (picasso == null) {
            Picasso.Builder builder = new Picasso.Builder(context);

            builder.memoryCache(new LruCache(getBytesForMemCache(15, context)));

            Picasso.RequestTransformer requestTransformer = new Picasso.RequestTransformer() {
                @Override
                public Request transformRequest(Request request) {
                    Log.v(TAG, request.toString());
                    return request;
                }
            };
            builder.requestTransformer(requestTransformer);

            builder.listener(new Picasso.Listener() {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                    Log.d("image load error", uri.getPath());
                }
            });
            picasso = builder.build();
        }
        return picasso;
    }

    /**
     *
     * @param percent
     * @param context
     * @return
     */
    private static int getBytesForMemCache(int percent, Context context){
        Log.v(TAG, Thread.currentThread().getStackTrace()[5].getMethodName());
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);

        return (int)(percent * mi.availMem / 100);
    }
}
