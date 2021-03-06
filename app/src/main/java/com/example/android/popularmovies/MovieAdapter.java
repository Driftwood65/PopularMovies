package com.example.android.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.data.MovieContract.MovieEntry;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by dmuhm on 09.12.2016.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    private Cursor mCursor;

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_list_item, parent, false);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MovieAdapterViewHolder holder, final int position) {
        if (!mCursor.moveToPosition(position))
            return;

        Context context = holder.mMoviePosterImageView.getContext();
        final String posterPath =
                mCursor.getString(mCursor.getColumnIndex(MovieEntry.COLUMN_POSTER_PATH));
        final String title =
                mCursor.getString(mCursor.getColumnIndex(MovieEntry.COLUMN_ORIGINAL_TITLE));
        Picasso.with(context)
                .load(NetworkUtils.buildPosterUrl(posterPath))
                .into(holder.mMoviePosterImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.mTitle.setVisibility(GONE);
                        holder.mTitle.setText(title);
                    }

                    @Override
                    public void onError() {
                        holder.mTitle.setVisibility(VISIBLE);
                        holder.mTitle.setText(title);
                    }
                });
    }

    @Override
    public int getItemCount() {
        return mCursor != null ? mCursor.getCount() : 0;
    }

    class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.iv_poster) ImageView mMoviePosterImageView;
        @BindView(R.id.tv_title) TextView mTitle;

        public MovieAdapterViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (!mCursor.moveToPosition(getAdapterPosition()))
                return;

            Intent intent = new Intent(view.getContext(), DetailActivity.class);
            ContentValues cv = new ContentValues();
            DatabaseUtils.cursorRowToContentValues(mCursor, cv);
            intent.putExtra(Intent.EXTRA_TEXT, cv);
            view.getContext().startActivity(intent);
        }
    }

    public void swapCursor(Cursor newCursor) {
        // Always close the previous mCursor first
        if (mCursor != null) mCursor.close();
        mCursor = newCursor;
        if (newCursor != null) {
            // Force the RecyclerView to refresh
            this.notifyDataSetChanged();
        }
    }
}
