package com.example.android.popularmovies;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

/**
 * Created by dmuhm on 13.12.2016.
 */

public class MovieDetailLoader extends AsyncTaskLoader<Movie> {

    String mId;
    Movie mMovie;

    public MovieDetailLoader(Context context, String id) {
        super(context);
        mId = id;
    }

    @Override
    public Movie loadInBackground() {
        URL url = NetworkUtils.buildUrl(mId);
        try {
            String json = NetworkUtils.getResponseFromHttpUrl(url);
            mMovie = TmdbJsonUtils.getMovieFromJson(json);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mMovie;
    }

    @Override
    protected void onStartLoading() {
        if(mMovie != null) {
            deliverResult(mMovie);
        } else {
            forceLoad();
        }
    }
}
