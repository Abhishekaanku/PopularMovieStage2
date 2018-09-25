package com.exmaple.android.popularmovie.MovieRetro;


import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import com.exmaple.android.popularmovie.BuildConfig;
import com.exmaple.android.popularmovie.MovieReview;
import com.exmaple.android.popularmovie.MovieRoom.Movie;
import com.exmaple.android.popularmovie.MovieTrailer;
import com.exmaple.android.popularmovie.R;
import com.exmaple.android.popularmovie.Utilities.MovieJsonConverter;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MovieRetroController {
    private static String apiKey= BuildConfig.API_KEY;
    private static Retrofit retrofit=new Retrofit.Builder()
            .addConverterFactory(ScalarsConverterFactory.create())
            .baseUrl(MovieRetroAPI.BASE_URL)
            .build();
    private static MovieRetroAPI movieRetroAPI=retrofit.create(MovieRetroAPI.class);

    public static ArrayList<Movie> onStart(String sortByPreference,Context context) throws IOException {

        Call<String> call;
        if(sortByPreference.equals(context.getString(R.string.prefSortDefaultValue))) {
            call=movieRetroAPI.getMovieListPopularity(apiKey);
        }
        else {
            call=movieRetroAPI.getMovieListByRating(apiKey);
        }

        return MovieJsonConverter.getMovies(call.execute().body());
    }
    public static void onGetTrailer(int movieId, final MutableLiveData<ArrayList<MovieTrailer>> trailerLive) throws IOException {
        Call<String> call=movieRetroAPI.getMovieTrailersById(movieId,apiKey);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()) {
                    trailerLive.postValue(MovieJsonConverter.getMovieTrailerList(response.body()));
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                trailerLive.postValue(null);
                t.printStackTrace();
            }
        });

    }
    public static ArrayList<MovieReview> onGetReview(int movieId) throws IOException {
        Call<String> call=movieRetroAPI.getMovieReviewById(movieId,apiKey);
        return MovieJsonConverter.getMovieReviewList(call.execute().body());
    }
}
