package com.example.android.movie;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.movie.Utilities.NetworkUtils;
import com.example.android.movie.adapter.RecycleViewAdapter;
import com.example.android.movie.database.MovieContract;
import com.example.android.movie.model.Movie;

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

public class MainActivity extends AppCompatActivity implements Callback {

    RecyclerView recyclerView;
    RecycleViewAdapter adapter;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressBar progressBar;
    Movie movieFav;
    List<Movie> movieList;
    private String TAG = MainActivity.class.getSimpleName();

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("app", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        progressBar = findViewById(R.id.progress);
        recyclerView = findViewById(R.id.main_recycle_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        String path;
        if (sharedPreferences.contains("movie")) {
            path = sharedPreferences.getString("movie", null);
        } else {
            path = sharedPreferences.getString("movie", getResources().getString(R.string.pref_movie_value_top_rated));
        }
        try {
            run(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    void run(String path) throws IOException {
        OkHttpClient client = new OkHttpClient();
        URL url = NetworkUtils.buildURL_Movies(path);
        assert url != null;
        Request request = new Request.Builder()
                .url(url)
                .build();
        showProgress();
        client.newCall(request).enqueue(this);

    }


//    private class FetchMoviesData extends AsyncTask<String, Integer, List<Movie>> {
//        Movie movie;
//        List<Movie> movieList = new ArrayList<>();
//
//        @Override
//        protected void onPostExecute(List<Movie> movies) {
//            RecycleViewAdapter adapter = new RecycleViewAdapter(MainActivity.this, movies);
//            recyclerView.setVisibility(View.VISIBLE);
//            progressBar.setVisibility(View.GONE);
//            recyclerView.setAdapter(adapter);
//        }
//
//        @Override
//        protected void onPreExecute() {
//            recyclerView.setVisibility(View.INVISIBLE);
//            progressBar.setVisibility(View.VISIBLE);
//
//        }
//
//        @Override
//        protected List<Movie> doInBackground(String... input) {
//
//            try {
//                URL url = NetworkUtils.buildURL_Movies(input[0]);
//
//                String responseFromHttpUrl = NetworkUtils.getResponseFromHttpUrl(url);
//                JSONObject jsonObject = new JSONObject(responseFromHttpUrl);
//
//                JSONArray jsonArray = jsonObject.getJSONArray("results");
//
//                for (int i = 0; i < jsonArray.length(); i++) {
//
//                    JSONObject object = jsonArray.getJSONObject(i);
//                    String poster_Path = object.getString(Movie.sPoster_path);
//                    String title = object.getString(Movie.sTitle);
//
//                    movie = new Movie();
//                    movie.setPoster_path(poster_Path);
//                    movie.setTitle(title);
//
//                    movieList.add(movie);
//
//                }
//
//            } catch (IOException e) {
//                e.printStackTrace();
//                return null;
//            } catch (JSONException e) {
//                e.printStackTrace();
//                return null;
//            }
//            return movieList;
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "On start");

    }

    @SuppressLint("Recycle")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.popular_menu_item:
                editor.putString("movie", getResources().getString(R.string.pref_movie_value_popular));
                editor.apply();
                try {
                    run(getResources().getString(R.string.pref_movie_value_popular));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            case R.id.top_rated_menu_item:
                editor.putString("movie", getResources().getString(R.string.pref_movie_value_top_rated));
                editor.apply();
                try {
                    run(getResources().getString(R.string.pref_movie_value_top_rated));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            case R.id.favorites:

                Cursor cursor = getContentResolver().query(MovieContract.MovieEntries.CONTENT_URI, MovieContract.MovieEntries.getColumns(), null, null, null);
                if (cursor == null) {
                    Toast.makeText(this, "There is error in cursor", Toast.LENGTH_SHORT).show();
                } else if (cursor.getCount() < 1) {
                    Toast.makeText(this, "There is no Favorite movies ", Toast.LENGTH_SHORT).show();
                } else if (cursor.getCount() > 1) {

                    movieList = new ArrayList<>();

                    if (cursor.moveToFirst()) {
                        do {

                            int id = cursor.getInt(cursor.getColumnIndex(MovieContract.MovieEntries._ID));
                            String title = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntries.COLUMN_TITLE));
                            String poster_path = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntries.COLUMN_POSTER_PATH));
                            String overview = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntries.COLUMN_OVERVIEW));
                            String backdrop_path = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntries.COLUMN_BACKDROP_PATH));
                            String release_date = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntries.COLUMN_RELEASE_DATE));
                            float vote_avg = cursor.getFloat(cursor.getColumnIndex(MovieContract.MovieEntries.COLUMN_AVERAGE_VOTE));

                            movieFav = new Movie(id, vote_avg, title, poster_path, backdrop_path, overview, release_date);

                            movieList.add(movieFav);

                        } while (cursor.moveToNext());
                    }
                    Log.i(TAG, movieFav.getTitle());

                    adapter = new RecycleViewAdapter(this, movieList);
                    recyclerView.removeAllViews();
                    recyclerView.setAdapter(adapter);
                    return true;
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showProgress() {
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        recyclerView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onFailure(Call call, IOException e) {
        call.cancel();
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        final String string = response.body().string();
        this.runOnUiThread(new Runnable() {
            Movie movie;
            List<Movie> movieList = null;

            @Override
            public void run() {
                movieList = new ArrayList<>();

                try {
                    JSONObject jsonObject = new JSONObject(string);

                    JSONArray jsonArray = jsonObject.getJSONArray("results");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject object = jsonArray.getJSONObject(i);
                        String poster_Path = object.getString(Movie.sPoster_path);
                        String title = object.getString(Movie.sTitle);
                        String backdrop_path = object.getString(Movie.sBackdrop_path);
                        float vote_avg = (float) object.getDouble(Movie.sVote_Avg);
                        int id = object.getInt(Movie.sId);
                        String release_date = object.getString(Movie.sRelease_Date);
                        String overview = object.getString(Movie.sOverview);
                        movie = new Movie();

                        movie.setVote_average(vote_avg);
                        movie.setId(id);
                        movie.setOverview(overview);
                        movie.setRelease_date(release_date);
                        movie.setPoster_path(poster_Path);
                        movie.setTitle(title);
                        movie.setBackdrop_path(backdrop_path);

                        movieList.add(movie);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter = new RecycleViewAdapter(MainActivity.this, movieList);
                hideProgress();
                recyclerView.setAdapter(adapter);
            }
        });

    }
}
