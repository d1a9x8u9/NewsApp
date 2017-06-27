package com.example.daniel.newsapp.utilites;

import android.net.Uri;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Daniel on 6/16/2017.
 */

public final class NetworkUtils {
    public static final String TAG = NetworkUtils.class.getSimpleName();
    public static final String STATIC_NEWS_URL =
            "https://newsapi.org/v1/articles";
    public static final String NEWS_BASE_URL = STATIC_NEWS_URL;

    // Insert your key into 'apiKey'
    public static final String apikey = "6e6d27fa70c44a0da70427d2682402d6";
    public static final String sort = "latest";
    public static final String defaultsource = "buzzfeed";

    public static String PARAM_API_KEY = "apiKey";
    public static String PARAM_SORT =  "sortBy";
    public static String PARAM_SOURCE = "source";


    public static URL buildUrl(){
        Uri buildUri = Uri.parse(NEWS_BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_SOURCE, defaultsource)
                .appendQueryParameter(PARAM_SORT, sort)
                .appendQueryParameter(PARAM_API_KEY, apikey)
                .build();

        URL url = null;
        try{
            url = new URL(buildUri.toString());
        } catch (MalformedURLException e){
            e.printStackTrace();
        }
        Log.v(TAG, "Built URI " + url);
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                Log.d(TAG, "Response from HTTP");
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static ArrayList<Repository> parseJSON(String json) throws JSONException{
        ArrayList<Repository> result = new ArrayList<>();
        JSONObject main = new JSONObject(json);
        JSONArray articles = main.getJSONArray("articles");

        for(int i = 0; i < articles.length(); i++){
            JSONObject article = articles.getJSONObject(i);
            String author = article.getString("author");
            String title =  article.getString("title");
            String description = article.getString("description");
            String url_string = article.getString("url");
            String urlToImage = article.getString("urlToImage");
            String publishedAt = article.getString("publishedAt");
            Repository repo = new Repository(author, title, description, url_string, urlToImage, publishedAt);
            result.add(repo);

            Log.d(TAG, "Added articles to Repo array");

        }
        return result;
    }

}
