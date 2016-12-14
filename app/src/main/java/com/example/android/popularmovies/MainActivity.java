package com.example.android.popularmovies;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popularmovies.data.MovieContract;
import com.example.android.popularmovies.data.MovieDbHelper;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.android.popularmovies.NetworkUtils.PATH_POPULAR;
import static com.example.android.popularmovies.NetworkUtils.PATH_TOP_RATED;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ContentValues[]> {

    @BindView(R.id.recyclerview_movies) RecyclerView mRecyclerView;
    @BindView(R.id.tv_error_message_display) TextView mErrorMessageDisplay;
    @BindView(R.id.pb_loading_indicator) ProgressBar mLoadingIndicator;

    private MovieAdapter mMovieAdapter;
    private SQLiteDatabase mDb;

    private String path = PATH_POPULAR;
    private final String SORT = "sort";
    private int TMDB_API_LOADER = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mDb = new MovieDbHelper(this).getReadableDatabase();

        mMovieAdapter = new MovieAdapter();

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mMovieAdapter);


        if (savedInstanceState != null) {
            path = savedInstanceState.getString(SORT);
        }
        if (path != null) {
            getSupportLoaderManager().initLoader(TMDB_API_LOADER, null, this);
        } else {
            mMovieAdapter.swapCursor(getAllFavorites());
        }
    }

    @Override
    public Loader<ContentValues[]> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<ContentValues[]>(this) {

            ContentValues[] mMovies = null;

            @Override
            public ContentValues[] loadInBackground() {
                URL url = NetworkUtils.buildUrl(path);
                try {
                    String json = NetworkUtils.getResponseFromHttpUrl(url);
                    return TmdbJsonUtils.getMoviesFromJson(json);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onStartLoading() {
                if(mMovies != null) {
                    deliverResult(mMovies);
                } else {
                    mLoadingIndicator.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }

            @Override
            public void deliverResult(ContentValues[] data) {
                mMovies = data;
                super.deliverResult(data);
            }
        };
    }


    @Override
    public void onLoadFinished(Loader<ContentValues[]> loader, ContentValues[] data) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);

        if(data != null && data.length > 0) {
            String[] columns = data[0].keySet().toArray(new String[data[0].keySet().size()]);
            MatrixCursor cursor = new MatrixCursor(columns, data.length);
            for(ContentValues c : data) {
                List<Object> values = new ArrayList<>();
                for(Map.Entry<String, Object> e: c.valueSet()) {
                    values.add(e.getValue());
                }
                cursor.addRow(values);
            }
            mMovieAdapter.swapCursor(cursor);
            showMovieDataView();

        } else {
            showErrorMessage();
        }
    }

    @Override
    public void onLoaderReset(Loader<ContentValues[]> loader) {

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
            getSupportLoaderManager().restartLoader(TMDB_API_LOADER, null, this);
        }
        if(item.getItemId() == R.id.action_sort_by_top_rated && !PATH_TOP_RATED.equals(path)) {
            path = PATH_TOP_RATED;
            getSupportLoaderManager().restartLoader(TMDB_API_LOADER, null, this);
        }
        if(item.getItemId() == R.id.action_favorites && path != null) {
            path = null;
            getSupportLoaderManager().destroyLoader(TMDB_API_LOADER);
            mMovieAdapter.swapCursor(getAllFavorites());
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

    private Cursor getAllFavorites() {
        return mDb.query(
                MovieContract.MovieEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(SORT, path);
    }
}
