package com.lang.ayu.book;

import android.content.Context;
import android.content.AsyncTaskLoader;

import java.util.List;

public class BookLoader extends AsyncTaskLoader {

    private static final String LOG_TAG = BookLoader.class.getName();

    private String mUrl;


    public BookLoader(Context context , String url) {
        super(context);
        mUrl =url;
    }

    protected void onStartLoading()
    {
        forceLoad();
    }

    @Override
    public Object loadInBackground() {
        if(mUrl == null)
             return null;

        List<Book> books1 = QueryUtils.fetchBookData(mUrl);
            return books1;
    }
}
