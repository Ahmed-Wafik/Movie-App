package com.example.android.rest;

import com.example.android.model.MovieResponse;
import com.example.android.model.ReviewsResponse;
import com.example.android.model.VideosResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    //search for movies
    @GET("search/movie?api_key=1d6d44ed2156ccd55f118d5018edcfa2&language=en-US&page=1")
    Call<MovieResponse> getFilteredMovies(@Query("query") String movieName);

    //get top rated movies
    @GET("movie/top_rated?api_key=1d6d44ed2156ccd55f118d5018edcfa2&language=en-US&page=1")
    Call<MovieResponse> getTopRatedMovies();

    //get popular movies
    @GET("movie/popular?api_key=1d6d44ed2156ccd55f118d5018edcfa2&language=en-US&page=1")
    Call<MovieResponse> getPopularMovies();

    //get movie videos
    @GET("movie/{movie_ID}/videos?api_key=1d6d44ed2156ccd55f118d5018edcfa2&language=en-US")
    Call<VideosResponse> getVideosMovies(@Path("movie_ID") int id);

    //get movie reviews
    @GET("movie/{movie_ID}/reviews?api_key=1d6d44ed2156ccd55f118d5018edcfa2&language=en-US&page=1")
    Call<ReviewsResponse> getReviewsMovies(@Path("movie_ID") int id);

}
