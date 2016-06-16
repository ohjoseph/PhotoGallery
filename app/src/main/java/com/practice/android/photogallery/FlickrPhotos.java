package com.practice.android.photogallery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joseph on 6/16/16.
 */

public class FlickrPhotos {
    private int page;
    private int pages;
    private int perpage;
    private int total;
    private List<FlickrPhoto> photo = new ArrayList<>();

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getPerpage() {
        return perpage;
    }

    public void setPerpage(int perpage) {
        this.perpage = perpage;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<FlickrPhoto> getPhoto() {
        return photo;
    }

    public void setPhoto(List<FlickrPhoto> photo) {
        this.photo = photo;
    }
}
