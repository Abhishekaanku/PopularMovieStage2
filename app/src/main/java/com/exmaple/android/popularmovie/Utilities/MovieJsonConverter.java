package com.exmaple.android.popularmovie.Utilities;

import android.util.Log;

import com.exmaple.android.popularmovie.MovieReview;
import com.exmaple.android.popularmovie.MovieRoom.Movie;
import com.exmaple.android.popularmovie.MovieTrailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MovieJsonConverter {
    public static ArrayList<Movie> getMovies(String JSONResponse) {
        try {
            ArrayList<Movie> moviesList=new ArrayList<>();
            JSONObject moviesJSON=new JSONObject(JSONResponse);
            JSONArray movieJSONArray=moviesJSON.getJSONArray("results");
            int JSONArraySize=movieJSONArray.length();
            int index;
            for(index=0;index<JSONArraySize;index++) {
                moviesList.add(getMovie(movieJSONArray.getJSONObject(index)));
            }
            return moviesList;
        }
        catch(JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    private static Movie getMovie(JSONObject movieJSON) throws JSONException {

        Movie movie=new Movie();

        movie.setMovieID(movieJSON.getInt("id"));

        movie.setTitle(movieJSON.getString("title"));

        String overView=movieJSON.getString("overview");
        if(overView.length()==0) {
            movie.setOverView("No Overview");
        }
        else {
            movie.setOverView(overView);
        }

        movie.setPopularity((float)movieJSON.getDouble("popularity"));

        movie.setOriginalTitle(movieJSON.getString("original_title"));

        movie.setUser_rating((float)movieJSON.getDouble("vote_average"));

        movie.setReleaseDate(movieJSON.getString("release_date"));

        String imageURL=movieJSON.getString("poster_path");
        String baseImageURL="https://image.tmdb.org/t/p/w185/";
        imageURL=baseImageURL+imageURL;
        movie.setPoster(imageURL);

        return movie;
    }
    public static ArrayList<MovieReview> getMovieReviewList(String movieReviewJsonString) {
        try {
            JSONObject movieReviewJson=new JSONObject(movieReviewJsonString);
            JSONArray reviewJsonArray=movieReviewJson.getJSONArray("results");
            ArrayList<MovieReview> reviewList=new ArrayList<>();
            int index=0;
            for(index=0;index<reviewJsonArray.length();++index) {
                MovieReview movieReview=new MovieReview();
                JSONObject reviewJson=reviewJsonArray.getJSONObject(index);
                movieReview.setId(reviewJson.getString("id"));
                movieReview.setAuthor(reviewJson.getString("author"));
                movieReview.setContent(reviewJson.getString("content"));
                reviewList.add(movieReview);
            }
            Log.d("ERRPR404",Integer.toString(reviewList.size()));
            return reviewList;
        }
        catch(JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static ArrayList<MovieTrailer> getMovieTrailerList(String movieTrailerJsonString) {
        MovieTrailer trailerObject;
        try {
            JSONObject movieTrailerJson=new JSONObject(movieTrailerJsonString);
            JSONArray trailerJson=movieTrailerJson.getJSONArray("results");
            ArrayList<MovieTrailer> trailerList=new ArrayList<>();
            int index=0;
            for(index=0;index<trailerJson.length();++index) {
                trailerObject=new MovieTrailer();
                JSONObject jsonObject=trailerJson.getJSONObject(index);
                trailerObject.setKey(jsonObject.getString("key"));
                trailerObject.setVideoName(jsonObject.getString("name"));
                trailerList.add(trailerObject);
            }
            Log.d("ERRPR405",Integer.toString(trailerList.size()));
            return trailerList;
        }
        catch(JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


}
