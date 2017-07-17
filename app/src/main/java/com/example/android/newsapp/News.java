package com.example.android.newsapp;

import android.graphics.Bitmap;

/**
 * Created by Niamh on 16/07/2017.
 */

public class News {

    private Bitmap mImage;
    private String mArticleTitle;
    private String mArticleDescription;
    private String mUrl;

    public News(Bitmap image, String articleTitle, String articleDescription, String url){
        mImage=image;
        mArticleTitle=articleTitle;
        mArticleDescription=articleDescription;
        mUrl=url;
    }

    public Bitmap getmImage(){
        return mImage;
    }
    public String getmArticleTitle(){
        return mArticleTitle;
    }
    public String getmArticleDescription(){
        return mArticleDescription;
    }
    public String getUrl(){
        return mUrl;
    }
}
