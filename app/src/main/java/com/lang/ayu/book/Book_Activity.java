package com.lang.ayu.book;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.app.LoaderManager.LoaderCallbacks;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Book_Activity extends AppCompatActivity  implements LoaderCallbacks<List<Book>> {


    private BookAdapter adapter;
    private  TextView mEmptyState ;
    private  TextView mConTextView;
    private static final int BOOK_ID = 1;
    static String find;
    private static final String BOOK_REQUEST_URL = "https://www.googleapis.com/books/v1/volumes?q=";
   private String BOOKS ="https://www.googleapis.com/books/v1/volumes?q="+find;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_activity);
        ListView bookview = (ListView)findViewById(R.id.list);

        adapter = new BookAdapter(this,new ArrayList<Book>());
        bookview.setAdapter(adapter);
        mConTextView = (TextView)findViewById(R.id.view) ;
        mEmptyState = (TextView) findViewById(R.id.empty_view);
        bookview.setEmptyView(mEmptyState);
        bookview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Book currentBook = adapter.getItem(i);
                Uri earthquakeUri = Uri.parse(currentBook.getInfo());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);
                startActivity(websiteIntent);
            }
        });
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network_Info = connMgr.getActiveNetworkInfo();
        if (network_Info != null && network_Info.isConnected()) {
            LoaderManager loader_Manager = getLoaderManager();
            loader_Manager.initLoader(BOOK_ID, null, this);
        } else {
            View loading_Indicator = findViewById(R.id.loading);
            loading_Indicator.setVisibility(View.GONE);
            mConTextView.setText(R.string.no_internet);
        }
        Button serchBTn = (Button) findViewById(R.id.search);

        serchBTn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editText =(EditText)findViewById(R.id.text1);
                find = editText.getText().toString();
                BOOKS = BOOK_REQUEST_URL + find + "&maxResults=10";
                restart_Loader();

                ConnectivityManager connectMgr = (ConnectivityManager)
                        getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {

                } else {
                    View load_Indicator = findViewById(R.id.loading);
                    load_Indicator.setVisibility(View.GONE);
                    mConTextView.setText(R.string.no_internet);
                }
            }
        });
        android.app.LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(BOOK_ID,null, this);
    }

    public void restart_Loader() {
        LoaderManager load_Manager = getLoaderManager();
        load_Manager.restartLoader(BOOK_ID, null, this);
    }
    @NonNull
    @Override
    public Loader<List<Book>> onCreateLoader(int i, Bundle bundle) {
        BookLoader bookload = new BookLoader(this,BOOKS);
        return bookload;
    }

    @Override
    public void onLoadFinished(android.content.Loader<List<Book>> loader, List<Book> books) {
        adapter.clear();
        mEmptyState.setText(R.string.no);
        View loadingIndicator = findViewById(R.id.loading);
        loadingIndicator.setVisibility(View.GONE);

        if (books != null && !books.isEmpty()) {
            adapter.addAll(books);
        }
    }

    @Override
    public void onLoaderReset(android.content.Loader<List<Book>> loader) {
        adapter.clear();
    }

    private class BookAsync extends AsyncTask<String, Void, List<Book>> {

        @Override
        protected List<Book> doInBackground(String... urls) {
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }
            List<Book> result = QueryUtils.fetchBookData(urls[0]);
            return result;
        }

        @Override
        protected void onPostExecute(List<Book> data) {
            adapter.clear();

            if (data != null && !data.isEmpty()) {
                adapter.addAll(data);
            }
        }
    }
}
