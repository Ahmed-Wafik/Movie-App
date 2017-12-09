package com.example.android.rest;

import com.example.android.model.MovieResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("search/movie")
    Call<MovieResponse> getFilteredMovies(@Query("api_key") String API,@Query("query") String movieName,@Query("language") String language);

}
