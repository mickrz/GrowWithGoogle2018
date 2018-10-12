package com.mrz.androidjokelib;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

// Step 2
public class JokeActivity extends AppCompatActivity {
    public static final String JOKE_DATA = "joke_data";
    TextView jokeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joke);
        jokeView = findViewById(R.id.show_joke);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey(JOKE_DATA)) {
            jokeView.setText(bundle.getString(JOKE_DATA));
        }
    }
}
