package com.example.android.provider;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;


@Database(version = MovieDataBase.VERSION)
public class MovieDataBase {

    public static final int VERSION = 1;

    @Table(MovieContract.class)
    public static final String FavMovies = "Favorite_Movies";
}
