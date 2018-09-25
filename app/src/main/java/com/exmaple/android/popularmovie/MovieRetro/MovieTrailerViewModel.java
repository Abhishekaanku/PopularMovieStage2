package com.exmaple.android.popularmovie.MovieRetro;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.exmaple.android.popularmovie.MovieTrailer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MovieTrailerViewModel extends ViewModel {
    private Map<Integer,MutableLiveData<ArrayList<MovieTrailer>>> trailerLiveMap=new HashMap<>();
    private ArrayList<Integer> movieIds;


    MovieTrailerViewModel(ArrayList<Integer> movieIds) {
        this.movieIds=movieIds;
        for(int movieId:movieIds) {
            MutableLiveData<ArrayList<MovieTrailer>> trailerLive=new MutableLiveData<>();
            trailerLiveMap.put(movieId,trailerLive);
            try {
                MovieRetroController.onGetTrailer(movieId,trailerLive);
            }
            catch(IOException e) {
                e.printStackTrace();
            }
        }
    }
    public LiveData<ArrayList<MovieTrailer>> getMovieTrailer(int movieId) {
        return trailerLiveMap.get(movieId);
    }
    public void refreshData() {
        for(int movieId:movieIds) {
            try {
                MovieRetroController.onGetTrailer(movieId,trailerLiveMap.get(movieId));
            }
            catch(IOException e) {
                e.printStackTrace();
            }
        }
    }
}
