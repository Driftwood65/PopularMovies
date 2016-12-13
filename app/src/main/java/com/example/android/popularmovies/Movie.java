package com.example.android.popularmovies;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by dmuhm on 13.12.2016.
 */

public class Movie {
    List<Video> videos = new ArrayList<>();
    List<Review> reviews = new ArrayList<>();

    public List<Video> getVideos() {
        return Collections.unmodifiableList(videos);
    }

    public Movie addVideo(Video video) {
        videos.add(video);
        return this;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public Movie addReview(Review review) {
        reviews.add(review);
        return this;
    }
}
