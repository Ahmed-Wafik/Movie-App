package com.example.android.movie.database;

import android.annotation.SuppressLint;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Arrays;
import java.util.HashSet;


@SuppressLint("Registered")
public class MoviesContentProvider extends ContentProvider {

    public static final int MOVIES = 100;
    public static final int MOVIE_BY_ID = 101;
    static final int FAVORITES = 300;


    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static final String MOVIE_ID_SELECTION =
            MovieContract.MovieEntries.TABLE_NAME + "." + MovieContract.MovieEntries._ID + " = ? ";
    private MovieDBHelper mDBHelper;

    private static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_MOVIES, MOVIES);
        uriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_MOVIES + "/#", MOVIE_BY_ID);
        uriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_MOVIES + "/" + MovieContract.PATH_FAVORITES, FAVORITES);

        return uriMatcher;
    }


    @Override
    public boolean onCreate() {
        mDBHelper = new MovieDBHelper(getContext());
        return true;
    }

    private void checkColumns(String[] projection) {
        if (projection != null) {
            HashSet<String> availableColumns = new HashSet<>(Arrays.asList(
                    MovieContract.MovieEntries.getColumns()));
            HashSet<String> requestedColumns = new HashSet<>(Arrays.asList(projection));
            if (!availableColumns.containsAll(requestedColumns)) {
                throw new IllegalArgumentException("Unknown columns in projection.");
            }
        }
    }

    private Cursor getMovieById(Uri uri, String[] projection, String sortOrder) {
        long id = MovieContract.MovieEntries.getIdFromUri(uri);
        String selection = MOVIE_ID_SELECTION;
        String[] selectionArgs = new String[]{Long.toString(id)};
        return mDBHelper.getReadableDatabase().query(
                MovieContract.MovieEntries.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getMoviesFromReferenceTable(String tableName, String[] projection, String selection,
                                               String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder sqLiteQueryBuilder = new SQLiteQueryBuilder();

        // tableName INNER JOIN movies ON tableName.movie_id = movies._id
        sqLiteQueryBuilder.setTables(
                tableName + " INNER JOIN " + MovieContract.MovieEntries.TABLE_NAME +
                        " ON " + tableName + "." + MovieContract.COLUMN_MOVIE_ID_KEY +
                        " = " + MovieContract.MovieEntries.TABLE_NAME + "." + MovieContract.MovieEntries._ID
        );

        return sqLiteQueryBuilder.query(mDBHelper.getReadableDatabase(),
                projection,
                null,
                null,
                null,
                null,
                null
        );
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = mDBHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);

        Cursor cursor;
        checkColumns(projection);
        switch (match) {
            case MOVIES:

                cursor = db.query(MovieContract.MovieEntries.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null);
                break;
            case MOVIE_BY_ID:
                cursor = getMovieById(uri, projection, sortOrder);
                break;
            case FAVORITES:
                cursor = getMoviesFromReferenceTable(MovieContract.Favorites.TABLE_NAME,
                        projection, selection, selectionArgs, sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("unknown Uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;

    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIES:
                return MovieContract.MovieEntries.CONTENT_DIR_TYPE;

            case MOVIE_BY_ID:
                return MovieContract.Favorites.CONTENT_DIR_TYPE;

            case FAVORITES:
                return MovieContract.MovieEntries.CONTENT_ITEM_TYPE;
            default:
                return null;
        }

    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = mDBHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        Uri returnUri;
        long id;
        switch (match) {
            case MOVIES:
                id = db.insertWithOnConflict(MovieContract.MovieEntries.TABLE_NAME, null,
                        contentValues, SQLiteDatabase.CONFLICT_REPLACE);
                if (id > 0) {
                    returnUri = MovieContract.MovieEntries.buildMovieUri(id);
                } else {
                    throw new android.database.SQLException("Unknown Uri: " + uri);
                }
                break;
            case FAVORITES:
                id = db.insert(MovieContract.Favorites.TABLE_NAME, null, contentValues);
                if (id > 0) {
                    returnUri = MovieContract.Favorites.CONTENT_URI;
                } else {
                    throw new android.database.SQLException("Unknown Uri: " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        final SQLiteDatabase db = mDBHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);

        int numOfDeletedMovie;

        switch (match) {

            case MOVIES:
                numOfDeletedMovie = db.delete(MovieContract.MovieEntries.TABLE_NAME, s, strings);
                break;

            case MOVIE_BY_ID:
                long id = MovieContract.MovieEntries.getIdFromUri(uri);
                numOfDeletedMovie = db.delete(MovieContract.MovieEntries.TABLE_NAME,
                        MOVIE_ID_SELECTION,
                        new String[]{Long.toString(id)});
                break;
            case FAVORITES:
                numOfDeletedMovie = db.delete(MovieContract.Favorites.TABLE_NAME, s, strings);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (numOfDeletedMovie != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        //return of task deleted
        return numOfDeletedMovie;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
    @Override
    public void shutdown() {
        mDBHelper.close();
        super.shutdown();
    }
}
