package com.exmaple.android.popularmovie.MovieRetro;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import java.util.ArrayList;

public class MovieTrailerViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    ArrayList<Integer> movieIds;

    public MovieTrailerViewModelFactory(ArrayList<Integer> movieIds) {
        this.movieIds=movieIds;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MovieTrailerViewModel(movieIds);
    }
}
