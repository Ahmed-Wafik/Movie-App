package com.example.android.movie;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
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


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Movie>> {

    RecyclerView recyclerView;
    RecycleViewAdapter adapter;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressBar progressBar;
    private static final int LOADER_ID = 10;
    Bundle bundle = new Bundle();
    private String TAG = MainActivity.class.getSimpleName();
    Loader<List<Movie>> loader;

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

        LoaderManager supportLoaderManager = getSupportLoaderManager();

        loader = supportLoaderManager.getLoader(LOADER_ID);
        bundle.putString("bundle", sharedPreferences.getString("movie", getResources().getString(R.string.pref_movie_value_popular)));

        if (loader == null) {
            Log.i(TAG, "initializing Loader...");
            supportLoaderManager.initLoader(LOADER_ID, bundle, this);
        } else {
            Log.i(TAG, "Restart Loader...");

            supportLoaderManager.restartLoader(LOADER_ID, bundle, this);
        }

    }



    @SuppressLint("StaticFieldLeak")
    @Override
    public Loader<List<Movie>> onCreateLoader(int i, final Bundle data) {

        return new AsyncTaskLoader<List<Movie>>(this) {
            private Movie movie;
            private List<Movie> movieList ;

            @Override
            protected void onStartLoading() {
                showProgress();
                //caching the results
                if (movieList != null) {
                    deliverResult(movieList);

                } else {
                    forceLoad();
                }
            }

            @Override
            public List<Movie> loadInBackground() {
                movieList = new ArrayList<>();
                try {
                    String path = data.getString("bundle");
                    Log.i(TAG, path);
                    URL url = NetworkUtils.buildURL_Movies(path);
                    //Log.i(TAG, url.toString());
                    String responseFromHttpUrl = NetworkUtils.getResponseFromHttpUrl(url);
                    JSONObject jsonObject = new JSONObject(responseFromHttpUrl);

                    JSONArray jsonArray = jsonObject.getJSONArray("results");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject object = jsonArray.getJSONObject(i);
                        String poster_Path = object.getString(Movie.sPoster_path);
                        String title = object.getString(Movie.sTitle);

                        movie = new Movie();
                        movie.setPoster_path(poster_Path);
                        movie.setTitle(title);

                        movieList.add(movie);

                    }
                    return movieList;

                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void deliverResult(List<Movie> data) {
                movieList = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> movies) {
        hideProgress();

        adapter = new RecycleViewAdapter(MainActivity.this, movies);

        recyclerView.setAdapter(adapter);


    }


    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {

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
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.popular_menu_item:
                editor.putString("movie", getResources().getString(R.string.pref_movie_value_popular));
                editor.apply();
                bundle.putString("bundle",getResources().getString(R.string.pref_movie_value_popular));
                getSupportLoaderManager().restartLoader(LOADER_ID, bundle, this);
                Log.i(TAG,""+getSupportLoaderManager().hasRunningLoaders());

                return true;
            case R.id.top_rated_menu_item:
                editor.putString("movie", getResources().getString(R.string.pref_movie_value_top_rated));
                editor.apply();
                bundle.putString("bundle",getResources().getString(R.string.pref_movie_value_top_rated));
                    getSupportLoaderManager().restartLoader(LOADER_ID, bundle, this);

                return true;
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
}
