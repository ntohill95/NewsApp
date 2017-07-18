package com.example.android.newsapp;


import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.widget.TextView;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;


import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>>{
    private TextView mEmptyStateTextView;
    private NewsAdaptor mAdapter;
    private RecyclerView newsList;
    private static final String USGS_REQUEST_URL ="https://content.guardianapis.com/search?api-key=eb90884c-fd1b-4aad-8bd5-5a9bc6e9e51e";
    private static final int NEWS_LOADER_ID = 1;

    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        Uri baseUri = Uri.parse(USGS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {
        Log.i("NiamhTest","loader finished loading - Should have news!!!");
        View mLoadingBar = findViewById(R.id.loading_spinner);
        mLoadingBar.setVisibility(View.GONE);
        newsList.setVisibility(View.VISIBLE);
        mAdapter.setNewsList(new ArrayList<News>());
        if (news != null && !news.isEmpty()) {
            mAdapter.setNewsList(news);
            mAdapter.notifyDataSetChanged();
        } else {
            Log.i("NiamhTest","Seems that the news is Empty???");
            Log.i("NiamhTest","The news array -> " + news);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        mAdapter.setNewsList(new ArrayList<News>());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.newsList = (RecyclerView) findViewById(R.id.list);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        mAdapter = new NewsAdaptor();
        newsList.setAdapter(mAdapter);
        newsList.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.setNewsList(new ArrayList<News>());

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        final boolean isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();

        //If the device is connected to the network
        if (isConnected) {
            newsList.setVisibility(View.VISIBLE);
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        }
        //If the device is not connected to the network
        else {
            View loadingIndicator = findViewById(R.id.loading_spinner);
            loadingIndicator.setVisibility(View.GONE);
            newsList.setVisibility(View.GONE);
            mEmptyStateTextView.setVisibility(View.VISIBLE);
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }
    }
}
