package com.lollipop.dreamcatcher;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import model.DatabaseHandler;
import model.Dream;
import model.DreamController;

/**
 * Created by Vincent on 22/02/2016.
 */
public class DreamCursorAdapter extends ResourceCursorAdapter {


    public DreamCursorAdapter(Context context, int layout, Cursor c, int flags) {
        super(context, layout, c, flags);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        //Get the TextViews
        TextView tv_date = (TextView) view.findViewById(R.id.tv_item_date);
        TextView tv_title = (TextView) view.findViewById(R.id.tv_item_title);
        TextView tv_content = (TextView) view.findViewById(R.id.tv_item_content);

        //Construct current dream
        long id = cursor.getLong(cursor.getColumnIndex(DatabaseHandler.DREAM_KEY));
        String title = cursor.getString(cursor.getColumnIndex(DatabaseHandler.DREAM_TITLE));
        String content = cursor.getString(cursor.getColumnIndex(DatabaseHandler.DREAM_CONTENT));
        long date = cursor.getLong(cursor.getColumnIndex(DatabaseHandler.DREAM_CREATION_DATE));
        Dream dream = new Dream(id, title, content, date);

        //Display the appropriate elements in the item view
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        tv_date.setText(sdf.format(dream.getCreationDateFormatted()));

        tv_title.setText(dream.getTitle());

        tv_content.setText(dream.getContent());
    }

    public void update(Cursor cursor) {
        this.changeCursor(cursor);
    }
}
