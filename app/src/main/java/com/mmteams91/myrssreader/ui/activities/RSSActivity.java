package com.mmteams91.myrssreader.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mmteams91.myrssreader.R;
import com.mmteams91.myrssreader.data.models.RssLentItem;

public class RSSActivity extends AppCompatActivity {

    private TextView mDate, mTitle, mDescription, mLink;
    RssLentItem mNews;
    ImageView mImage;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rss);
        mNews = getIntent().getParcelableExtra("PARCELABLE_KEY");
        mDate = (TextView)findViewById(R.id.rss_date_txt);
        mTitle = (TextView)findViewById(R.id.rss_title_txt);
        mDescription = (TextView)findViewById(R.id.rss_description_txt);
        mLink = (TextView)findViewById(R.id.rss_link_txt);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setupToolbar();
        mDate.setText(mNews.getPubDate());
        mTitle.setText(mNews.getTitle());
        mDescription.setText(mNews.getDescription());
        mImage =(ImageView) findViewById(R.id.rss_image);
        Glide.with(this)
                .load(mNews.getImage())
                .error(R.drawable.user_bg)
                .placeholder(R.drawable.user_bg)
                .into(mImage);
        mLink.setText(mNews.getLink().toString());
        mLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browse = new Intent(Intent.ACTION_DEFAULT, Uri.parse(mLink.getText().toString()));
                startActivity(browse);
            }
        });
    }
    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}
