package com.example.daniel.newsapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.daniel.newsapp.utilites.NetworkUtils;

import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView mDataTextView;
    private ProgressBar progress;
    private EditText search;
    private TextView textView;
    private String defaultsource = "the-next-web";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progress = (ProgressBar) findViewById(R.id.progressBar);
        search = (EditText) findViewById(R.id.searchQuery);
        textView = (TextView) findViewById(R.id.news_data);
        mDataTextView = (TextView) findViewById(R.id.news_data);

        new FetchNewsTask().execute(defaultsource);
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
            String s = search.getText().toString();
            new FetchNewsTask().execute(s);
            mDataTextView.setText("");
            mDataTextView = (TextView) findViewById(R.id.news_data);
        }

        return true;
    }

    private class FetchNewsTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            if(strings.length == 0) {
                return null;
            }

            String newssource = strings[0];
            URL newsUrl = NetworkUtils.buildUrl(newssource);

            Log.d("TAG", "URL : " + newsUrl);

            try {
                return NetworkUtils.getResponseFromHttpUrl(newsUrl);
            }catch (Exception e){
                e.printStackTrace();
            }

            return "Not a valid source.";

        }

        @Override
        protected void onPostExecute(String newsData) {
            progress.setVisibility(View.GONE);
            if(newsData!=null){
                    mDataTextView.append(newsData + "\n\n\n");
                }
            }
        }

}