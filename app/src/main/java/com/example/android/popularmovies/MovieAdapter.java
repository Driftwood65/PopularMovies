package com.example.android.popularmovies;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dmuhm on 09.12.2016.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    private String[] movies;

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_list_item, parent, false);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {
        holder.mMovieNameTextView.setText(movies[position]);
    }

    @Override
    public int getItemCount() {
        return movies != null ? movies.length : 0;
    }

    class MovieAdapterViewHolder extends RecyclerView.ViewHolder {
        final TextView mMovieNameTextView;

        public MovieAdapterViewHolder(View itemView) {
            super(itemView);
            mMovieNameTextView = (TextView) itemView.findViewById(R.id.tv_movie_name);
        }
    }

    public void setMovies(String[] movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }
}
