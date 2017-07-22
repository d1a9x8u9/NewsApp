package com.example.daniel.newsapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.daniel.newsapp.utilites.DBHelper;
import com.example.daniel.newsapp.utilites.NetworkUtils;
import com.example.daniel.newsapp.utilites.NewsItem;
import com.example.daniel.newsapp.utilites.DatabaseUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Daniel on 7/21/2017.
 */

class RefreshTask {
    // When called, current db is cleared. Api is called to fetch new articles and then bulk inserted into our db. close db at the end of the method.
    public static void refreshArticles(Context context) {
        ArrayList<NewsItem> result;
        URL url = NetworkUtils.buildUrl();

        SQLiteDatabase db = new DBHelper(context).getWritableDatabase();

        try {
            DatabaseUtils.deleteAll(db);
            String json = NetworkUtils.getResponseFromHttpUrl(url);
            result = NetworkUtils.parseJSON(json);
            DatabaseUtils.bulkInsert(db, result);

        } catch (IOException e) {
            e.printStackTrace();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        db.close();
    }
}
