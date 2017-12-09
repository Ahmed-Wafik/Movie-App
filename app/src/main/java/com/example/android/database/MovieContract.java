package com.example.android.database;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class MovieContract {

    public static final String AUTHORITY = "com.example.android.movieApp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_MOVIES = "movies";

    private MovieContract() {
    }

    public static final class MovieEntries implements BaseColumns {

        private MovieEntries() {
        }

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();
        public static final String CONTENT_DIR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_MOVIES;

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


}
