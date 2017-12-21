package com.lollipop.dreamcatcher;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import model.Dream;
import model.DreamController;

/**
 * Created by Vincent on 20/02/2016.
 */
public class DreamListAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<Dream> dreamList;
    private DreamController dreamController;

    public DreamListAdapter(Context context, DreamController dreamController){
        this.dreamController = dreamController;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.dreamList = new ArrayList<Dream>(this.dreamController.getDreamList());
        this.orderByDate();
    }

    public void orderByDate() {
        Collections.sort(this.dreamList,
                new Comparator<Dream>() {
                    public int compare(Dream ld, Dream rd) {
                        return rd.getCreationDateFormatted().compareTo(ld.getCreationDateFormatted());
                    }
                }
        );
        this.notifyDataSetChanged();
    }

    public void orderByTitle(){
        //Sort the array
        Collections.sort(this.dreamList,
                new Comparator<Dream>() {
                    public int compare(Dream ld, Dream rd) {
                        return ld.getTitle().compareTo(rd.getTitle());
                    }
                }
        );
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return this.dreamList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.dreamList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return this.dreamList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Get the layout
        LinearLayout layout;
        if(convertView == null){
            layout = (LinearLayout) this.layoutInflater.inflate(R.layout.dream_list_item, null);
        }
        else {
            layout = (LinearLayout) convertView;
        }

        //Get the TextViews
        TextView tv_date = (TextView) layout.findViewById(R.id.tv_item_date);
        TextView tv_title = (TextView) layout.findViewById(R.id.tv_item_title);
        TextView tv_content = (TextView) layout.findViewById(R.id.tv_item_content);

        //Get the current dream
        Dream dream = this.dreamList.get(position);

        //Display the appropriate elements in the item view
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        tv_date.setText(sdf.format(dream.getCreationDateFormatted()));

        tv_title.setText(dream.getTitle());

        String dreamContentText = dream.getContent();
        if(dreamContentText.length() > 120){
            dreamContentText = dreamContentText.substring(0, 117) + "...";
        }
        tv_content.setText(dreamContentText);

        return layout;
    }
}
