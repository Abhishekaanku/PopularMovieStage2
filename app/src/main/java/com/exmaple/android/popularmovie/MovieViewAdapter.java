package com.exmaple.android.popularmovie;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.exmaple.android.popularmovie.MovieRoom.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.WINDOW_SERVICE;

public class MovieViewAdapter extends RecyclerView.Adapter<MovieViewAdapter.MovieViewHolder> {
    private Context mContext;
    int size=100;
    private ClickEventHandler myClickEventHandler;
    private List<Movie> movieList;

    interface ClickEventHandler {
        void onClickItemHandler(int moviePosition);
    }

    MovieViewAdapter(Context context,ClickEventHandler clickEventHandler,ArrayList<Movie> movieList) {
        mContext=context;
        myClickEventHandler=clickEventHandler;
        this.movieList=movieList;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(mContext);
        View movieItemView=layoutInflater.inflate(R.layout.movieitem,parent,false);
        MovieViewHolder newMovieViewHolder=new MovieViewHolder(movieItemView);
        return newMovieViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie=movieList.get(position);
        String movieImageURL=movie.getPoster();
        Picasso.with(mContext)
                .load(movieImageURL)
                .placeholder(R.drawable.posterplaceholder)
                .into(holder.numberView);
    }

    @Override
    public int getItemCount() {
        if(movieList==null) {
            return 0;
        }
        else {
            return movieList.size();
        }
    }

    public void setMovieList(List<Movie> movieList) {
        this.movieList=movieList;
        notifyDataSetChanged();
    }


    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView numberView;
        private MovieViewHolder(View itemView) {
            super(itemView);
            numberView = (ImageView) itemView.findViewById(R.id.movieItem);
            Pair<Integer,Integer> dimension=getScreenWidth(itemView);
            int posterWidth;
            if(((WindowManager) mContext.getSystemService(WINDOW_SERVICE))
                    .getDefaultDisplay().getOrientation()== ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                posterWidth=dimension.first/2;
            }
            else {
                posterWidth= dimension.first/3;

            }
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    posterWidth,(int)(1.5*posterWidth));
            numberView.setLayoutParams(params);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position=getAdapterPosition();
            myClickEventHandler.onClickItemHandler(position);
        }
        private Pair<Integer,Integer> getScreenWidth(View itemView) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((Activity)itemView.getContext()).getWindowManager()
                    .getDefaultDisplay().getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;
            int width = displayMetrics.widthPixels;
            return new Pair<>(width,height);
        }
    }
}