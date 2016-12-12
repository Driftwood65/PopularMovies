package com.example.android.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import static com.example.android.popularmovies.TmdbJsonUtils.MOVIE_OVERVIEW;
import static com.example.android.popularmovies.TmdbJsonUtils.MOVIE_POSTER;
import static com.example.android.popularmovies.TmdbJsonUtils.MOVIE_RELEASE_DATE;
import static com.example.android.popularmovies.TmdbJsonUtils.MOVIE_TITLE;
import static com.example.android.popularmovies.TmdbJsonUtils.MOVIE_USER_RATING;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(Intent.EXTRA_TEXT)) {
                ContentValues contentValues = intent.getParcelableExtra(Intent.EXTRA_TEXT);

                TextView titleTextView = (TextView) findViewById(R.id.tv_title);
                TextView overviewTextView = (TextView) findViewById(R.id.tv_overview);
                TextView releaseDateTextView = (TextView) findViewById(R.id.tv_release_date);
                TextView userRatingTextView = (TextView) findViewById(R.id.tv_user_rating);
                ImageView posterImageView = (ImageView) findViewById(R.id.iv_poster);

                titleTextView.setText(contentValues.getAsString(MOVIE_TITLE));
                overviewTextView.setText(contentValues.getAsString(MOVIE_OVERVIEW));
                releaseDateTextView.setText(contentValues.getAsString(MOVIE_RELEASE_DATE));
                userRatingTextView.setText(contentValues.getAsString(MOVIE_USER_RATING));
                Picasso.with(this)
                        .load(NetworkUtils.buildPosterUrl(contentValues.getAsString(MOVIE_POSTER)))
                        .into(posterImageView);
            }
        }
    }
}
