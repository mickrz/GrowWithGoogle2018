package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    private TextView mOriginView;
    private TextView mDescriptionView;
    private TextView mIngredientsView;
    private TextView mAlsoKnownAsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView ingredientsIv = findViewById(R.id.image_iv);
        mOriginView = (TextView) findViewById(R.id.origin_tv);
        mAlsoKnownAsView = (TextView) findViewById(R.id.also_known_tv);
        mIngredientsView = (TextView) findViewById(R.id.ingredients_tv);
        mDescriptionView = (TextView) findViewById(R.id.description_tv);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = JsonUtils.parseSandwichJson(json);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI(sandwich);
        Picasso.with(this)
                .load(sandwich.getImage())
                .into(ingredientsIv);

        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich sandwich) {

        /* get values for sandwich attributes and update textviews */
        String description = sandwich.getDescription();

        if (!description.isEmpty()) {
            mDescriptionView.setText(description);
        }

        StringBuilder ingredientStringBuilder = new StringBuilder();
        List<String> ingredients = sandwich.getIngredients();

        if (!ingredients.isEmpty()) {
            for (String ingredient : ingredients) {
                ingredientStringBuilder.append(ingredient);
                ingredientStringBuilder.append(", ");
            }
            mIngredientsView.setText(ingredientStringBuilder.toString());
        }

        StringBuilder akaStringBuilder = new StringBuilder();
        List<String> akaList = sandwich.getAlsoKnownAs();

        if (!akaList.isEmpty()) {
            for (String aka : akaList) {
                akaStringBuilder.append(aka);
                akaStringBuilder.append(", ");
            }
            mIngredientsView.setText(akaStringBuilder.toString());
        }

        String origin = sandwich.getPlaceOfOrigin();
        if (!origin.isEmpty()) {
            mOriginView.setText(origin);
        }
    }
}
