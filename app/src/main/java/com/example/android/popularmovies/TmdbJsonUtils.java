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

import android.content.ContentValues;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

/**
 * Utility functions to handle OpenWeatherMap JSON data.
 */
public final class TmdbJsonUtils {


    public static final String MOVIE_TITLE = "original_title";
    public static final String MOVIE_ID = "id";
    public static final String MOVIE_POSTER = "poster_path";
    public static final String MOVIE_OVERVIEW = "overview";
    public static final String MOVIE_USER_RATING = "vote_average";
    public static final String MOVIE_RELEASE_DATE = "release_date";
    public static final String VIDEOS = "videos";
    public static final String RESULTS = "results";
    public static final String KEY = "key";
    public static final String NAME = "name";
    public static final String SITE = "site";
    public static final String TYPE = "type";
    public static final String REVIEWS = "reviews";
    public static final String CONTENT = "content";

    public static final String YOU_TUBE = "YouTube";
    public static final String TRAILER = "Trailer";

    /**
     * This method parses JSON from a web response and returns an array of Strings
     *
     * @param jsonStr JSON response from server
     *
     * @return Array of Strings describing movie data
     *
     * @throws JSONException If JSON data cannot be properly parsed
     */
    public static ContentValues[] getMoviesFromJson(String jsonStr)
            throws JSONException {

        JSONObject json = new JSONObject(jsonStr);

        JSONArray movieArray = json.getJSONArray(RESULTS);

        ContentValues[] parsedMovieData = new ContentValues[movieArray.length()];

        for (int i = 0; i < movieArray.length(); i++) {
            JSONObject movie = movieArray.getJSONObject(i);

            ContentValues contentValues = new ContentValues();
            contentValues.put(MOVIE_ID, movie.getLong(MOVIE_ID));
            contentValues.put(MOVIE_TITLE, movie.getString(MOVIE_TITLE));
            contentValues.put(MOVIE_POSTER, movie.getString(MOVIE_POSTER));
            contentValues.put(MOVIE_OVERVIEW, movie.getString(MOVIE_OVERVIEW));
            contentValues.put(MOVIE_USER_RATING, movie.getDouble(MOVIE_USER_RATING));
            contentValues.put(MOVIE_RELEASE_DATE, movie.getString(MOVIE_RELEASE_DATE));

            parsedMovieData[i] = contentValues;
        }

        return parsedMovieData;
    }

    public static Movie getMovieFromJson(String jsonStr)
            throws JSONException {

        JSONObject json = new JSONObject(jsonStr);

        Movie parsedMovieData = new Movie();

        JSONArray videos = json.getJSONObject(VIDEOS).getJSONArray(RESULTS);
        for (int i = 0; i < videos.length(); i++) {
            JSONObject video = videos.getJSONObject(i);

            Video parsedVideo = new Video().setKey(video.getString(KEY))
                    .setName(video.getString(NAME))
                    .setSite(video.getString(SITE))
                    .setType(video.getString(TYPE));

            parsedMovieData.addVideo(parsedVideo);
        }

        JSONArray reviews = json.getJSONObject(REVIEWS).getJSONArray(RESULTS);
        for (int i = 0; i < reviews.length(); i++) {
            JSONObject review = reviews.getJSONObject(i);

            Review parsedReview = new Review().setContent(review.getString(CONTENT));

            parsedMovieData.addReview(parsedReview);
        }

        return parsedMovieData;
    }


}