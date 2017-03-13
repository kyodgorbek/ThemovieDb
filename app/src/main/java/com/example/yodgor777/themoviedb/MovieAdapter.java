package com.example.yodgor777.themoviedb;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by yodgor777 on 2017-02-02.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    Context context;
    ArrayList<MovieDataHolder> MovieItems = new ArrayList<>();

    public MovieAdapter(Context context, ArrayList<MovieDataHolder> MovieItems) {
        this.context = context;
        this.MovieItems = MovieItems;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.movie_row, parent, false);
        return new MovieViewHolder(v);
    }


    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        MovieDataHolder dataHolder = MovieItems.get(position);
        holder.MovieTitle.setText(dataHolder.Title);
        holder.MovieOverView.setText(dataHolder.OverView);
        holder.MovieIcon.setImageBitmap(null);

        Picasso.with(context)
                .load(dataHolder.ImageUrl)
                .fit().centerCrop()
                .error(R.drawable.ex)
                .into(holder.MovieIcon);
    }

    @Override
    public int getItemCount() {
        return MovieItems.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {
        public ImageView MovieIcon;
        public TextView MovieTitle, MovieOverView;

        public MovieViewHolder(View itemView) {
            super(itemView);
            MovieIcon = (ImageView) itemView.findViewById(R.id.MovieIcon);
            MovieTitle = (TextView) itemView.findViewById(R.id.MovieTitle);
            MovieOverView = (TextView) itemView.findViewById(R.id.MovieOverView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String id = MovieItems.get(getAdapterPosition()).id;
                    String title = MovieItems.get(getAdapterPosition()).Title;
                    String ImgUrl = MovieItems.get(getAdapterPosition()).ImageUrl;
                    Intent intent = new Intent(context,MovieDetailsActivity.class);
                    intent.putExtra("MovieId",id);
                    intent.putExtra("MovieTitle",title);
                    intent.putExtra("MovieImage",ImgUrl);
                    context.startActivity(intent);
                }
            });
        }
    }
}
