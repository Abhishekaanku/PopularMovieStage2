package com.exmaple.android.popularmovie;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.exmaple.android.popularmovie.MovieRetro.MovieRetroController;
import com.exmaple.android.popularmovie.MovieRoom.FavrtMovieViewModel;
import com.exmaple.android.popularmovie.MovieRoom.Movie;
import com.exmaple.android.popularmovie.MovieRoom.MovieFavrtDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieViewAdapter.ClickEventHandler,
        LoaderManager.LoaderCallbacks<ArrayList<Movie>>,SharedPreferences.OnSharedPreferenceChangeListener{
    private static final String BUNDLE_MOVIE_LIST="bundle_movie_list";
    private static final int LOADER_ID=20;
    RecyclerView recyclerView;
    GridLayoutManager gridLayoutManager;
    MovieViewAdapter movieViewAdapter;
    ProgressBar mProgressBar;
    TextView errorMsgView;
    SharedPreferences sharedPreferences;
    MovieFavrtDatabase movieFavrtDatabase;
    ArrayList<Movie> movieList;
    ArrayList<Movie> fvrtMovieList=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        movieFavrtDatabase=MovieFavrtDatabase.getDatabaseInstance(this);

        recyclerView=findViewById(R.id.movieItemRecyclerView);
        gridLayoutManager=new GridLayoutManager(this,1);

        int noOfColumn=calculateNoOfColumns();
        gridLayoutManager.setSpanCount(noOfColumn);

        recyclerView.setLayoutManager(gridLayoutManager);
        movieViewAdapter=new MovieViewAdapter(this,this,movieList);
        recyclerView.setAdapter(movieViewAdapter);
        recyclerView.setHasFixedSize(true);

        mProgressBar=findViewById(R.id.progressBar);
        errorMsgView=findViewById(R.id.TextViewNetworkErrorMessage);

        sharedPreferences=PreferenceManager.getDefaultSharedPreferences(this);

        Bundle loaderArgs=null;
        if(savedInstanceState!=null && savedInstanceState.containsKey(BUNDLE_MOVIE_LIST)) {
            movieList=savedInstanceState.getParcelableArrayList(BUNDLE_MOVIE_LIST);
        }

        getSupportLoaderManager().initLoader(LOADER_ID,loaderArgs,this);

        if(getSortBy().equals(getString(R.string.view_favourites))) {
            getSupportLoaderManager().restartLoader(LOADER_ID,loaderArgs,this);
        }

        showNotConnectedMessage(!isConnectedtoInternet());
    }

    private void showNotConnectedMessage(boolean isShow) {
        if(isShow) {
            errorMsgView.setVisibility(View.VISIBLE);
        }
        else {
            errorMsgView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if(movieList!=null) {
            outState.putParcelableArrayList(BUNDLE_MOVIE_LIST,movieList);
        }
        super.onSaveInstanceState(outState);
    }

    private boolean isConnectedtoInternet() {
        ConnectivityManager cm =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnected();
    }
    @Override
    public void onClickItemHandler(int moviePosition) {
        Intent intent=new Intent(this,MovieDetailActivity.class);
        if(getSortBy().equals(getString(R.string.view_favourites))) {
            intent.putParcelableArrayListExtra(MovieDetailActivity.MOVIE_OBJECT_KEY,fvrtMovieList);
        }
        else {
            intent.putParcelableArrayListExtra(MovieDetailActivity.MOVIE_OBJECT_KEY,movieList);
        }
        intent.putExtra(MovieDetailActivity.MOVIE_POSITION_KEY,moviePosition);
        startActivity(intent);
    }

    String getSortBy() {
        return sharedPreferences.getString(getString(R.string.preferenceKeySortBy),
                getString(R.string.prefSortDefaultValue));
    }

    @NonNull
    @Override
    public Loader<ArrayList<Movie>> onCreateLoader(int id, @Nullable Bundle args) {
        return new AsyncTaskLoader<ArrayList<Movie>>(this) {
            ArrayList<Movie> movieList=null;
            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                showNotConnectedMessage(false);
                if(getSortBy().equals(getString(R.string.view_favourites))) {
                    retriveFavrtMovieList();
                }
                else {
                    if(movieList!=null) {
                        deliverResult(movieList);
                    }
                    else {
                        mProgressBar.setVisibility(View.VISIBLE);
                        forceLoad();
                    }
                }
            }

            @Nullable
            @Override
            public ArrayList<Movie> loadInBackground() {
                try {
                    return MovieRetroController.onStart(getSortBy(),MainActivity.this);
                }
                catch(IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            public void deliverResult(@Nullable ArrayList<Movie> data) {
                movieList=data;
                super.deliverResult(data);
            }

        };
    }



    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<Movie>> loader, ArrayList<Movie> data) {
        mProgressBar.setVisibility(View.INVISIBLE);
        if(data!=null) {
            showNotConnectedMessage(false);
            movieList=data;
            movieViewAdapter.setMovieList(movieList);
        }
        else {
            showNotConnectedMessage(true);
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<Movie>> loader) {

    }

    public int calculateNoOfColumns() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return (int) (dpWidth / 180);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);

    }

    void inValidateData() {
        movieList=null;
        movieViewAdapter.setMovieList(movieList);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(getString(R.string.preferenceKeySortBy))) {
            inValidateData();
            getSupportLoaderManager().restartLoader(LOADER_ID,null,MainActivity.this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuitem,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemID=item.getItemId();
        if(itemID==R.id.viewSettingScreen) {
            Intent intent=new Intent(MainActivity.this,SettingActivity.class);
            startActivity(intent);
        }
        else if(itemID==R.id.viewRefresh) {
            inValidateData();
            getSupportLoaderManager().restartLoader(LOADER_ID,null,this);
        }
        return true;
    }

    void retriveFavrtMovieList() {
        FavrtMovieViewModel movieViewModel= ViewModelProviders.of(this)
                .get(FavrtMovieViewModel.class);
        movieViewModel.getFvrtMovieList().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                fvrtMovieList.clear();
                fvrtMovieList.addAll(movies);
                if(getSortBy().equals(getString(R.string.view_favourites))) {
                    movieViewAdapter.setMovieList(fvrtMovieList);
                }
            }
        });
    }
}
