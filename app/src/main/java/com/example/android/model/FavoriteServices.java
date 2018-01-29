package com.example.android.model;

import android.content.Context;
import android.database.Cursor;

import com.example.android.provider.MovieContract;
import com.example.android.provider.MovieProvider;

public class FavoriteServices {

    private final Context context;

    public FavoriteServices(Context context) {
        this.context = context.getApplicationContext();
    }

    public void addToFavorite(Movie movie) {
        context.getContentResolver().insert(MovieProvider.FavoriteMovies.CONTENT_URI, movie.toContentValues());

//        ContentValues contentValues = new ContentValues();
//        contentValues.put(MovieContract.COLUMN_MOVIE_ID_KEY, movie.getId());
//        context.getContentResolver().insert(MovieContract.Favorites.CONTENT_URI, contentValues);

    }

    public void removeFromFavorite(Movie movie) {
//        context.getContentResolver().delete(MovieContract.Favorites.CONTENT_URI,
//                MovieContract.COLUMN_MOVIE_ID_KEY + " = " + movie.getId(),
//                null);
        context.getContentResolver().delete(MovieProvider.FavoriteMovies.CONTENT_URI,
                MovieContract.COLUMN_ID+ " = " + movie.getId(),
                null);

    }

    public boolean isFavorite(Movie movie) {
        boolean favorite = false;
//        Cursor cursor = context.getContentResolver()
//                .query(MovieContract.Favorites.CONTENT_URI,
//                null,
//                MovieContract.COLUMN_MOVIE_ID_KEY +" = "+movie.getId(),
//                null,
//                null);
        Cursor cursor = context.getContentResolver()
                .query(MovieProvider.FavoriteMovies.CONTENT_URI,
                        MovieContract.getColumns(),
                        MovieContract.COLUMN_ID + " = " + movie.getId(),
                        null,
                        null);

        if (cursor != null) {
            favorite = cursor.getCount() != 0;
            cursor.close();
        }
        return favorite;
    }
}
