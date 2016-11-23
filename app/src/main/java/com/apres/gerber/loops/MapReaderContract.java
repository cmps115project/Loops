package com.apres.gerber.loops;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by marcholtsman on 11/15/16.
 */

public final class MapReaderContract {
    public MapReaderContract() {}

    public static abstract class MapEntry implements BaseColumns {
        public static final String TABLE_NAME = "CLASS_TABLE";
        public static final String COLUMN_NAME_ENTRY_ID = "entryid";
        public static final String COLUMN_ROUTE = "ROUTE";
       // public static final String COLUMN_DISTANCE = "DISTANCE";
       // public static final String COLUMN_ALTITUDE = "ALTITUDE";
    }

}
