package com.practice.android.photogallery;

/**
 * Created by Joseph on 6/16/16.
 */

public class GalleryItem {
    private String mCaption;
    private String mId;
    private String mUrl;

    public GalleryItem(FlickrPhoto photo) {
        mCaption = photo.getTitle();
        mId = photo.getId();
        mUrl = photo.getUrl_s();
    }

    public GalleryItem() {
    }

    @Override
    public String toString() {
        return mCaption;
    }

    public String getCaption() {
        return mCaption;
    }

    public void setCaption(String caption) {
        mCaption = caption;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }
}
