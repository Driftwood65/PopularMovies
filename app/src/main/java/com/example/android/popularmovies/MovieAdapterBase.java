package com.example.android.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.example.android.popularmovies.data.MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE;
import static com.example.android.popularmovies.data.MovieContract.MovieEntry.COLUMN_POSTER_PATH;

/**
 * Created by dmuhm on 09.12.2016.
 */

public abstract class MovieAdapterBase extends RecyclerView.Adapter<MovieAdapterBase.MovieAdapterViewHolder> {

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_list_item, parent, false);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MovieAdapterViewHolder holder, final int position) {
        Context context = holder.mMoviePosterImageView.getContext();
        ContentValues movie = getMovie(position);
        final String posterPath = movie.getAsString(COLUMN_POSTER_PATH);
        final String originalTitle = movie.getAsString(COLUMN_ORIGINAL_TITLE);
        Picasso.with(context)
                .load(NetworkUtils.buildPosterUrl(posterPath))
                .into(holder.mMoviePosterImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.mTitle.setVisibility(GONE);
                        holder.mTitle.setText(originalTitle);
                    }

                    @Override
                    public void onError() {
                        holder.mTitle.setVisibility(VISIBLE);
                        holder.mTitle.setText(originalTitle);
                    }
                });
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
            Intent intent = new Intent(view.getContext(), DetailActivity.class);
            intent.putExtra(Intent.EXTRA_TEXT, getMovie(getAdapterPosition()));
            view.getContext().startActivity(intent);
        }
    }

    public abstract ContentValues getMovie(final int position);

}
