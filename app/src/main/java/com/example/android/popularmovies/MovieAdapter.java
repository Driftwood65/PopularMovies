package com.example.android.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.android.popularmovies.TmdbJsonUtils.MOVIE_POSTER;
import static com.example.android.popularmovies.TmdbJsonUtils.MOVIE_TITLE;

/**
 * Created by dmuhm on 09.12.2016.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    private ContentValues[] movies;

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_list_item, parent, false);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {
        Context context = holder.mMoviePosterImageView.getContext();
        String posterPath = movies[position].getAsString(MOVIE_POSTER);
        Picasso.with(context)
                .load(NetworkUtils.buildPosterUrl(posterPath))
                .into(holder.mMoviePosterImageView);
    }

    @Override
    public int getItemCount() {
        return movies != null ? movies.length : 0;
    }

    class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView mMoviePosterImageView;

        public MovieAdapterViewHolder(View itemView) {
            super(itemView);
            mMoviePosterImageView = (ImageView) itemView.findViewById(R.id.iv_movie_poster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), DetailActivity.class);
            intent.putExtra(Intent.EXTRA_TEXT, movies[getAdapterPosition()]);
            view.getContext().startActivity(intent);
        }
    }

    public void setMovies(ContentValues[] movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }
}
