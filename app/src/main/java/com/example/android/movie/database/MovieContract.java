package com.example.android.movie.database;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class MovieContract {

    public static final String AUTHORITY = "com.example.android.movieApp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_MOVIES = "movies";
    public static final String PATH_FAVORITES = "favorites";
    public static final String COLUMN_MOVIE_ID_KEY = "movie_id";

    private MovieContract() {
    }

    public static final class MovieEntries implements BaseColumns {

        private MovieEntries() {
        }

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();
        public static final String CONTENT_DIR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_MOVIES;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_MOVIES;

        public static final String TABLE_NAME = "movies";


        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_AVERAGE_VOTE = "vote_average";
        public static final String COLUMN_BACKDROP_PATH = "backdrop_path";

        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID +                   " INTEGER PRIMARY KEY, " +
                        COLUMN_OVERVIEW +       " TEXT, " +
                        COLUMN_RELEASE_DATE +   " TEXT, " +
                        COLUMN_POSTER_PATH +    " TEXT, " +
                        COLUMN_TITLE +          " TEXT, " +
                        COLUMN_AVERAGE_VOTE +   " REAL, " +
                        COLUMN_BACKDROP_PATH +  " TEXT " +
                        " );";

        private static final String[] COLUMNS = {_ID,COLUMN_OVERVIEW,
                COLUMN_RELEASE_DATE, COLUMN_POSTER_PATH, COLUMN_TITLE,
                COLUMN_AVERAGE_VOTE, COLUMN_BACKDROP_PATH};

        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static long getIdFromUri(Uri uri) {
            return ContentUris.parseId(uri);
        }

        public static String[] getColumns() {
            return COLUMNS.clone();
        }

    }

    public static final class Favorites implements BaseColumns {
        public static final Uri CONTENT_URI = MovieEntries.CONTENT_URI.buildUpon()
                .appendPath(PATH_FAVORITES)
                .build();

        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_MOVIES
                        + "/" + PATH_FAVORITES;

        public static final String TABLE_NAME = "favorites";
        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" +
                        _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_MOVIE_ID_KEY + " INTEGER NOT NULL, " +

                        " FOREIGN KEY (" + COLUMN_MOVIE_ID_KEY + ") REFERENCES " +
                        MovieEntries.TABLE_NAME + " (" + MovieEntries._ID + ") " +

                        " );";
        private static final String[] COLUMNS = {_ID, COLUMN_MOVIE_ID_KEY};

        private Favorites() {
        }

        public static String[] getColumns() {
            return COLUMNS.clone();
        }
    }
}
