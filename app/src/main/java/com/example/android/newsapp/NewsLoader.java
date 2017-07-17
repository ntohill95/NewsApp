package com.example.android.newsapp;
import android.content.AsyncTaskLoader;
import android.content.Context;
import java.util.List;

/**
 * Created by Niamh on 16/07/2017.
 */

public class NewsLoader extends AsyncTaskLoader<List<News>>  {
    private static final String LOG_TAG = NewsLoader.class.getName();
    private String mUrl;
    public NewsLoader(Context context, String url) {
        super(context);
        mUrl =url;
    }
    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<News> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of books.
        List<News> news = NewsUtils.fetchNewsData(mUrl);
        return news;
    }
}
