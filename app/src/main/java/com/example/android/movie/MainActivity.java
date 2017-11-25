package com.example.android.movie;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

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

    static RecyclerView recyclerView ;


    private String TAG = MainActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.main_recycle_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));

        new FetchMoviesData().execute();
    }


    private class FetchMoviesData extends AsyncTask<Void, Void, List<Movie>> {
        Movie movie;
        List<Movie> movieList = new ArrayList<>();
        @Override
        protected void onPostExecute(List<Movie> movies) {
            RecycleViewAdapter adapter= new RecycleViewAdapter(MainActivity.this,movies);
            recyclerView.setAdapter(adapter);
        }

        @Override
        protected List<Movie> doInBackground(Void... voids) {

            try {
                URL url = NetworkUtils.buildURL_Movies();
                Log.i(TAG,url.toString());
                String responseFromHttpUrl = NetworkUtils.getResponseFromHttpUrl(url);
                JSONObject jsonObject = new JSONObject(responseFromHttpUrl);

                JSONArray jsonArray = jsonObject.getJSONArray("results");
                Log.i(TAG,jsonArray.toString());
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject object = jsonArray.getJSONObject(i);
                    String poster_Path = object.getString(Movie.sPoster_path);
                    String title = object.getString(Movie.sTitle);

                    movie = new Movie();
                    movie.setPoster_path(poster_Path);
                    Log.i("ascasc",movie.getPoster_path());
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
}
