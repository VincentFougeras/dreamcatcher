package com.lollipop.dreamcatcher;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import model.Dream;
import model.DreamController;

public class UpdateDream extends BaseActivity {

    private Dream dream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_dream);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        // Set default text
        this.dream = getIntent().getParcelableExtra(Home.EXTRA_DREAM);

        EditText title = (EditText) findViewById(R.id.update_dream_title);
        EditText content = (EditText) findViewById(R.id.update_dream_content);

        title.setText(dream.getTitle());
        content.setText(dream.getContent());
    }

    public void inputUpdateDream(){
        String title = ((EditText) findViewById(R.id.update_dream_title)).getText().toString();
        String content = ((EditText) findViewById(R.id.update_dream_content)).getText().toString();

        this.dream.setTitle(title);
        this.dream.setContent(content);
        DreamController dc = new DreamController(this);
        dc.updateDream(this.dream);
        setResult(Activity.RESULT_OK);
        this.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_new_dream, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_add_dream:
                inputUpdateDream();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
