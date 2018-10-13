/*
 * Author: M. Rzucidlo
 * Created as part of the Google - Udacity scholarship (Android Developer Nanodegree program).
 * Project 4: Baking App.
 * Copyright (c) 2018. All rights reserved.
 * Last modified: 8/14/18 6:22 PM
 */

package com.mrz.bake;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;

import com.mrz.bake.ui.recipelist.CardsActivity;

/**
 * Implementation of App Widget functionality.
 */
public class BakeWidget extends AppWidgetProvider {
    private static final String TAG = BakeWidget.class.getSimpleName();

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        //CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.bake_widget);

        // Get id and name of most recent recipe and display ingredients
        /* These lines go into Activity */
        /* Add line in res and variable name and default value for each */
        //PreferenceManager.getDefaultSharedPreferences(context).edit().putInt(context.getString(), ).apply();
        //PreferenceManager.getDefaultSharedPreferences(context).edit().putString(context.getString(), ).apply();

        String recipeName = PreferenceManager.getDefaultSharedPreferences(
                context).getString(context.getString(
                        R.string.widget_recipe_name),"Recipe Name");
        String recipeIngredients = PreferenceManager.getDefaultSharedPreferences(
                context).getString(context.getString(
                        R.string.widget_recipe_ingredients), "Ingredients");

        String widgetContents = recipeName + "\n" + recipeIngredients;
        Log.v(TAG, widgetContents);
        views.setTextViewText(R.id.appwidget_text, widgetContents);


        // Create Intent to launch main activity
        Intent intent = new Intent(context, CardsActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        // Set pending intent for when user clicks textview
        views.setOnClickPendingIntent(R.id.widget_layout, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ComponentName widget = new ComponentName(context.getPackageName(), BakeWidget.class.getName());
        int[] appWidgetIds = AppWidgetManager.getInstance(context).getAppWidgetIds(widget);
        onUpdate(context, AppWidgetManager.getInstance(context), appWidgetIds);
        super.onReceive(context, intent);
    }
}

