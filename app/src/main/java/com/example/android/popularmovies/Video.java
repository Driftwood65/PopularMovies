package com.example.android.popularmovies;

/**
 * Created by aaa on 13.12.2016.
 */

public class Video {
    String key;
    String site;
    String type;
    String name;

    public String getKey() {
        return key;
    }

    public Video setKey(String key) {
        this.key = key;
        return this;
    }

    public String getSite() {
        return site;
    }

    public Video setSite(String site) {
        this.site = site;
        return this;
    }

    public String getType() {
        return type;
    }

    public Video setType(String type) {
        this.type = type;
        return this;
    }

    public String getName() {
        return name;
    }

    public Video setName(String name) {
        this.name = name;
        return this;
    }
}
