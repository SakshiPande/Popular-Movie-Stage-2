package com.example.popularmoviesstage2.networking;

import com.example.popularmoviesstage2.model.MovieBaseResponse;
import com.example.popularmoviesstage2.model.ReviewResponse;
import com.example.popularmoviesstage2.model.TrailersBaseResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PopularMoviesAPI {

    @GET("popular")
    Call<MovieBaseResponse> getPopularMovies(@Query("api_key") String apiKey);

    @GET("top_rated")
    Call<MovieBaseResponse> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("{id}/videos")
    Call<TrailersBaseResponse> getTrailers(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("{id}/reviews")
    Call<ReviewResponse> getReviews(@Path("id") int id, @Query("api_key") String apiKey);
}
