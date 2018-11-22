package com.example.popularmoviesstage2.data;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import com.example.popularmoviesstage2.dao.MovieDao;
import com.example.popularmoviesstage2.model.Movie;

import java.util.List;

class MovieRepository {

    private MovieDao movieDao;
    private LiveData<List<Movie>> movieList;


    public MovieRepository(Application application) {
        MovieDatabase movieDatabase = MovieDatabase.getInstance(application);
        movieDao = movieDatabase.getMovieDao();
        movieList = movieDao.getAllMovies();
    }

    public LiveData<List<Movie>> getAll() {
        return movieList;
    }

    public void insertMovie(Movie movie) {
        new InsertMovieTask(movieDao).execute(movie);
    }

    public void deleteMovie(Movie movie) {
        new DeleteMovieTask(movieDao).execute(movie);
    }


    static class InsertMovieTask extends AsyncTask<Movie, Void, Void> {
        private MovieDao movieDao;

        private InsertMovieTask(MovieDao movieDao) {
            this.movieDao = movieDao;
        }

        @Override
        protected Void doInBackground(Movie... movies) {
            movieDao.insertMovie(movies[0]);
            return null;
        }

    }

    static class DeleteMovieTask extends AsyncTask<Movie, Void, Void> {
        private MovieDao movieDao;

        private DeleteMovieTask(MovieDao movieDao) {
            this.movieDao = movieDao;
        }

        @Override
        protected Void doInBackground(Movie... movies) {
            movieDao.deleteMovie(movies[0]);
            return null;
        }

    }
}
