package com.exmaple.android.popularmovie;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class TrailerViewAdapter extends ArrayAdapter<MovieTrailer>{
    private Context context;
    final String BASE_TRAILER_URL="https://www.youtube.com";
    ClickListener clickListener;
    public TrailerViewAdapter(@NonNull Context context,ClickListener clickListener) {
        super(context, 0);
        this.context=context;
        this.clickListener=clickListener;
    }
    interface ClickListener {
        void addClickListener(ImageView imageView,String URL);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView==null) {
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.trailerlistitem,parent,false);

        }
        final MovieTrailer movieTrailer=getItem(position);
        String key=movieTrailer.getKey();
        final Uri trailer=Uri.parse(BASE_TRAILER_URL).buildUpon()
                .appendPath("watch")
                .appendQueryParameter("v",key)
                .build();
        ImageView movieTrailerIcon=convertView.findViewById(R.id.imageViewTrailer);
        movieTrailerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_VIEW);
                intent.setData(trailer);
                if(intent.resolveActivity(context.getPackageManager())!=null) {
                    context.startActivity(intent);
                }
            }
        });
        ImageView shareIcon=convertView.findViewById(R.id.imageshareIcon);
        clickListener.addClickListener(shareIcon,trailer.toString());
        TextView trailerLabelView=convertView.findViewById(R.id.textViewTrailerLabel);
        trailerLabelView.setText(movieTrailer.getVideoName());

        return convertView;
    }

    public void setTrailerList(List<MovieTrailer> movieTrailerList) {
        clear();
        if(movieTrailerList!=null) {
            addAll(movieTrailerList);
        }
        notifyDataSetChanged();
    }
}
