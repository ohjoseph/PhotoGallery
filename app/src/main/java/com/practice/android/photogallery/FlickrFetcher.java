package com.practice.android.photogallery;

import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joseph on 6/16/16.
 */

public class FlickrFetcher {

    private static final String TAG = "FlickrFetcher";
    private static final String API_KEY = "6cf0b2bd27f9add5f1c8104aaa4e7cd7";

    public byte[] getUrlBytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();

            // Check for connection error
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() + ": with " + urlSpec);
            }

            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    public String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    public List<GalleryItem> fetchItems(int page) {
        List<GalleryItem> items = new ArrayList<>();

        try {
            String url = Uri.parse("https://api.flickr.com/services/rest")
                    .buildUpon()
                    .appendQueryParameter("method", "flickr.photos.getRecent")
                    .appendQueryParameter("api_key", API_KEY)
                    .appendQueryParameter("format", "json")
                    .appendQueryParameter("nojsoncallback", "1")
                    .appendQueryParameter("extras", "url_s")
                    .appendQueryParameter("page", page + "")
                    .build().toString();
            String jsonString = getUrlString(url);
            JSONObject jsonBody = new JSONObject(jsonString);
            //parseItems(items, jsonBody);
            GsonParse(items, jsonBody);
            Log.d(TAG, "Successfully fetched items: " + url);
        } catch (IOException e) {
            Log.e(TAG, "Failed to fetch items: " + e);
        } catch (JSONException e) {
            Log.e(TAG, "Failed to parse JSON: " + e);
        }

        return items;
    }

    private void parseItems(List<GalleryItem> items, JSONObject jsonBody)
            throws IOException, JSONException {

        JSONObject photosJSON = jsonBody.getJSONObject("photos");
        JSONArray photosJSONArray = photosJSON.getJSONArray("photo");

        for (int i = 0; i < photosJSONArray.length(); i++) {
            JSONObject photoJsonObject = photosJSONArray.getJSONObject(i);

            GalleryItem item = new GalleryItem();
            item.setId(photoJsonObject.getString("id"));
            item.setCaption(photoJsonObject.getString("title"));

            if (!photoJsonObject.has("url_s")) {
                continue;
            }

            item.setUrl(photoJsonObject.getString("url_s"));
            items.add(item);
        }
    }

    private void GsonParse(List<GalleryItem> items, JSONObject body)
            throws IOException, JSONException {

        Gson gson = new Gson();
        FlickrPhoto[] photos = gson.fromJson(body.getJSONObject("photos")
                .getJSONArray("photo").toString(), FlickrPhoto[].class);

        for (FlickrPhoto photo : photos) {
            if (photo.getUrl_s() != null) {
                items.add(new GalleryItem(photo));
            }
        }
    }
}
