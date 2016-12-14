package com.example.android.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.popularmovies.data.MovieContract;
import com.example.android.popularmovies.data.MovieDbHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static butterknife.ButterKnife.*;
import static com.example.android.popularmovies.data.MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE;
import static com.example.android.popularmovies.data.MovieContract.MovieEntry.COLUMN_OVERVIEW;
import static com.example.android.popularmovies.data.MovieContract.MovieEntry.COLUMN_POSTER_PATH;
import static com.example.android.popularmovies.data.MovieContract.MovieEntry.COLUMN_RELEASE_DATE;
import static com.example.android.popularmovies.data.MovieContract.MovieEntry.COLUMN_TMDB_ID;
import static com.example.android.popularmovies.data.MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Movie>{

    static int MOVIE_DETAIL_LOADER = 1;

    @BindView(R.id.lv_videos) ListView mVideosListView;

    private Movie mMovie;
    private final static String YOU_TUBE_BASE = "http://www.youtube.com/watch?v=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(Intent.EXTRA_TEXT)) {

                ContentValues contentValues = intent.getParcelableExtra(Intent.EXTRA_TEXT);

                ButterKnife.bind(this);
                TextView titleTextView = findById(this, R.id.tv_title);
                TextView overviewTextView = findById(this, R.id.tv_overview);
                TextView releaseDateTextView = findById(this, R.id.tv_release_date);
                TextView userRatingTextView = findById(this, R.id.tv_user_rating);
                ImageView posterImageView = findById(this, R.id.iv_poster);

                titleTextView.setText(contentValues.getAsString(COLUMN_ORIGINAL_TITLE));
                overviewTextView.setText(contentValues.getAsString(COLUMN_OVERVIEW));
                releaseDateTextView.setText(contentValues.getAsString(COLUMN_RELEASE_DATE));
                userRatingTextView.setText(contentValues.getAsString(COLUMN_VOTE_AVERAGE));
                Picasso.with(this)
                        .load(NetworkUtils.buildPosterUrl(contentValues.getAsString(COLUMN_POSTER_PATH)))
                        .into(posterImageView);

                mVideosListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Uri uri = Uri.parse(YOU_TUBE_BASE+mMovie.getVideos().get(i).getKey());
                        startActivity(new Intent(Intent.ACTION_VIEW, uri));
                    }
                });

                Bundle queryBundle = new Bundle();
                queryBundle.putString(COLUMN_TMDB_ID, contentValues.getAsString(COLUMN_TMDB_ID));
                getSupportLoaderManager().initLoader(MOVIE_DETAIL_LOADER, queryBundle, this);
            }
        }
    }


    @Override
    public Loader<Movie> onCreateLoader(int i, Bundle bundle) {
        return new MovieDetailLoader(this, bundle.getString(COLUMN_TMDB_ID));
    }

    @Override
    public void onLoadFinished(Loader<Movie> loader, Movie movie) {
        mMovie = movie;
        List<String> videos = new ArrayList<>();
        for(Video video : movie.getVideos()) {
            videos.add(video.getName());
        }
        List<String> reviews = new ArrayList<>();
        for(Review review : movie.getReviews()) {
            reviews.add(review.getContent());
        }
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, videos);
        mVideosListView.setAdapter(adapter);
    }

    @Override
    public void onLoaderReset(Loader<Movie> loader) {

    }

    @OnClick(R.id.bt_favorite)
    void storeMovie() {
        ContentValues contentValues = getIntent().getParcelableExtra(Intent.EXTRA_TEXT);
        new MovieDbHelper(this).getWritableDatabase()
                .insert(MovieContract.MovieEntry.TABLE_NAME, null, contentValues);
    }
}
