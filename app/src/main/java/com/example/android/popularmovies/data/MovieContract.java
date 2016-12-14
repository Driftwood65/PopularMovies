package com.example.android.popularmovies.data;

import android.provider.BaseColumns;

/**
 * Created by aaa on 14.12.2016.
 */

public class MovieContract {

    public static final class MovieEntry implements BaseColumns {

        public static final String TABLE_NAME = "movie";

        public static final String COLUMN_TMDB_ID = "tmdb_id";

        public static final String COLUMN_ORIGINAL_TITLE = "original_title";

        public static final String COLUMN_POSTER_PATH = "poster_path";

        public static final String COLUMN_OVERVIEW = "overview";

        public static final String COLUMN_VOTE_AVERAGE = "vote_average";

        public static final String COLUMN_RELEASE_DATE = "release_date";

    }
}
