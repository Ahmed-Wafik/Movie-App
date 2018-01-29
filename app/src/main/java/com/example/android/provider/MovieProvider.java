package com.example.android.provider;


import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

@ContentProvider(
        authority = MovieProvider.AUTHORITY,
        database = MovieDataBase.class)
public final class MovieProvider {

    public static final String AUTHORITY = "com.example.android.movieApp";


    @TableEndpoint(table = MovieDataBase.FavMovies)
    public static class FavoriteMovies {

        @ContentUri(
                path = "movies",
                type = "vnd.android.cursor.dir/movies")
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/movies");

    }
}