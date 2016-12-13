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

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
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
    public void onBindViewHolder(final MovieAdapterViewHolder holder, final int position) {
        Context context = holder.mMoviePosterImageView.getContext();
        String posterPath = movies[position].getAsString(MOVIE_POSTER);
        Picasso.with(context)
                .load(NetworkUtils.buildPosterUrl(posterPath))
                .into(holder.mMoviePosterImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.mTitle.setVisibility(GONE);
                        holder.mTitle.setText(movies[position].getAsString(MOVIE_TITLE));
                    }

                    @Override
                    public void onError() {
                        holder.mTitle.setVisibility(VISIBLE);
                        holder.mTitle.setText(movies[position].getAsString(MOVIE_TITLE));
                    }
                });
    }

    @Override
    public int getItemCount() {
        return movies != null ? movies.length : 0;
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
            intent.putExtra(Intent.EXTRA_TEXT, movies[getAdapterPosition()]);
            view.getContext().startActivity(intent);
        }
    }

    public void setMovies(ContentValues[] movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }
}
