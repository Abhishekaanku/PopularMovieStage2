package com.exmaple.android.popularmovie.MovieRoom;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;


import java.util.List;

public class FavrtMovieViewModel extends AndroidViewModel {
    private LiveData<List<Movie>> fvrtMovieList;

    public FavrtMovieViewModel(@NonNull Application application) {
        super(application);
        MovieFavrtDatabase database=MovieFavrtDatabase.getDatabaseInstance(application.getApplicationContext());
        fvrtMovieList=database.movieFavrtTaskDao().selectFavrtMovies();
    }

    public LiveData<List<Movie>> getFvrtMovieList() {
        return fvrtMovieList;
    }
}
