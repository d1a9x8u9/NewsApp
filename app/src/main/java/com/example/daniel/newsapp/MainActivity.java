package com.example.daniel.newsapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.daniel.newsapp.utilites.Contract;
import com.example.daniel.newsapp.utilites.DBHelper;
import com.example.daniel.newsapp.utilites.DatabaseUtils;



public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Void>, MyAdapter.ItemClickListener{

    private ProgressBar progress;
    private RecyclerView rv;
    private MyAdapter adapter;
    private Cursor cursor;
    private SQLiteDatabase db;

    private static final int NEWSFEED_LOADER = 1;
    static final String TAG = "mainactivity";

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            progress = (ProgressBar) findViewById(R.id.progressBar);
            rv = (RecyclerView) findViewById(R.id.recyclerView);
            rv.setLayoutManager(new LinearLayoutManager(this));

            // Checks if first time install, if it is, then load, else schedule jobs and
            // load news from db
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            boolean isFirst = prefs.getBoolean("isfirst", true);

            if (isFirst) {
                load();
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("isfirst", false);
                editor.commit();
            }

            ScheduleUtilities.scheduleRefresh(this);
    }

    // After onCreate, this starts. It'll get the current iteration of our db, assign cursor to the first entry
    // then set adapter to db and set it to our rv
    @Override
    protected void onStart() {
        super.onStart();
        db = new DBHelper(MainActivity.this).getReadableDatabase();
        cursor = DatabaseUtils.getAll(db);
        adapter = new MyAdapter(cursor, this);
        rv.setAdapter(adapter);
    }

    // When app stops, close our db and cursor
    @Override
    protected void onStop() {
        super.onStop();
        db.close();
        cursor.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    // When REFRESH is clicked, refresh and fetch new data
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemNumber = item.getItemId();
        if (itemNumber == R.id.search) {
            load();
        }
        return true;
    }

    private void load() {
        LoaderManager loaderManager = getSupportLoaderManager();
        loaderManager.restartLoader(NEWSFEED_LOADER, null, this).forceLoad();
    }

    // When called, it'll show spinning loader icon and refresh the articles via api in the background
    @Override
    public Loader<Void> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<Void>(this) {
            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                progress.setVisibility(View.VISIBLE);
            }

            @Override
            public Void loadInBackground() {
                RefreshTask.refreshArticles(MainActivity.this);
                return null;
            }
        };
    }

    // when done, get db items and display via rv
    @Override
    public void onLoadFinished(Loader<Void> loader, Void data) {
        progress.setVisibility(View.GONE);
        db = new DBHelper(MainActivity.this).getReadableDatabase();
        cursor = DatabaseUtils.getAll(db);

        adapter = new MyAdapter(cursor,this);
        rv.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onLoaderReset(Loader<Void> loader) {

    }

    // when article is clicked, grab the url from db and direct to browser artcile
    @Override
    public void onItemClick(Cursor cursor, int clickedItemIndex) {
        cursor.moveToPosition(clickedItemIndex);
        String url = cursor.getString(cursor.getColumnIndex(Contract.TABLE_ARTICLES.COLUMN_NAME_URL));
        Log.d(TAG, String.format("Url %s", url));

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }
}
