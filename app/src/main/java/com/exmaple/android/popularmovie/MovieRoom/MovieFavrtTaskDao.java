package com.exmaple.android.popularmovie.MovieRoom;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface MovieFavrtTaskDao {
    @Query("SELECT * FROM movie_data")
    LiveData<List<Movie>> selectFavrtMovies();

    @Insert
    void insertFavrtMovie(Movie movie);

    @Delete
    void deleteFavrtMovie(Movie movie);

    @Query("SELECT * FROM movie_data WHERE movieID=:movieID")
    LiveData<Movie> selectMovie(int movieID);
}
