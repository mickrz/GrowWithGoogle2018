package com.udacity.sandwichclub.utils;

import android.util.Log;

import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public final class JsonUtils {

    public static Sandwich parseSandwichJson(String json) {

        /* Sandwich attributes */
        final String NAME = "name";
        final String MAIN_NAME = "mainName";
        final String AKA = "alsoKnownAs";
        final String ORIGIN = "placeOfOrigin";
        final String DESCRIPTION = "description";
        final String IMG = "image";
        final String INGREDIENTS = "ingredients";

        try{
            JSONObject sandwichInfo = new JSONObject(json);
            JSONObject altNames = new JSONObject(sandwichInfo.getString(NAME));

            /* Convert JSON Array to list of strings for AKA & INGREDIENTS */
            JSONArray alsoKnownAsNames = altNames.getJSONArray(AKA);
            JSONArray ingredients = sandwichInfo.getJSONArray(INGREDIENTS);
            List<String> alsoKnownAsList = new ArrayList<String>();
            List<String> ingredientsList = new ArrayList<String>();

            for(int i=0;i<alsoKnownAsNames.length();i++){
                alsoKnownAsList.add(alsoKnownAsNames.getString(i));
            }

            for (int i = 0; i < ingredients.length(); i++) {
                ingredientsList.add(ingredients.getString(i));
            }

            /* Instead of using set methods, just populate c-tor for
               conciseness and readability.
            */
            return new Sandwich(
                    altNames.getString(MAIN_NAME),
                    alsoKnownAsList,
                    sandwichInfo.getString(ORIGIN),
                    sandwichInfo.getString(DESCRIPTION),
                    sandwichInfo.getString(IMG),
                    ingredientsList);
        }
        catch (final JSONException e) {
            e.printStackTrace();
        }
    return null;
    }
}
