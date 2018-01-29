package com.example.android.model;


import android.content.ContentValues;


import com.example.android.provider.MovieContract;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Movie implements Serializable {

    public static final String sTitle = "title";
    public static final String sPoster_path = "poster_path";
    public static final String sBackdrop_path = "backdrop_path";
    public static final String sId = "id";
    public static final String sRelease_Date = "release_date";
    public static final String sOverview = "overview";
    public static final String sRate = "rate";
    public static final String sVote_Avg ="vote_average";

    @SerializedName("id")
    private int id;
    @SerializedName("vote_count")
    private int vote_count;
    @SerializedName("video")
    private boolean video;
    @SerializedName("vote_average")
    private float vote_average;
    @SerializedName("title")
    private String title;
    @SerializedName("popularity")
    private float popularity;
    @SerializedName("poster_path")
    private String poster_path;
    @SerializedName("backdrop_path")
    private String backdrop_path;
    @SerializedName("adult")
    private boolean adult;
    @SerializedName("overview")
    private String overview;
    @SerializedName("release_date")
    private String release_date;

    public Movie() {

    }

    public Movie(int id, float vote_average, String title, String poster_path, String backdrop_path, String overview, String release_date) {
        this.id = id;
        this.vote_average = vote_average;
        this.title = title;
        this.poster_path = poster_path;
        this.backdrop_path = backdrop_path;
        this.overview = overview;
        this.release_date = release_date;
    }

    public ContentValues toContentValues(){

        ContentValues values = new ContentValues();
        values.put(MovieContract.COLUMN_ID, id);
        values.put(MovieContract.COLUMN_OVERVIEW, overview);
        values.put(MovieContract.COLUMN_RELEASE_DATE, release_date);
        values.put(MovieContract.COLUMN_POSTER_PATH, poster_path);
        values.put(MovieContract.COLUMN_TITLE, title);
        values.put(MovieContract.COLUMN_AVERAGE_VOTE, vote_average);
        values.put(MovieContract.COLUMN_BACKDROP_PATH, backdrop_path);

        return values;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVote_count() {
        return vote_count;
    }

    public void setVote_count(int vote_count) {
        this.vote_count = vote_count;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public float getVote_average() {
        return vote_average;
    }

    public void setVote_average(float vote_average) {
        this.vote_average = vote_average;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getPopularity() {
        return popularity;
    }

    public void setPopularity(float popularity) {
        this.popularity = popularity;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }
}
