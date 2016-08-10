package com.mmteams91.myrssreader.ui.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mmteams91.myrssreader.R;
import com.mmteams91.myrssreader.data.models.RssLentItem;

import java.util.List;


public class RssLentAdapter extends RecyclerView.Adapter<RssLentAdapter.RssItemHolder> {
    private static final String TAG = "RSS adapter";
    private List<RssLentItem> mRssLent;
    private Context mContext;
    OnItemClickListener mListener;
    public interface OnItemClickListener {
        public void onItemClicked(int position);
    }

    public RssLentAdapter(List<RssLentItem> rssLent, OnItemClickListener listener) {
        mListener =listener;
        mRssLent = rssLent;
    }

    @Override
    public RssItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View convertView = LayoutInflater.from(mContext).inflate(R.layout.item_list_rss, parent, false);
        return new RssItemHolder(convertView);
    }

    @Override
    public void onBindViewHolder(RssItemHolder holder, final int position) {
        final RssLentItem rssItem = mRssLent.get(position);
        String rssImage;
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onItemClicked(position);
            }
        });
        if (rssItem.getImage() == null) {
            Log.e(TAG, "onBindViewHolder: RSSLent " + position + "do not have image");
            rssImage = "null";
        } else {
            rssImage = rssItem.getImage();
        }
        Glide.with(mContext)
                .load(rssImage)
                .error(holder.mDummy)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(holder.mDummy)
                .centerCrop()
                .fitCenter()
                .into(holder.mRssItemImage);
        holder.mTitle.setText(rssItem.getTitle());
        holder.mDate.setText(rssItem.getPubDate().toString());

    }

    @Override
    public int getItemCount() {
        return mRssLent.size();
    }

    public class RssItemHolder extends RecyclerView.ViewHolder{
        TextView mTitle;
        TextView mDate;
        ImageView mRssItemImage;
        Drawable mDummy;
        View mView;

        public RssItemHolder(View convertView) {
            super(convertView);
            mView = convertView;
            mRssItemImage = (ImageView) convertView.findViewById(R.id.rss_image);
            mTitle = (TextView) convertView.findViewById(R.id.rss_title_txt);
            mDate = (TextView) convertView.findViewById(R.id.rss_date_txt);
            mDummy = mRssItemImage.getContext().getResources().getDrawable(R.drawable.user_bg);

        }

    }
}
