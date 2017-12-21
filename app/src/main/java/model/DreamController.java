package model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;

import com.lollipop.dreamcatcher.DreamCursorAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vincent on 21/02/2016.
 */
public class DreamController extends ControllerBase {

    public DreamController(Context pContext) {
        super(pContext);
    }

    public void addDream(Dream dream){
        ContentValues value = new ContentValues();
        value.put(DatabaseHandler.DREAM_TITLE, dream.getTitle());
        value.put(DatabaseHandler.DREAM_CONTENT, dream.getContent());
        value.put(DatabaseHandler.DREAM_CREATION_DATE, dream.getCreationDate());
        this.open();
        mDb.insert(DatabaseHandler.DREAM_TABLE_NAME, null, value);
        this.close();
    }

    public void deleteDream(long id){
        this.open();
        mDb.delete(DatabaseHandler.DREAM_TABLE_NAME, DatabaseHandler.DREAM_KEY + " = ?", new String[]{String.valueOf(id)});
        this.close();
    }

    public void updateDream(Dream dream){
        this.open();
        ContentValues value = new ContentValues();
        value.put(DatabaseHandler.DREAM_TITLE, dream.getTitle());
        value.put(DatabaseHandler.DREAM_CONTENT, dream.getContent());
        mDb.update(DatabaseHandler.DREAM_TABLE_NAME, value, DatabaseHandler.DREAM_KEY + " = ?", new String[]{String.valueOf(dream.getId())});
        this.close();
    }

    public Dream getDream(long id){
        this.open();
        Cursor c = mDb.rawQuery("select * from " + DatabaseHandler.DREAM_TABLE_NAME + " where " + DatabaseHandler.DREAM_KEY + " = ?", new String[]{String.valueOf(id)});
        c.moveToFirst();
        Dream dream = new Dream(c.getLong(0), c.getString(1), c.getString(2), c.getLong(3));
        this.close();
        return dream;
    }

    public List<Dream> getDreamList(){
        List<Dream> dreamList = new ArrayList<Dream>();
        this.open();
        Cursor c = mDb.rawQuery("select * from " + DatabaseHandler.DREAM_TABLE_NAME, null);
        while(c.moveToNext()){
            dreamList.add(new Dream(c.getLong(0), c.getString(1), c.getString(2), c.getLong(3)));
        }
        c.close();
        this.close();
        return dreamList;
    }

    public Cursor getDreamCursor(String sort){
        this.open();
        Cursor c;
        // Sort by date
        if(sort.equals(DatabaseHandler.DREAM_CREATION_DATE)){
            c = mDb.rawQuery("select * from " + DatabaseHandler.DREAM_TABLE_NAME + " order by " + DatabaseHandler.DREAM_CREATION_DATE + " desc", null);
        }
        // Else sort by title
        else {
            c = mDb.rawQuery("select * from " + DatabaseHandler.DREAM_TABLE_NAME + " order by " + DatabaseHandler.DREAM_TITLE + " collate nocase asc", null);
        }
        return c;
    }

    public Cursor getDreamCursorFiltered(String searchInput, String sort){
        searchInput = "%" + searchInput + "%";
        this.open();
        Cursor c;
        // Sort by date
        if(sort.equals(DatabaseHandler.DREAM_CREATION_DATE)){
            c = mDb.rawQuery("select * from " + DatabaseHandler.DREAM_TABLE_NAME
                    + " where "  + DatabaseHandler.DREAM_CONTENT + " like ? "
                    + "or " + DatabaseHandler.DREAM_TITLE + " like ? "
                    + "order by " + DatabaseHandler.DREAM_CREATION_DATE + " desc", new String[]{searchInput, searchInput});
        }
        // Else sort by title
        else {
            c = mDb.rawQuery("select * from " + DatabaseHandler.DREAM_TABLE_NAME
                    + " where "  + DatabaseHandler.DREAM_CONTENT + " like ? "
                    + "or " + DatabaseHandler.DREAM_TITLE + " like ? "
                    + "order by " + DatabaseHandler.DREAM_TITLE + " collate nocase asc", new String[]{searchInput, searchInput});
        }
        return c;
    }

    public boolean emptyDatabase() {
        this.open();
        long nbRows = DatabaseUtils.queryNumEntries(mDb, DatabaseHandler.DREAM_TABLE_NAME);
        this.close();
        if(nbRows == 0){
            return true;
        }
        else {
            return false;
        }
    }
}
