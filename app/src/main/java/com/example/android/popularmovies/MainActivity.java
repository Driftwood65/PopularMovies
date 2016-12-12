package com.example.android.popularmovies;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;

import static com.example.android.popularmovies.NetworkUtils.PATH_POPULAR;
import static com.example.android.popularmovies.NetworkUtils.PATH_TOP_RATED;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;

    private String path = PATH_POPULAR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        mMovieAdapter = new MovieAdapter();

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_movies);

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mMovieAdapter);

        new FetchMovieTask().execute();
    }

    class FetchMovieTask extends AsyncTask<Void, Void , ContentValues[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected ContentValues[] doInBackground(Void... voids) {
            URL url = NetworkUtils.buildUrl(path);
            try {
                String json = NetworkUtils.getResponseFromHttpUrl(url);
                return TmdbJsonUtils.getSimpleWeatherStringsFromJson(json);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ContentValues[] movies) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if(movies != null) {
                showMovieDataView();
                mMovieAdapter.setMovies(movies);
            } else {
                showErrorMessage();
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_sort_by_popular && !PATH_POPULAR.equals(path)) {
            path = PATH_POPULAR;
            new FetchMovieTask().execute();
        }
        if(item.getItemId() == R.id.action_sort_by_top_rated && !PATH_TOP_RATED.equals(path)) {
            path = PATH_TOP_RATED;
            new FetchMovieTask().execute();
        }

        return super.onOptionsItemSelected(item);
    }

    private void showMovieDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }
}
