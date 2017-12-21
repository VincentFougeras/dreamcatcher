package com.lollipop.dreamcatcher;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.SearchManager;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.IntentCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

import model.DatabaseHandler;
import model.Dream;
import model.DreamController;

public class Home extends BaseActivity implements AdapterView.OnItemClickListener, SearchView.OnQueryTextListener, SharedPreferences.OnSharedPreferenceChangeListener {

    protected static final String EXTRA_DREAM = "com.lollipop.dreamcatcher.dream";
    // Extras when returning from the details
    protected static final String EXTRA_DELETE_DREAM = "com.lollipop.dreamcatcher.delete_dream";
    protected static final String EXTRA_DREAM_ID = "com.lollipop.dreamcatcher.dream_id";


    public static final int RESULT_NEW_DREAM = 100;
    public static final int RESULT_UPDATE_DREAM = 200;
    public static final int RESULT_DETAILS_DREAM = 300;

    private String sort;

    private Dream tempDeletedDream;

    //DreamListAdapter dreamListAdapter;
    DreamCursorAdapter dreamCursorAdapter;
    DreamController dreamController;

    //Dynamically change theme
    boolean newTheme = false;
    SharedPreferences sharedPreferences;

    // Request code : identify the notification service in an unique way
    public static final int REQUEST_CODE = 4567;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*Default android activity */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Home.this.addDream();
            }
        });

        /* My content */

        // By default, sort by date
        this.sort = DatabaseHandler.DREAM_CREATION_DATE;

        //List of dreams
        this.dreamController = new DreamController(this);

        //List data and adapter

        //this.dreamListAdapter = new DreamListAdapter(this, this.dreamController);

        this.dreamCursorAdapter = new DreamCursorAdapter(this, R.layout.dream_list_item, dreamController.getDreamCursor(this.sort), 0);

        ListView listView = (ListView) this.findViewById(R.id.listDream);
        listView.setAdapter(dreamCursorAdapter);

        listView.setOnItemClickListener(this);
        this.registerForContextMenu(listView);

        //Default dream if none exists
        this.addInitialDream();
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        // Change theme
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        this.sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
        // Theme changed
        if (key.equals(SettingsActivity.KEY_PREF_DARK_MODE)) {
            /*final Intent intent = getIntent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);*/
            newTheme = true;
        }
        // Alarm changed
        if (key.equals(SettingsActivity.KEY_PREF_NOTIFICATION)) {
            boolean notifActivated = prefs.getBoolean(SettingsActivity.KEY_PREF_NOTIFICATION, false);
            if(notifActivated){
                createReceiver();
            }
            else {
                stopReceiver();
            }
        }
    }

    /*
       Create the BroadcastReceiver that will make a notification at 5:00
     */
    public void createReceiver(){
        Intent intent = new Intent(Home.this, Receiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(Home.this, REQUEST_CODE, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // Set the calendar at 5:00
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 5);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    /*
        Cancel the notification
     */
    public void stopReceiver(){
        Intent intent = new Intent(Home.this, Receiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(Home.this, REQUEST_CODE, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(newTheme)
        {
            newTheme = false;
            /*Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage( getBaseContext().getPackageName() );
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);*/
            final Intent intent = getIntent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    private void addInitialDream() {
        if(dreamController.emptyDatabase()){
            String title = getResources().getString(R.string.initial_dream_title);
            String content = getResources().getString(R.string.initial_dream_content);
            dreamController.addDream(new Dream(title, content));
            this.updateDreamList();
        }
    }

    /* OPTIONS MENU */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        // Associate searchable configuration with the SearchView
        MenuItem searchItem = menu.findItem(R.id.search_dream);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        // User pressed the search button
        //if(query.length() > 0){
            this.filter(query);
        //}
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        // User changed the text
        //if(newText.length() > 0) {
            this.filter(newText);
        //}
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                this.startSettings();
                return true;
            case R.id.action_sort_date:
                if (item.isChecked());
                else item.setChecked(true);
                this.sortByDate();
                return true;
            case R.id.action_sort_title:
                if (item.isChecked());
                else item.setChecked(true);
                this.sortByTitle();
                return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == RESULT_NEW_DREAM){
            if(resultCode == Activity.RESULT_OK){
                this.updateDreamList();
            }
        }
        else if(requestCode == RESULT_UPDATE_DREAM){
            if(resultCode == Activity.RESULT_OK){
                this.updateDreamList();
            }
        }
        else if(requestCode == RESULT_DETAILS_DREAM){
            if(resultCode == Activity.RESULT_OK){
                // Dream to modify or delete
                boolean toDelete = data.getBooleanExtra(EXTRA_DELETE_DREAM, false);
                long dreamId = data.getLongExtra(EXTRA_DREAM_ID, -1);
                if(dreamId == -1){
                    Log.e("Home", "erreur : ID du reve non specifie");
                }
                else {
                    if(toDelete){
                        deleteDream(dreamId);
                    }
                    else {
                        updateDream(dreamId);
                    }
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }



    /* DREAM LIST OPTIONS */
    private void updateDreamList(){
        this.dreamCursorAdapter.update(dreamController.getDreamCursor(this.sort));
    }
    public void sortByDate(){
        this.dreamCursorAdapter.update(dreamController.getDreamCursor(DatabaseHandler.DREAM_CREATION_DATE));
        this.sort = DatabaseHandler.DREAM_CREATION_DATE;
    }
    public void sortByTitle(){
        this.dreamCursorAdapter.update(dreamController.getDreamCursor(DatabaseHandler.DREAM_TITLE));
        this.sort = DatabaseHandler.DREAM_TITLE;
    }
    public void filter(String query){
        this.dreamCursorAdapter.update(dreamController.getDreamCursorFiltered(query, this.sort));
    }

    /* DREAM LIST CONTEXT MENU */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_listview, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        //Choice
        int menuItemId = item.getItemId();

        // Position of the item selected
        AdapterView.AdapterContextMenuInfo
                info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        // ID of the dream selected
        long id = info.id;

        switch(menuItemId){
            case R.id.menu_listview_update:
                this.updateDream(id);
                break;
            case R.id.menu_listview_delete:
                this.deleteDream(id);
                break;
            case R.id.menu_listview_share:
                this.shareDream(id);
                break;
        }
        return super.onContextItemSelected(item);
    }


    /* DREAM ACTIONS */
    // Details dream
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, DetailsDream.class);
        Dream dream = this.dreamController.getDream(id);
        intent.putExtra(EXTRA_DREAM, dream);
        startActivityForResult(intent, RESULT_DETAILS_DREAM);
    }

    private void addDream() {
        Intent intent = new Intent(this, AddDream.class);
        startActivityForResult(intent, RESULT_NEW_DREAM);
    }

    private void updateDream(long id){
        Intent intent = new Intent(this, UpdateDream.class);
        Dream dream = this.dreamController.getDream(id);
        intent.putExtra(EXTRA_DREAM, dream);
        startActivityForResult(intent, RESULT_UPDATE_DREAM);
    }

    private void deleteDream(long id){
        //Temporarily store the dream in case the user undoes the action
        this.tempDeletedDream = this.dreamController.getDream(id);

        //Delete the dream from the database
        dreamController.deleteDream(id);
        this.updateDreamList();

        //Offer user to undo the action
        View view = findViewById(android.R.id.content);
        Snackbar.make(view, getResources().getString(R.string.snackbar_deleted_dream), Snackbar.LENGTH_LONG).
                setAction(getResources().getString(R.string.undo), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dreamController.addDream(tempDeletedDream);
                        updateDreamList();
                    }
                }).show();
    }

    private void shareDream(long id) {
        Dream dream = this.dreamController.getDream(id);
        // Make a share intent
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, dream.getContent());
        startActivity(Intent.createChooser(share, getResources().getString(R.string.share_dream)));
    }

    public void startSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}
