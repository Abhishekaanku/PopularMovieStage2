package com.exmaple.android.popularmovie.MovieRetro;



import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieRetroAPI {
    String BASE_URL="https://api.themoviedb.org";

    @GET("/3/movie/popular")
    Call<String> getMovieListPopularity(@Query("api_key")String apiKey);

    @GET("/3/movie/top_rated")
    Call<String> getMovieListByRating(@Query("api_key")String apiKey);

    @GET("/3/movie/{id}/reviews")
    Call<String> getMovieReviewById(@Path("id") int id,@Query("api_key")String apiKey);

    @GET("/3/movie/{id}/videos")
    Call<String> getMovieTrailersById(@Path("id")int id,@Query("api_key")String apiKey);
}
