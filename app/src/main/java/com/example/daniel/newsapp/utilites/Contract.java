package com.example.daniel.newsapp.utilites;

import android.provider.BaseColumns;

/**
 * Created by Daniel on 7/21/2017.
 */

public class Contract {

    // Declare our table data
    public static class TABLE_ARTICLES implements BaseColumns {
        public static final String TABLE_NAME = "articles";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_AUTHOR = "author";
        public static final String COLUMN_NAME_PUBLISHED_DATE = "published_date";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_THUMBURL = "thumb_url";
        public static final String COLUMN_NAME_URL = "url";
    }
}
