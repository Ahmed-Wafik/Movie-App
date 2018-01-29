package com.example.android;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.Utilities.NetworkUtils;
import com.example.android.adapter.FavoriteAdapter;
import com.example.android.adapter.MovieAdapter;
import com.example.android.model.Movie;
import com.example.android.model.MovieResponse;
import com.example.android.movie.R;
import com.example.android.provider.MovieProvider;
import com.example.android.rest.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MovieAdapter adapter;
    SharedPreferences sharedPreferences;
    ProgressBar progressBar;
    private String TAG = MainActivity.class.getSimpleName();
    private SearchView searchView;
    String path;

    private ApiInterface apiInterface;

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // toolbar fancy stuff
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // getSupportActionBar().setTitle(R.string.action_search);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        progressBar = findViewById(R.id.progress);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));


        apiInterface = NetworkUtils.getMoviesRequest().create(ApiInterface.class);

        path = sharedPreferences.getString(getString(R.string.pref_selection_key), getString(R.string.pref_movie_value_popular));
        showProgress();
        getRequestType(path);

        Log.i(TAG, "On Create");
    }

//    private class FetchMoviesData extends AsyncTask<String, Integer, List<Movie>> {
//        Movie movie;
//        List<Movie> movieList = new ArrayList<>();
//
//        @Override
//        protected void onPostExecute(List<Movie> movies) {
//            MovieAdapter reviewAdapter = new MovieAdapter(MainActivity.this, movies);
//            recyclerView.setVisibility(View.VISIBLE);
//            progressBar.setVisibility(View.GONE);
//            recyclerView.setAdapter(reviewAdapter);
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
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        if (searchManager != null) {
            searchView.setSearchableInfo(searchManager
                    .getSearchableInfo(getComponentName()));
        }
        searchView.setMaxWidth(Integer.MAX_VALUE);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                adapter.notifyDataSetChanged();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
//                adapter.getFilter().filter(newText);
//                adapter.notifyDataSetChanged();
       //         getSupportActionBar().setDisplayHomeAsUpEnabled(true);
               return false;
            }

        });


        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {

                getRequestType(path);
                return false;
            }

        });

        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "On start");

        String string = sharedPreferences.getString(getString(R.string.pref_selection_key), getString(R.string.pref_movie_value_popular));
       // getRequestType(string);


    }

    @SuppressLint("Recycle")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        switch (itemId) {

            case R.id.settings:
                Intent intent = new Intent(this, MoviePreferenceActivity.class);
                startActivity(intent);
                return true;
//            case R.id.favorites:
//
//                Cursor cursor = getContentResolver().query(MovieProvider.FavoriteMovies.CONTENT_URI, com.example.android.provider.MovieContract.getColumns(), null, null, null);
//                if (cursor == null) {
//                    Toast.makeText(this, "There is error in cursor", Toast.LENGTH_SHORT).show();
//                } else if (cursor.getCount() < 1) {
//                    Toast.makeText(this, "There is no Favorite movies ", Toast.LENGTH_SHORT).show();
//                } else if (cursor.getCount() > 1) {
//
//                    movieList = new ArrayList<>();
//
//                    if (cursor.moveToFirst()) {
//                        do {
//                            int id = cursor.getInt(cursor.getColumnIndex(MovieContract.COLUMN_ID));
//                            String title = cursor.getString(cursor.getColumnIndex(MovieContract.COLUMN_TITLE));
//                            String poster_path = cursor.getString(cursor.getColumnIndex(MovieContract.COLUMN_POSTER_PATH));
//                            String overview = cursor.getString(cursor.getColumnIndex(MovieContract.COLUMN_OVERVIEW));
//                            String backdrop_path = cursor.getString(cursor.getColumnIndex(MovieContract.COLUMN_BACKDROP_PATH));
//                            String release_date = cursor.getString(cursor.getColumnIndex(MovieContract.COLUMN_RELEASE_DATE));
//                            float vote_avg = cursor.getFloat(cursor.getColumnIndex(MovieContract.COLUMN_AVERAGE_VOTE));
//
//                            movieFav = new Movie(id, vote_avg, title, poster_path, backdrop_path, overview, release_date);
//
//                            movieList.add(movieFav);
//
//                        } while (cursor.moveToNext());
//                        Log.i(TAG, movieFav.getTitle());
//
//                        adapter = new MovieAdapter(this, movieList);
//                        recyclerView.removeAllViews();
//                        recyclerView.setAdapter(adapter);
//
//                    }
//                    cursor.close();
//                }
//                return true;

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


    public void getRequestType(String path) {
        showProgress();
        Log.i(TAG, path);
        if (path.equals(getString(R.string.pref_movie_value_popular))) {
            Call<MovieResponse> popularMovies = apiInterface.getPopularMovies();
            popularMovies.enqueue(new Callback<MovieResponse>() {
                @Override
                public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                    List<Movie> results = response.body().getResults();
                    adapter = new MovieAdapter(MainActivity.this, results);
                    hideProgress();
                    recyclerView.setAdapter(adapter);

                }

                @Override
                public void onFailure(Call<MovieResponse> call, Throwable t) {
                    Log.i(TAG, t.toString());
                    hideProgress();
                }
            });
        } else if (path.equals(getString(R.string.pref_movie_value_top_rated))) {
            Call<MovieResponse> topRatedMovie = apiInterface.getTopRatedMovies();
            topRatedMovie.enqueue(new Callback<MovieResponse>() {
                @Override
                public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                    List<Movie> results = response.body().getResults();
                    adapter = new MovieAdapter(MainActivity.this, results);
                    hideProgress();
                    recyclerView.setAdapter(adapter);
                }

                @Override
                public void onFailure(Call<MovieResponse> call, Throwable t) {
                    Log.i(TAG, t.toString());
                    hideProgress();
                }
            });
        } else if (path.equals(getString(R.string.favorites_value))) {
            Cursor cursor = getContentResolver().query(MovieProvider.FavoriteMovies.CONTENT_URI, com.example.android.provider.MovieContract.getColumns(), null, null, null);

            if (cursor == null) {
                Toast.makeText(this, "Error loading favorites movies from database ", Toast.LENGTH_SHORT).show();
            } else if (cursor.getCount() < 1) {
                Toast.makeText(this, "There is no favorite movies" + cursor.getCount(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Favorites" + cursor.getCount(), Toast.LENGTH_SHORT).show();
                FavoriteAdapter favoriteAdapter = new FavoriteAdapter(MainActivity.this);
                favoriteAdapter.swapCursor(cursor);
                recyclerView.setAdapter(favoriteAdapter);
            }
        }
        hideProgress();
    }
}
