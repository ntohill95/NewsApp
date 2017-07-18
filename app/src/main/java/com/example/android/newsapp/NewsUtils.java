package com.example.android.newsapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.bitmap;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

/**
 * Created by Niamh on 16/07/2017.
 */

public class NewsUtils {

    public static final String LOG_TAG = NewsUtils.class.getSimpleName();

    private NewsUtils() {
    }

    public static List<News> fetchNewsData(String requestURL) {
        URL url = createUrl(requestURL);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        List<News> news = extractFeaturesFromJson(jsonResponse);
        return news;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    //not used in this project but worked in my original app using newsapi.com where i got to experiment with downloading an image from a url
    public static byte[] getBitmapFromURL(String urlString) {
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(createUrl(urlString).openStream());
            if (bitmap == null) {
                Log.i("NiamhTest", "THe bitmap is null");
                return null;
            }
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            Log.i("NiamhTest", "Got exception for getting image bitmap. UrlString -> " + urlString);
            e.printStackTrace();
            return null;
        }
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the news JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    public static List<News> extractFeaturesFromJson(String newsJSON) {
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }
        List<News> news = new ArrayList<>();
        try {
            //convert from string to json
            JSONObject obj = new JSONObject(newsJSON);
            Log.i("NiamhTest", "This is the JSON -> " + obj.toString());
            //extract "items" jsonarray
            if (obj.has("response")) {
                JSONObject newsObject = obj.getJSONObject("response");
                if (newsObject.has("results")) {
                    JSONArray results = newsObject.getJSONArray("results");
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject currentNewsItem = results.getJSONObject(i);
                        //extract author, title, url
                        String title = currentNewsItem.getString("webTitle");
                        String section = currentNewsItem.getString("sectionName");
                        //String description = results.getString("description");
                        String url = currentNewsItem.getString("webUrl");
                        //byte[] imageData = getBitmapFromURL(currentNewsItem.getString("urlToImage"));
                        //Bitmap imageBitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
                        //create earthquake object with location mag time ..
                        News news1 = new News(section, title, url);
                        news.add(news1);

                    }
                }
            } else {
                Log.i("NiamhTest", "Did not find articles");
            }
        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the news JSON results", e);
        }

        // Return the list of books
        if (news.size() > 0) {
            Log.i("NiamhTest", "The array we have has items in it");
        }
        return news;
    }
}
