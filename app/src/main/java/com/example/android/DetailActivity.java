package com.example.android;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.Utilities.NetworkUtils;
import com.example.android.adapter.ReviewAdapter;
import com.example.android.adapter.VideosAdapter;
import com.example.android.model.FavoriteServices;
import com.example.android.model.Movie;
import com.example.android.model.Reviews;
import com.example.android.model.ReviewsResponse;
import com.example.android.model.Video;
import com.example.android.model.VideosResponse;
import com.example.android.movie.R;
import com.example.android.rest.ApiInterface;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DetailActivity extends AppCompatActivity  {

    ReviewAdapter reviewAdapter;
    RecyclerView  videosRecycleView , reviewsRecycleView;
    ImageView posterImg, backdropImg;
    TextView movie_title, movie_rate, movie_release_date, movie_overview;
    ImageButton imageButton;
    String backdrop_url, poster_url, title, overview, vote_avg, release_date;
    FavoriteServices favoriteServices;

    ApiInterface apiInterface;
    VideosAdapter videosAdapter;

    Movie movie;
    int movie_id;
    private final String TAG = DetailActivity.class.getSimpleName();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initCollapsingToolbar();

        imageButton = findViewById(R.id.fav_btn);
        posterImg = findViewById(R.id.poster_image);
        backdropImg = findViewById(R.id.backdrop_image);
        movie_title = findViewById(R.id.movie_title);
        movie_rate = findViewById(R.id.movie_rate);
        movie_release_date = findViewById(R.id.movie_date);
        movie_overview = findViewById(R.id.movie_overview);
        reviewsRecycleView = findViewById(R.id.reviews_recycle);
        videosRecycleView = findViewById(R.id.videos_recycle);

        favoriteServices = new FavoriteServices(this);

        videosRecycleView.setHasFixedSize(true);
        videosRecycleView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        reviewsRecycleView.setLayoutManager(new LinearLayoutManager(this));

        apiInterface = NetworkUtils.getMoviesRequest().create(ApiInterface.class);

        movie = (Movie) getIntent().getSerializableExtra("data");

        backdrop_url = NetworkUtils.buildURL_Image_backdrop(movie.getBackdrop_path());
        poster_url = NetworkUtils.buildURL_Image(movie.getPoster_path());

        movie_id = movie.getId();
        title = movie.getTitle();
        overview = movie.getOverview();
        vote_avg = String.valueOf(movie.getVote_average());
        release_date = movie.getRelease_date();

        Picasso.with(this).load(backdrop_url).error(R.mipmap.ic_launcher).into(backdropImg);
        Picasso.with(this).load(poster_url).error(R.mipmap.ic_launcher).into(posterImg);

        movie_title.setText(title);
        movie_overview.setText(overview);
        movie_rate.setText(vote_avg + "/10");
        movie_release_date.setText(release_date);

        getVideos(movie_id);
        getReviews(movie_id);

    }

    @Override
    protected void onStart() {
        super.onStart();
        updateImage();
    }

    public void updateImage() {
        if (!favoriteServices.isFavorite(movie)) {
            imageButton.setImageResource(R.drawable.ic_favorite_empty_50dp);
        } else {
            imageButton.setImageResource(R.drawable.ic_favorite_fill_50dp);

        }
    }

    private void initCollapsingToolbar() {

        final CollapsingToolbarLayout collapsingToolbar =
                findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout =  findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
            appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(title);
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    private void getVideos(int id){
        Call<VideosResponse> videosMovies = apiInterface.getVideosMovies(id);
        videosMovies.enqueue(new Callback<VideosResponse>() {
            @Override
            public void onResponse(@NonNull Call<VideosResponse> call, @NonNull Response<VideosResponse> response) {

                List<Video> results = response.body().getResults();
                videosAdapter = new VideosAdapter(DetailActivity.this,results);
                videosRecycleView.setAdapter(videosAdapter);
            }

            @Override
            public void onFailure(Call<VideosResponse> call, Throwable t) {
            call.cancel();
            Log.i(TAG,"Error loading Movies Videos");
            }
        });
    }
    private void getReviews(int id){

        Call<ReviewsResponse> reviewsMovies = apiInterface.getReviewsMovies(id);
        reviewsMovies.enqueue(new Callback<ReviewsResponse>() {
            @Override
            public void onResponse(Call<ReviewsResponse> call, Response<ReviewsResponse> response) {
                List<Reviews> results = response.body().getResults();

                reviewAdapter = new ReviewAdapter(DetailActivity.this,results);
                reviewsRecycleView.setAdapter(reviewAdapter);

            }

            @Override
            public void onFailure(Call<ReviewsResponse> call, Throwable t) {
                Log.i(TAG,"Error Loading reviews "+t.toString());
                call.cancel();
            }
        });
    }

    public void imageButton(View view) {
        if (favoriteServices.isFavorite(movie)) {
            favoriteServices.removeFromFavorite(movie);
            Toast.makeText(DetailActivity.this, "Removed form favorites", Toast.LENGTH_SHORT).show();
        } else {
            favoriteServices.addToFavorite(movie);
            Toast.makeText(DetailActivity.this, "Added to favorites", Toast.LENGTH_SHORT).show();
        }

        updateImage();
    }
}
