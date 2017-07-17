package com.example.android.newsapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Niamh on 16/07/2017.
 */

public class NewsAdaptor extends RecyclerView.Adapter<NewsAdaptor.NewsViewHolder> {
    private static final String LOCATION_SEPARATOR = " of ";
    private List<News> newsList;

    public NewsAdaptor() {}

    public void setNewsList(List<News> list) {
        if(list.size() == 0) {
            Log.i("NiamhTest","The list is EMPTY when setting the RecyclerView list");
        }
        Log.i("NiamhTest","Setting the news list. The news list is -> " + list);
        this.newsList = list;
        this.notifyDataSetChanged();
    }

    @Override
    public NewsAdaptor.NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new NewsViewHolder(itemView);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return this.newsList.size();
    }

    public News getItemAt(int position) {
        return this.newsList.get(position);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        holder.setNewsItem(getItemAt(position));
        holder.imageView.setImageBitmap(holder.newsItem.getmImage());
        holder.newsTitle.setText(holder.newsItem.getmArticleTitle());
        holder.newsDescription.setText(holder.newsItem.getmArticleDescription());
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private News newsItem;
        private final Context context;
        private ImageView imageView;
        private TextView newsTitle;
        private TextView newsDescription;

        public NewsViewHolder(View itemView) {
            super(itemView);
            Log.i("NiamhTest","Init a NewsViewsHolder");
            context = itemView.getContext();
            imageView = (ImageView) itemView.findViewById(R.id.news_image);
            newsTitle = (TextView) itemView.findViewById(R.id.article_heading);
            newsDescription = (TextView) itemView.findViewById(R.id.article_description);
            itemView.setOnClickListener(this);
        }

        public void setNewsItem(News item) {
            this.newsItem = item;
        }

        @Override
        public void onClick(View v) {
            Log.i("onClickTest","Tapped view with news title -> " + this.newsItem.getmArticleTitle());
            Uri newsUri = Uri.parse(this.newsItem.getUrl());
            Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);
            this.context.startActivity(websiteIntent);
        }
    }
}
