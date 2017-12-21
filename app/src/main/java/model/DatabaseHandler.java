package model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Vincent on 21/02/2016.
 */
public class DatabaseHandler extends SQLiteOpenHelper {
    public static final String DREAM_KEY = "_id";
    public static final String DREAM_TITLE = "title";
    public static final String DREAM_CONTENT = "content";
    public static final String DREAM_CREATION_DATE = "creationDate";

    public static final String DREAM_TABLE_NAME = "Dream";
    public static final String DREAM_TABLE_CREATE =
            "CREATE TABLE " + DREAM_TABLE_NAME + " (" +
                    DREAM_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    DREAM_TITLE + " TEXT, " +
                    DREAM_CONTENT + " TEXT, " +
                    DREAM_CREATION_DATE + " INTEGER);";

    public static final String DREAM_TABLE_DROP = "DROP TABLE IF EXISTS " + DREAM_TABLE_NAME + ";";

    public DatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DREAM_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DREAM_TABLE_DROP);
        onCreate(db);
    }
}