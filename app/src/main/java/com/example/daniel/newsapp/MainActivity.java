package com.example.daniel.newsapp;

import android.content.Intent;
import android.net.Network;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.daniel.newsapp.utilites.NetworkUtils;
import com.example.daniel.newsapp.utilites.Repository;

import org.json.JSONException;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progress;
    private EditText search;
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progress = (ProgressBar) findViewById(R.id.progressBar);
        search = (EditText) findViewById(R.id.searchQuery);
        rv = (RecyclerView) findViewById(R.id.recyclerView);

        rv.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemNumber = item.getItemId();

        if (itemNumber == R.id.search) {
                FetchNewsTask task = new FetchNewsTask();
                task.execute();
        }
        return true;
    }

    private class FetchNewsTask extends AsyncTask<URL, Void, ArrayList<Repository>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<Repository> doInBackground(URL... strings) {

            ArrayList<Repository> result = null;

            URL url = NetworkUtils.buildUrl();
            Log.d("MainActivity_url", "url: " + url.toString());

            try {
                String json =  NetworkUtils.getResponseFromHttpUrl(url);
                result = NetworkUtils.parseJSON(json);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e){
                e.printStackTrace();
            }

            return result;

        }

        @Override
        protected void onPostExecute(final ArrayList<Repository> newsData) {
            super.onPostExecute(newsData);
            progress.setVisibility(View.GONE);
            if(newsData!=null){
                    NewsAdapter adapter = new NewsAdapter(newsData, new NewsAdapter.ItemClickListener() {
                        @Override
                        public void onItemClick(int clickedItemIndex) {
                            String url = newsData.get(clickedItemIndex).getUrl();
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            startActivity(browserIntent);
                            Log.d("MainActivity", String.format("Url %s",url));
                        }
                    });
                    rv.setAdapter(adapter);
                }
            }


    }

}
