package com.example.popularmoviesstage2.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.example.popularmoviesstage2.model.MovieBaseResponse;
import com.example.popularmoviesstage2.model.MovieResponse;
import com.example.popularmoviesstage2.networking.PopularMoviesAPI;
import com.example.popularmoviesstage2.networking.RestClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TopRatedMoviesViewModel extends ViewModel {

    private MutableLiveData<List<MovieResponse>> movieList;


    public LiveData<List<MovieResponse>> getTopRatedMovies(String API_KEY) {
        if (movieList == null) {
            this.movieList = new MutableLiveData<>();

            loadTopRatedMovies(API_KEY);
        }

        return this.movieList;
    }

    private void loadTopRatedMovies(String API_KEY) {

        Retrofit retrofit = RestClient.retrofitService();
        PopularMoviesAPI popularMoviesAPI = retrofit.create(PopularMoviesAPI.class);
        Call<MovieBaseResponse> movieBaseResponseCall = popularMoviesAPI.getTopRatedMovies(API_KEY);
        movieBaseResponseCall.enqueue(new Callback<MovieBaseResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieBaseResponse> call, @NonNull Response<MovieBaseResponse> response) {
                if (response.isSuccessful()) {
                    MovieBaseResponse movieBaseResponseObj = response.body();
                    List<MovieResponse> movieListR = movieBaseResponseObj.getResults();
                    movieList.setValue(movieListR);
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieBaseResponse> call, @NonNull Throwable t) {
            }
        });

    }


}
