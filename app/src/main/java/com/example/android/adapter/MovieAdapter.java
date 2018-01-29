package com.example.android.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.DetailActivity;
import com.example.android.Utilities.NetworkUtils;
import com.example.android.model.Movie;
import com.example.android.model.MovieResponse;
import com.example.android.movie.R;
import com.example.android.rest.ApiInterface;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ItemViewHolder> implements Filterable {

    private Context context;
    private List<Movie> movieList;
    private List<Movie> movieListFiltered;
    private String TAG = MovieAdapter.class.getSimpleName();

    public MovieAdapter(Context context, List<Movie> movieList) {
        this.context = context;
        this.movieList = movieList;
        this.movieListFiltered = movieList;

    }

    private Palette.Swatch checkVibrantSwatch(Palette p) {
        Palette.Swatch swatch = null;

        if ((p.getVibrantSwatch()) != null) {
            swatch = p.getVibrantSwatch();

        } else if ((p.getMutedSwatch()) != null) {
            swatch = p.getMutedSwatch();

        }
        return swatch;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_layout, parent, false);

        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {

        final Movie movie = movieListFiltered.get(position);
        String title = movie.getTitle();
        String poster_path = movie.getPoster_path();
        String imagePath = NetworkUtils.buildURL_Image(poster_path);

        holder.title.setText(title);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("data", movie);
                context.startActivity(intent);

            }
        });
        if (!imagePath.isEmpty()) {

            Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                    holder.poster_image.setImageBitmap(bitmap);
                    Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                        @Override
                        public void onGenerated(Palette palette) {
                            Palette.Swatch swatch = checkVibrantSwatch(palette);
                            if (swatch != null) {
                                holder.title.setBackgroundColor(swatch.getRgb());
                                holder.title.setTextColor(swatch.getBodyTextColor());
                            }
                        }
                    });
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };
            Picasso.with(context)
                    .load(imagePath)
                    .error(R.mipmap.ic_launcher)
                    .into(target);
            }
        }


    @Override
    public int getItemCount() {
        return movieListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            ApiInterface apiInterface = NetworkUtils.getMoviesRequest().create(ApiInterface.class);
            Call<MovieResponse> call;

            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString().trim();
                if (charString.isEmpty()) {
                    movieListFiltered = movieList;
                } else {
                    call = apiInterface.getFilteredMovies(charString);
                    call.enqueue(new Callback<MovieResponse>() {
                        @Override
                        public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                            movieListFiltered.clear();
                            movieListFiltered = response.body().getResults();
                        }

                        @Override
                        public void onFailure(Call<MovieResponse> call, Throwable t) {
                            Log.e(TAG, t.toString());
                        }
                    });
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = movieListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                movieListFiltered = (List<Movie>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        private ImageView poster_image;
        private TextView title;


        public ItemViewHolder(View itemView) {
            super(itemView);

            poster_image = itemView.findViewById(R.id.poster_image);
            title = itemView.findViewById(R.id.movie_title_TV);

        }
    }
}
