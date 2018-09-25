package com.exmaple.android.popularmovie;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class MovieReviewAdapter extends RecyclerView.Adapter<MovieReviewAdapter.ReviewViewHolder>{
    private List<MovieReview> movieReviewList;
    private Context context;
    private final String BASE_REVIEW_URL="https://www.themoviedb.org";
    MovieReviewAdapter(Context context,List<MovieReview> movieReviewList) {
        this.movieReviewList=movieReviewList;
        this.context=context;
    }
    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReviewViewHolder(LayoutInflater.from(context).inflate(
                R.layout.moviereviewlistitem,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        MovieReview movieReview=movieReviewList.get(position);
        holder.reviewAuthorView.setText(movieReview.getAuthor());
        holder.reviewContentView.setText(movieReview.getContent());
        final Uri movieReviewUri=Uri.parse(BASE_REVIEW_URL).buildUpon()
                .appendPath("review")
                .appendPath(movieReview.getId())
                .build();
        holder.reviewUrlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_VIEW);
                intent.setData(movieReviewUri);
                if(intent.resolveActivity(context.getPackageManager())!=null) {
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if(movieReviewList!=null) {
            return movieReviewList.size();
        }
        return 0;
    }

    public void setMovieReviewList(List<MovieReview> movieReviewList) {
        this.movieReviewList=movieReviewList;
        notifyDataSetChanged();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder{
        TextView reviewAuthorView;
        TextView reviewContentView;
        TextView reviewUrlButton;
        private ReviewViewHolder(View itemView) {
            super(itemView);
            reviewAuthorView=(TextView)itemView.findViewById(R.id.textViewReviewAuthor);
            reviewContentView=(TextView)itemView.findViewById(R.id.textViewReviewContent);
            reviewUrlButton=itemView.findViewById(R.id.textViewReviewURL);
        }
    }
}
