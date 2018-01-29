package com.example.android.provider;

import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.PrimaryKey;

/**
 * Created by Ahmed Wafik Mohamed on 1/20/2018.
 */

public class MovieContract {

    @DataType(DataType.Type.INTEGER)
    @PrimaryKey
    public static final String COLUMN_ID = "_id";

    @DataType(DataType.Type.TEXT)
    public static final String COLUMN_OVERVIEW = "overview";

    @DataType(DataType.Type.TEXT)
    public static final String COLUMN_RELEASE_DATE = "release_date";

    @DataType(DataType.Type.TEXT)
    public static final String COLUMN_POSTER_PATH = "poster_path";

    @DataType(DataType.Type.TEXT)
    public static final String COLUMN_TITLE = "title";

    @DataType(DataType.Type.TEXT)
    public static final String COLUMN_AVERAGE_VOTE = "vote_average";

    @DataType(DataType.Type.TEXT)
    public static final String COLUMN_BACKDROP_PATH = "backdrop_path";

    private static final String[] COLUMNS = {COLUMN_ID, COLUMN_OVERVIEW,
            COLUMN_RELEASE_DATE, COLUMN_POSTER_PATH, COLUMN_TITLE,
            COLUMN_AVERAGE_VOTE, COLUMN_BACKDROP_PATH};

    public static String[] getColumns() {
        return COLUMNS.clone();
    }
}
