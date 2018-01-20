package com.example.android;

import android.annotation.SuppressLint;
import android.os.Bundle;
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
import com.example.android.model.Videos;
import com.example.android.movie.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DetailActivity extends AppCompatActivity implements Callback {

    ReviewAdapter adapter;
    RecyclerView recyclerView, videosRecycleView;
    ImageView posterImg, backdropImg;
    TextView movie_title, movie_rate, movie_release_date, movie_overview;
    ImageButton imageButton;
    String backdrop_url, poster_url, title, overview, vote_avg, release_date;
    FavoriteServices favoriteServices;

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
        initCollapsingToolbar();

        imageButton = findViewById(R.id.fav_btn);
        posterImg = findViewById(R.id.poster_image);
        backdropImg = findViewById(R.id.backdrop_image);
        movie_title = findViewById(R.id.movie_title);
        movie_rate = findViewById(R.id.movie_rate);
        movie_release_date = findViewById(R.id.movie_date);
        movie_overview = findViewById(R.id.movie_overview);
        recyclerView = findViewById(R.id.my_recycle_view);
        videosRecycleView = findViewById(R.id.videos_recycle);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        favoriteServices = new FavoriteServices(this);

        videosRecycleView.setHasFixedSize(true);
        videosRecycleView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

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

        getReviews(movie_id);
        //updateImage();
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

    private void getReviews(int id) {
        OkHttpClient client = new OkHttpClient();
        URL url = NetworkUtils.buildURL_Movie_Review(id);
        URL video_URL = NetworkUtils.build_Movie_Videos(id);

        Request request = new Request.Builder().url(url).build();
        Request videoRequest = new Request.Builder().url(video_URL).build();
        client.newCall(request).enqueue(this);
        client.newCall(videoRequest).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
                Log.i(TAG, "Failed to fetch Reviews");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String string = response.body().string();
                runOnUiThread(new Runnable() {
                    Videos videos;
                    List<Videos> videosList;

                    @Override
                    public void run() {
                        videosList = new ArrayList<>();

                        try {
                            JSONObject jsonObject = new JSONObject(string);
                            JSONArray jsonArray = jsonObject.getJSONArray("results");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                String key = object.getString("key");

                                videos = new Videos(key);
                                videosList.add(videos);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        videosAdapter = new VideosAdapter(DetailActivity.this, videosList);
                        videosRecycleView.setAdapter(videosAdapter);
                    }
                });

            }
        });
    }

    @Override
    public void onFailure(Call call, IOException e) {
        call.cancel();
        Log.i(TAG, "Failed to fetch Reviews");
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {

        final String respone = response.body().string();

        runOnUiThread(new Runnable() {
            Reviews reviews;
            List<Reviews> reviewsList;

            @Override
            public void run() {

                reviewsList = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(respone);
                    JSONArray jsonArray = jsonObject.getJSONArray("results");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String author = object.getString(Reviews.sAUTHOR);
                        String content = object.getString(Reviews.sCONTENT);
                        String id = object.getString(Reviews.sID);
                        reviews = new Reviews(author, content);
                        reviewsList.add(reviews);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter = new ReviewAdapter(DetailActivity.this, reviewsList);
                recyclerView.setAdapter(adapter);

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
