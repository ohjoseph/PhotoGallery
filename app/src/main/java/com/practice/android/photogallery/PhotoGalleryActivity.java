package com.practice.android.photogallery;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class PhotoGalleryActivity extends SingleFragmentActivity {

    @Override
    protected PhotoGalleryFragment createFragment() {
        return PhotoGalleryFragment.newInstance();
    }
}
