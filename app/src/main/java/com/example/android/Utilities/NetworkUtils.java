package com.example.android.Utilities;

import android.net.Uri;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkUtils {
    private static final String BASE_URL = "https://api.themoviedb.org/3/";

    //private static final String BASE_URL_IMG = "https://image.tmdb.org/t/p/w342";
    private static final String API_KEY = "1d6d44ed2156ccd55f118d5018edcfa2";

    private static final String BASE_URL_YOUTUBE = "http://www.youtube.com/watch";

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String API_KEY_ST = "api_key";

    private static Retrofit retrofit;

    public static Retrofit getMoviesRequest() {

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
             
        }
        return retrofit;
    }

    public static String buildURL_Image(String posterImage) {

        Uri.Builder uri = new Uri.Builder();
        if (posterImage != null) {
            uri.scheme("https")
                    .authority("image.tmdb.org")
                    .appendPath("t")
                    .appendPath("p")
                    .appendPath("w185")
                    .appendPath(posterImage.substring(1))
                    .build();
        }
        return uri.toString();

    }

    public static String buildURL_Image_backdrop(String posterImage) {

        Uri.Builder uri = new Uri.Builder();
        if (posterImage != null) {
            uri.scheme("https")
                    .authority("image.tmdb.org")
                    .appendPath("t")
                    .appendPath("p")
                    .appendPath("w780")
                    .appendPath(posterImage.substring(1))
                    .build();
        }
        return uri.toString();

    }

    public static String buildURL_Youtube(String key) {

        Uri uri = Uri.parse(BASE_URL_YOUTUBE);
        Uri builder = uri.buildUpon().appendQueryParameter("v", key).build();

        return builder.toString();
    }

    public static String buildURL_thumbnail(String key) {
        final String BASE_THuMBNAIL_URL = "https://img.youtube.com/vi/";
        Uri uri = Uri.parse(BASE_THuMBNAIL_URL);
        Uri build = uri.buildUpon().appendPath(key)
                .appendPath("sddefault.jpg").build();
        return build.toString();
    }


}
