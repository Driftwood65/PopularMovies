package com.example.android.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.popularmovies.data.MovieContract.MovieEntry;

/**
 * Created by aaa on 14.12.2016.
 */

public class MovieDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "movie.db";

    private static final int DATABASE_VERSION = 1;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " + MovieEntry.TABLE_NAME + " ("+

                MovieEntry._ID                      + " INTEGER PRIMARY KEY AUTOINCREMENT, "    +
                MovieEntry.COLUMN_TMDB_ID           + " INTEGER NOT NULL, "                     +
                MovieEntry.COLUMN_ORIGINAL_TITLE    + " INTEGER NOT NULL, "                     +
                MovieEntry.COLUMN_RELEASE_DATE      + " TEXT, "                                 +
                MovieEntry.COLUMN_POSTER_PATH       + " TEXT, "                                 +
                MovieEntry.COLUMN_OVERVIEW          + " TEXT, "                                 +
                MovieEntry.COLUMN_VOTE_AVERAGE      + " REAL, "                                  +

                " UNIQUE (" + MovieEntry.COLUMN_TMDB_ID + ") ON CONFLICT REPLACE);";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
