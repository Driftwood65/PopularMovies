package com.example.android.popularmovies;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;

    MovieAdapter mMovieAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMovieAdapter = new MovieAdapter();

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_movies);



        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mMovieAdapter);

        new FetchMovieTask().execute();
    }

    class FetchMovieTask extends AsyncTask<Void, Void , String[]> {
        @Override
        protected String[] doInBackground(Void... voids) {
            URL url = NetworkUtils.buildUrl();
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
        protected void onPostExecute(String[] strings) {
            mMovieAdapter.setMovies(strings);
        }
    }
}
