package com.example.daniel.newsapp.utilites;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Daniel on 6/16/2017.
 */

public final class NetworkUtils {
    public static final String TAG = NetworkUtils.class.getSimpleName();
    public static final String STATIC_NEWS_URL =
            "https://newsapi.org/v1/articles";
    public static final String NEWS_BASE_URL = STATIC_NEWS_URL;
    
    // TODO Insert API key into 'apiKey' below
    public static final String apikey = "";
    public static final String sort = "latest";

    public static String PARAM_API_KEY = "apiKey";
    public static String PARAM_SORT =  "sortBy";
    public static String PARAM_SOURCE = "source";


    public static URL buildUrl(String source){
        Uri buildUri = Uri.parse(NEWS_BASE_URL).buildUpon()
                .appendQueryParameter(PARAM_SOURCE, source)
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
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
