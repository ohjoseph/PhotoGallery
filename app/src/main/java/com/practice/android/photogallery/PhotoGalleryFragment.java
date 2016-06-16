package com.practice.android.photogallery;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Joseph on 6/16/16.
 */

public class PhotoGalleryFragment extends Fragment {
    private static final String TAG = "PhotoGalleryFragment";

    private RecyclerView mRecyclerView;
    private GridLayoutManager mGridLayoutManager;
    private List<GalleryItem> mItems = new ArrayList<>();

    private int mPageNumber;

    public static PhotoGalleryFragment newInstance() {
        return new PhotoGalleryFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        new FetchItemsTask().execute(mPageNumber);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_photo_gallery, parent, false);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.photo_gallery_recyclerView);
        mGridLayoutManager = new GridLayoutManager(getActivity(), 3);
        mRecyclerView.setLayoutManager(mGridLayoutManager);

        boolean loading;
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }

            @Override
            public void onScrollStateChanged(RecyclerView rv, int newState) {
                GalleryAdapter adapter = (GalleryAdapter) rv.getAdapter();
                int lastPosition = adapter.getLastPosition();

                GridLayoutManager layoutManager = (GridLayoutManager)rv.getLayoutManager();
                int loadBufferPosition = 1;

                if(lastPosition >= adapter.getItemCount() - layoutManager.getSpanCount()- loadBufferPosition){
                    new FetchItemsTask().execute(mPageNumber);
                }
            }
        });
        setupAdapter();

        return v;
    }

    private void setupAdapter() {
        if (isAdded()) {
            mRecyclerView.setAdapter(new GalleryAdapter(mItems));
            Log.d(TAG, "Adapter set up complete");
        }
    }

    private class FetchItemsTask extends AsyncTask<Integer, Void, List<GalleryItem>> {

        @Override
        protected List<GalleryItem> doInBackground(Integer... params) {
            return new FlickrFetcher().fetchItems(params[0]);
        }

        @Override
        protected void onPostExecute(List<GalleryItem> items) {
            if (mPageNumber < 1) {
                mItems = items;
                setupAdapter();
            } else {
                mItems.addAll(items);
                mRecyclerView.getAdapter().notifyDataSetChanged();
            }
            mPageNumber++;
        }
    }

    private class GalleryHolder extends RecyclerView.ViewHolder {
        private GalleryItem mGalleryItem;
        private TextView mTextView;

        public GalleryHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView;
        }

        public void bindGalleryItem(GalleryItem item) {
            mGalleryItem = item;
            mTextView.setText(item.getCaption());
        }
    }

    private class GalleryAdapter extends RecyclerView.Adapter<GalleryHolder> {
        private List<GalleryItem> mGalleryItems;
        private int mLastPosition;

        public GalleryAdapter(List<GalleryItem> items) {
            mGalleryItems = items;
        }

        public int getLastPosition() {
            return mLastPosition;
        }

        @Override
        public GalleryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            TextView tv = new TextView(getActivity());
            return new GalleryHolder(tv);
        }

        @Override
        public void onBindViewHolder(GalleryHolder holder, int position) {
            holder.bindGalleryItem(mGalleryItems.get(position));
            mLastPosition = position;
        }

        @Override
        public int getItemCount() {
            return mGalleryItems.size();
        }
    }

}
