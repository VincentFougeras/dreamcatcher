package com.lollipop.dreamcatcher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import model.Dream;

public class DetailsDream extends BaseActivity {

    private Dream dream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_dream);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.dream = getIntent().getParcelableExtra(Home.EXTRA_DREAM);

        //Get the TextViews
        TextView tv_date = (TextView) findViewById(R.id.tv_date);
        TextView tv_title = (TextView) findViewById(R.id.tv_title);
        TextView tv_content = (TextView) findViewById(R.id.tv_content);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        tv_date.setText(sdf.format(this.dream.getCreationDateFormatted()));

        tv_title.setText(this.dream.getTitle());
        tv_content.setText(this.dream.getContent());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_details_dream, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.menu_details_dream_share:
                shareDream();
                return true;
            case R.id.menu_details_dream_delete:
                deleteOrUpdateDream(true);
                return true;
            case R.id.menu_details_dream_update:
                deleteOrUpdateDream(false);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void shareDream() {
        // Make a share intent
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.putExtra(Intent.EXTRA_TEXT, this.dream.getContent());
        startActivity(Intent.createChooser(share, getResources().getString(R.string.share_dream)));
    }

    /**
        Delete or update the current dream
        @param toDelete true if the dream is to be deleted, false if the dream is to be updated
     */
    private void deleteOrUpdateDream(boolean toDelete){
        Intent result = new Intent();
        result.putExtra(Home.EXTRA_DELETE_DREAM, toDelete);
        result.putExtra(Home.EXTRA_DREAM_ID, dream.getId());
        setResult(Activity.RESULT_OK, result);
        this.finish();
    }
}
