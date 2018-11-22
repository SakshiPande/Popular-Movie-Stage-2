package com.example.popularmoviesstage2.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.popularmoviesstage2.dao.MovieDao;
import com.example.popularmoviesstage2.model.Movie;

@Database(entities = {Movie.class}, version = 1, exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase {

    private static MovieDatabase movieDB;

    public static MovieDatabase getInstance(Context context) {
        if (movieDB == null) {
            movieDB = Room.databaseBuilder(context.getApplicationContext(), MovieDatabase.class, "Movie_database")
                    .fallbackToDestructiveMigration()
                    .build();

        }
        return movieDB;
    }

    public abstract MovieDao getMovieDao();


}
