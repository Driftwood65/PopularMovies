/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.popularmovies;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * These utilities will be used to communicate with the tmdb servers.
 */
final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private final static String API_KEY_PARAM = "api_key";

    static final String MOVIES_BASE_URL = "http://api.themoviedb.org/3/movie";
    static final String PATH_TOP_RATED = "top_rated";
    static final String PATH_POPULAR = "popular";
    static final String PATH_VIDEOS = "videos";
    static final String APPEND_TO_RESONSE_PARAM = "append_to_response";
    static final String APPEND_VALUE = "videos,reviews";

    private static final String POSTER_BASE_URL =  "http://image.tmdb.org/t/p/w185";

    /**
     * Builds the URL used to talk to the tmdb server.
     *
     * @return The URL to use to query the tmdb server.
     */
    public static URL buildUrl(String path) {
        Uri.Builder builder = Uri.parse(MOVIES_BASE_URL).buildUpon()
                .appendPath(path)
                .appendQueryParameter(API_KEY_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY);

        // check if it is a detail call
        try {
            Long.parseLong(path);
            builder.appendQueryParameter(APPEND_TO_RESONSE_PARAM, APPEND_VALUE);
        } catch (NumberFormatException e) {
            //ignore
        }

        URL url = null;
        try {
            url = new URL(builder.build().toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    static String buildPosterUrl(String posterPath) {
       return POSTER_BASE_URL + posterPath;
    }


    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}