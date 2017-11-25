package com.example.android.movie;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.movie.Utilities.NetworkUtils;
import com.example.android.movie.data.Movie;
import com.squareup.picasso.Picasso;
import java.util.List;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ItemViewHolder>{

    private  Context context;
    private List<Movie> movieList;

    public RecycleViewAdapter(Context context, List<Movie> movieList) {
        this.context = context;
        this.movieList = movieList;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_layout, parent, false);

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {

        Movie movie = movieList.get(position);
        String title = movie.getTitle();
        String poster_path = movie.getPoster_path();
        String imagePath = NetworkUtils.buildURL_Image(poster_path);

        holder.title.setText(title);
        Picasso.with(context)
                .load(imagePath)
                .into(holder.poster_image);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView poster_image;
        TextView title;

        public ItemViewHolder(View itemView) {
            super(itemView);
            poster_image = itemView.findViewById(R.id.poster_image);
            title = itemView.findViewById(R.id.movie_title_TV);

        }
    }
}
