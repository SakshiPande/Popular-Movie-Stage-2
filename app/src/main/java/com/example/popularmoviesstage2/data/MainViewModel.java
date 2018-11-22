package com.example.popularmoviesstage2.data;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.popularmoviesstage2.model.Movie;

import java.util.List;

public class MainViewModel extends AndroidViewModel {
    private LiveData<List<Movie>> favoriteMovies;
    private MovieRepository movieRepository;

    public MainViewModel(@NonNull Application application) {
        super(application);
        movieRepository = new MovieRepository(application);
        favoriteMovies = movieRepository.getAll();
    }


    public LiveData<List<Movie>> getFavoriteMovies() {
        return favoriteMovies;
    }


    public void insert(Movie movie) {
        movieRepository.insertMovie(movie);
    }

    public void delete(Movie movie) {
        movieRepository.deleteMovie(movie);
    }

}
