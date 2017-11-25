package com.example.android.movie.Utilities;

import android.net.Uri;
import java.io.IOException;
import java.net.URL;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NetworkUtils {

    //private static final String BASE_URL_IMG = "https://image.tmdb.org/t/p/w342";
    private static final String API_KEY = "1d6d44ed2156ccd55f118d5018edcfa2";
    private static final String TAG = NetworkUtils.class.getSimpleName();
    private static final String BASE_URL_MOVIES = "https://api.themoviedb.org/3/movie/popular";

    public static URL buildURL_Movies() {

        Uri uri = Uri.parse(BASE_URL_MOVIES).buildUpon().appendQueryParameter("api_key", API_KEY).build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (IOException e) {
            e.printStackTrace();
           return null;
        }

        return url;

    }

    public static String buildURL_Image(String posterImage) {

        Uri.Builder uri= new Uri.Builder();

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
