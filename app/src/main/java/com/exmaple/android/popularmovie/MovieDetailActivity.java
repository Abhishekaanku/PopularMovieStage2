package com.exmaple.android.popularmovie;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.exmaple.android.popularmovie.MovieRetro.MovieTrailerViewModel;
import com.exmaple.android.popularmovie.MovieRetro.MovieTrailerViewModelFactory;
import com.exmaple.android.popularmovie.MovieRoom.Movie;

import com.exmaple.android.popularmovie.MovieRoom.MovieDatabaseCallExecutor;
import com.exmaple.android.popularmovie.MovieRoom.MovieFavrtDatabase;

import com.exmaple.android.popularmovie.databinding.ActivityMovieDetailBinding;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;


public class MovieDetailActivity extends AppCompatActivity
            implements TrailerViewAdapter.ClickListener{

    private static final String TRAILER_KEY="trailer_key";
    private final int DEFAULT_VALUE=-1;
    public static final String MOVIE_OBJECT_KEY="movie_objects_key";
    public static final String MOVIE_POSITION_KEY="movie_position_key";

    private ActivityMovieDetailBinding activityMovieDetailBinding;

    ArrayList<Movie> movieList;
    int position; //Movie Position in movieList

    MovieFavrtDatabase movieFavrtDatabase=null;
    MovieDatabaseCallExecutor mCallExecutor;

    ListView movieTrailerListView;
    TrailerViewAdapter trailerViewAdapter=null;

    ProgressBar progressBar;
    Button movieReviewButton;
    TextView trailerErrorView;

    ArrayList<Integer> movieIds=null;
    MovieTrailerViewModel trailerViewModel;
    Toast mToast;
    String []months;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMovieDetailBinding= DataBindingUtil.setContentView(this,R.layout.activity_movie_detail);

        movieTrailerListView=activityMovieDetailBinding.trailerListView;
        progressBar=activityMovieDetailBinding.progressBarTrailer;
        trailerErrorView=activityMovieDetailBinding.textViewTrailerError;

        trailerViewAdapter=new TrailerViewAdapter(this,this);
        movieTrailerListView.setAdapter(trailerViewAdapter);

        movieFavrtDatabase=MovieFavrtDatabase.getDatabaseInstance(this);
        mCallExecutor=MovieDatabaseCallExecutor.getExecutorInstance();

        months=getResources().getStringArray(R.array.Months);

        Intent intent=getIntent();
        if(intent.hasExtra(MOVIE_OBJECT_KEY)) {
            movieList=intent.getParcelableArrayListExtra(MOVIE_OBJECT_KEY);
        }
        if(savedInstanceState!=null && savedInstanceState.containsKey(MOVIE_POSITION_KEY)) {
            position =savedInstanceState.getInt(MOVIE_POSITION_KEY);
        }
        else if(intent.hasExtra(MOVIE_POSITION_KEY)){
            position =intent.getIntExtra(MOVIE_POSITION_KEY,DEFAULT_VALUE);

        }
        if(position !=DEFAULT_VALUE) {
            populateView(position);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(MOVIE_POSITION_KEY, position);
        super.onSaveInstanceState(outState);
    }

    private void populateView(int position) {
        if(movieIds==null) {
            movieIds=new ArrayList<>();
            for(Movie movie:movieList) {
                movieIds.add(movie.getMovieID());
            }
        }
        final Movie movie=movieList.get(position);

        activityMovieDetailBinding.textViewMovieTitle.setText(movie.getOriginalTitle());
        activityMovieDetailBinding.layoutMovieData.textViewMovieName.setText(movie.getTitle());
        String movieRating=movie.getUser_rating()+"/10";
        activityMovieDetailBinding.layoutMovieData.textViewMovieRating.setText(movieRating);

        activityMovieDetailBinding.layoutMovieData.textViewReleaseData
                .setText(getFormattedDate(movie.getReleaseDate()));

        Picasso.with(this)
                .load(movie.getPoster())
                .into(activityMovieDetailBinding.layoutMovieData.imageViewMoviePoster);

        activityMovieDetailBinding.layoutMoviePlot.textViewPlot.setText(movie.getOverView());
        activityMovieDetailBinding.layoutMoviePlot.buttonReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MovieDetailActivity.this,MovieReviewActivity.class);
                intent.putExtra(MovieReviewActivity.REVIEW_INTENT,movie.getMovieID());
                startActivity(intent);
            }
        });

        movieTrailerListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                movieTrailerListView.getParent()
                        .requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        setIsFavrtMovie(movie);

        MovieTrailerViewModelFactory viewModelFactory=new MovieTrailerViewModelFactory(movieIds);
        trailerViewModel= ViewModelProviders.of(this,viewModelFactory)
                .get(MovieTrailerViewModel.class);
        LiveData<ArrayList<MovieTrailer>> trailerLiveData=trailerViewModel.getMovieTrailer(movie.getMovieID());
        progressBar.setVisibility(View.VISIBLE);
        trailerLiveData.observe(this, new Observer<ArrayList<MovieTrailer>>() {
            @Override
            public void onChanged(@Nullable ArrayList<MovieTrailer> movieTrailers) {
                showTrailerView(movieTrailers);
            }
        });
        addClickAction(activityMovieDetailBinding.layoutMovieData.imageViewFavourite,movie);
    }

    void setIsFavrtMovie(final Movie movie) {
        int movieID=movie.getMovieID();
        LiveData<Movie> movieLiveData=movieFavrtDatabase.movieFavrtTaskDao().selectMovie(movieID);
        movieLiveData.observe(this, new Observer<Movie>() {
            @Override
            public void onChanged(@Nullable Movie databaseMovie) {
                if(databaseMovie==null) {
                    movie.setFavrt(false);
                    setFavouriteIcon(false);
                }
                else {
                    movie.setFavrt(true);
                    setFavouriteIcon(true);
                }
            }
        });
    }

    void setFavouriteIcon(boolean isFavrt) {
        int imageResID;
        if(isFavrt) {
            imageResID=R.drawable.ic_favrtheartred;
        }
        else {
            imageResID=R.drawable.ic_favrtheart;
        }
        activityMovieDetailBinding.layoutMovieData.imageViewFavourite
                .setImageResource(imageResID);
    }


    void addClickAction(ImageView movieIconView,final Movie movie) {
        movieIconView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(movie.getIsFavrt()) {
                    mCallExecutor.getDatabaseIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            movieFavrtDatabase.movieFavrtTaskDao().deleteFavrtMovie(movie);
                            movie.setFavrt(false);
                        }
                    });
                }
                else {
                    mCallExecutor.getDatabaseIO().execute(new Runnable() {
                        @Override
                        public void run() {
                            movieFavrtDatabase.movieFavrtTaskDao().insertFavrtMovie(movie);
                            movie.setFavrt(true);
                        }
                    });
                }
                if(mToast!=null) {
                    mToast.cancel();
                }
                if(movie.getIsFavrt()) {
                    mToast=Toast.makeText(MovieDetailActivity.this,getString(R.string.removeFromFvrtMsg),Toast.LENGTH_LONG);
                }
                else {
                    mToast=Toast.makeText(MovieDetailActivity.this,getString(R.string.addToFavrtMsg),Toast.LENGTH_LONG);
                }
                mToast.show();
            }
        });
    }

    public void onClickNextMovie(View view) {
        position =(1+ position)%movieList.size();
        Intent intent=new Intent(this,MovieDetailActivity.class);
        intent.putExtra(MOVIE_POSITION_KEY, position);
        startActivity(intent);
    }


    private void showTrailerView(ArrayList<MovieTrailer> movieTrailers) {
        progressBar.setVisibility(View.INVISIBLE);
        trailerErrorView.setVisibility(View.INVISIBLE);
        trailerViewAdapter.setTrailerList(null);
        if(movieTrailers==null) {
            trailerErrorView.setVisibility(View.VISIBLE);
            trailerErrorView.setText(getString(R.string.trailerError));
        }
        else if(movieTrailers.size()==0) {
            trailerErrorView.setVisibility(View.VISIBLE);
            trailerErrorView.setText(getString(R.string.trailerNotAvailable));
        }
        else {
            trailerViewAdapter.setTrailerList(movieTrailers);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int position;
        if(intent.hasExtra(MOVIE_POSITION_KEY)) {
            position=intent.getIntExtra(MOVIE_POSITION_KEY,DEFAULT_VALUE);
            populateView(position);
        }
    }

    @Override
    public void addClickListener(ImageView imageView, final String trailer) {
        imageView.setImageResource(R.drawable.ic_share);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mimeType="text/plain";
                String shareTitle="Share Video URL!";
                ShareCompat.IntentBuilder
                        .from(MovieDetailActivity.this)
                        .setType(mimeType)
                        .setChooserTitle(shareTitle)
                        .setText(trailer)
                        .startChooser();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuitem,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemID=item.getItemId();
        if(itemID==R.id.viewSettingScreen) {
            Intent intent=new Intent(this,SettingActivity.class);
            startActivity(intent);
        }
        else if(itemID==R.id.viewRefresh) {
            if(trailerErrorView.getVisibility()==View.VISIBLE) {
                trailerViewModel.refreshData();
                trailerErrorView.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);
            }
        }
        else if(itemID==android.R.id.home) {
            onBackPressed();
        }
        return true;
    }
    private String getFormattedDate(String rawDate) {
        return rawDate.substring(8)+" "
                + months[Integer.parseInt(rawDate.substring(5,7))-1]+" "
                + rawDate.substring(0,4);
    }
}
