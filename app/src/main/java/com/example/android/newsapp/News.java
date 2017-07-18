package com.example.android.newsapp;

import android.graphics.Bitmap;

/**
 * Created by Niamh on 16/07/2017.
 */

public class News {

    private String mArticleTitle;
    private String mUrl;
    private String mSection;

    public News(String section, String articleTitle, String url){
        mSection=section;
        mArticleTitle=articleTitle;
        mUrl=url;
    }

    public String getSection(){
        return mSection;
    }
    public String getmArticleTitle(){
        return mArticleTitle;
    }

    public String getUrl(){
        return mUrl;
    }
}
