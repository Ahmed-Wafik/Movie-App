package com.example.android.movie;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.AsyncTask;
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

import com.example.android.movie.Utilities.NetworkUtils;
import com.example.android.movie.data.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressBar progressBar;

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

        if (sharedPreferences.contains("movie")) {
            new FetchMoviesData().execute(sharedPreferences.getString("movie", null));
        } else {
            new FetchMoviesData().execute(sharedPreferences.getString("movie", "top_rated"));
        }
    }


    private class FetchMoviesData extends AsyncTask<String, Integer, List<Movie>> {
        Movie movie;
        List<Movie> movieList = new ArrayList<>();

        @Override
        protected void onPostExecute(List<Movie> movies) {
            RecycleViewAdapter adapter = new RecycleViewAdapter(MainActivity.this, movies);
            recyclerView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            recyclerView.setAdapter(adapter);
        }

        @Override
        protected void onPreExecute() {
            recyclerView.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected List<Movie> doInBackground(String... input) {

            try {
                URL url = NetworkUtils.buildURL_Movies(input[0]);
                Log.i(TAG, url.toString());
                String responseFromHttpUrl = NetworkUtils.getResponseFromHttpUrl(url);
                JSONObject jsonObject = new JSONObject(responseFromHttpUrl);

                JSONArray jsonArray = jsonObject.getJSONArray("results");
                Log.i(TAG, jsonArray.toString());
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject object = jsonArray.getJSONObject(i);
                    String poster_Path = object.getString(Movie.sPoster_path);
                    String title = object.getString(Movie.sTitle);

                    movie = new Movie();
                    movie.setPoster_path(poster_Path);
                    movie.setTitle(title);

                    movieList.add(movie);

                }

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
            return movieList;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //  editor = sharedPreferences.edit();
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.popular_menu_item:
                editor.putString("movie", "popular");
                editor.apply();
                new FetchMoviesData().execute(getResources().getString(R.string.pref_movie_value_popular));
                return true;
            case R.id.top_rated_menu_item:
                editor.putString("movie", "top_rated");
                editor.apply();
                new FetchMoviesData().execute(getResources().getString(R.string.pref_movie_value_top_rated));
                return true;
            default:

                return super.onOptionsItemSelected(item);
        }
    }
}
