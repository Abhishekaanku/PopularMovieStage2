package com.exmaple.android.popularmovie;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.exmaple.android.popularmovie.MovieRetro.MovieRetroController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MovieReviewActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<ArrayList<MovieReview>> {
    private static final String REVIEW_KEY="review_key";
    RecyclerView recyclerView;
    ArrayList<MovieReview> movieReviewList;
    ProgressBar progressBar;
    TextView reviewErrorView;
    MovieReviewAdapter reviewAdapter;
    private static final int REVIEW_LOADER_ID=200;
    public static final String REVIEW_INTENT="reviewIntent";
    private static final int DEFAULT_MOVIE_ID=-1;
    int movieId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_review);
        progressBar=findViewById(R.id.progressBarReview);
        reviewErrorView=findViewById(R.id.textViewReviewError);
        recyclerView=findViewById(R.id.recyclerViewReview);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        reviewAdapter=new MovieReviewAdapter(this,movieReviewList);
        recyclerView.setAdapter(reviewAdapter);
        Intent intent=getIntent();
        if(savedInstanceState!=null && savedInstanceState.containsKey(REVIEW_KEY)) {
            movieReviewList=savedInstanceState.getParcelableArrayList(REVIEW_KEY);
        }
        if(intent.hasExtra(REVIEW_INTENT)) {
            movieId=intent.getIntExtra(REVIEW_INTENT,DEFAULT_MOVIE_ID);
            if(movieId!=DEFAULT_MOVIE_ID) {
                getSupportLoaderManager().initLoader(REVIEW_LOADER_ID,null,this);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(REVIEW_KEY,movieReviewList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public Loader<ArrayList<MovieReview>> onCreateLoader(int i, Bundle bundle) {
        return new AsyncTaskLoader<ArrayList<MovieReview>>(this) {
            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                reviewErrorView.setVisibility(View.INVISIBLE);
                if(movieReviewList!=null) {
                    deliverResult(movieReviewList);
                }
                else {
                    progressBar.setVisibility(View.VISIBLE);
                    forceLoad();
                }
            }

            @Override
            public ArrayList<MovieReview> loadInBackground() {
                try {
                    return MovieRetroController.onGetReview(movieId);
                }
                catch(IOException e) {
                    e.printStackTrace();
                }
                return null;
            }


            @Override
            public void deliverResult(ArrayList<MovieReview> data) {
                movieReviewList=data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<MovieReview>> loader, ArrayList<MovieReview> movieReviewList) {
        progressBar.setVisibility(View.INVISIBLE);
        if(movieReviewList!=null) {
            if(movieReviewList.size()==0) {
                reviewErrorView.setVisibility(View.VISIBLE);
                reviewErrorView.setText(getString(R.string.reviewNotAvailable));
            }
            else {
                reviewErrorView.setVisibility(View.INVISIBLE);
                reviewAdapter.setMovieReviewList(movieReviewList);
            }
        }
        else {
            reviewErrorView.setVisibility(View.VISIBLE);
            reviewErrorView.setText(getString(R.string.reviewError));
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<MovieReview>> loader) {

    }
}