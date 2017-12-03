package com.example.android.movie.Utilities;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NetworkUtils {

    //private static final String BASE_URL_IMG = "https://image.tmdb.org/t/p/w342";
    private static final String API_KEY = "1d6d44ed2156ccd55f118d5018edcfa2";
    private static final String TAG = NetworkUtils.class.getSimpleName();
    private static final String BASE_URL_MOVIES = "https://api.themoviedb.org/3/movie";
    private static final String API_KEY_ST = "api_key";

    public static URL buildURL_Movies(String path) {
        Uri uri = Uri.parse(BASE_URL_MOVIES).buildUpon()
                .appendPath(path)
                .appendQueryParameter(API_KEY_ST, API_KEY).build();
        URL url;
        try {
            url = new URL(uri.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return url;

    }

    public static URL builURL_Movie_Review(int id) {
        Uri.Builder uri = Uri.parse(BASE_URL_MOVIES).buildUpon();
        uri.appendPath(String.valueOf(id))
                .appendPath("reviews")
                .appendQueryParameter(API_KEY_ST,API_KEY)
                .appendQueryParameter("language","en-US")
                .appendQueryParameter("page","1")
                .build();

        URL url = null;
        try {
            url = new URL(uri.toString());
            Log.i(TAG,url.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String buildURL_Image(String posterImage) {

        Uri.Builder uri = new Uri.Builder();

        uri.scheme("https")
                .authority("image.tmdb.org")
                .appendPath("t")
                .appendPath("p")
                .appendPath("w342")
                .appendPath(posterImage.substring(1))
                .build();

        return uri.toString();

    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();

        return response.body().string();
    }

}
