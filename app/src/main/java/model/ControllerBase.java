package model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Vincent on 21/02/2016.
 */
public abstract class ControllerBase {
    protected final static int VERSION = 2;
    // Le nom du fichier qui représente ma base
    protected final static String NOM = "dreamCatcher.db";

    protected SQLiteDatabase mDb = null;
    protected DatabaseHandler mHandler = null;

    public ControllerBase(Context pContext) {
        this.mHandler = new DatabaseHandler(pContext, NOM, null, VERSION);
    }

    public SQLiteDatabase open() {
        mDb = mHandler.getWritableDatabase();
        return mDb;
    }

    public void close() {
        mDb.close();
    }

    public SQLiteDatabase getDb() {
        return mDb;
    }
}