package com.mmteams91.myrssreader.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;


import com.mmteams91.myrssreader.data.models.RssLentItem;

import java.util.List;


public class RetainedFragment extends Fragment {
    // data object we want to retain
    private List<RssLentItem> mRssLentItems;

    // this method is only called once for this fragment
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // retain this fragment
        setRetainInstance(true);
    }

    public void setData(List<RssLentItem> data) {
        this.mRssLentItems = data;
    }


    public List<RssLentItem> getData() {
        return mRssLentItems;
    }
}
