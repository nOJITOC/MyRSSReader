package com.mmteams91.myrssreader.data.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;


public class RssLentItem implements Comparable<RssLentItem>, Parcelable {
    private String title;
    private String description;
    private Date pubDate;
    private String image;
    private URL link;
    static SimpleDateFormat FORMATTER = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");

    public RssLentItem() {
    }
    public RssLentItem copy(){
        return new RssLentItem(this.title,description,pubDate,image,link);
    }
    public RssLentItem(String title, String description, Date pubDate, String image, URL link) {
        this.title = title;
        this.description = description;
        this.pubDate = pubDate;
        this.image = image;
        this.link = link;
    }

    public String getTitle() {
        return title;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPubDate() {
        return FORMATTER.format(this.pubDate);
    }

    public void setPubDate(String pubDate) {
        while (!pubDate.endsWith("00")) {
            pubDate += "0";
        }
        try {
//            this.pubDate = FORMATTER.parse(pubDate);
            this.pubDate = new Date(pubDate);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public URL getLink() {
        return link;
    }

    public void setLink(String link) {
        try {
            this.link = new URL(link);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int hashCode() {
        return pubDate.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return pubDate.equals(obj);
    }

    @Override
    public int compareTo(RssLentItem rssLentItem) {
        if (rssLentItem == null) return 1;
        // sort descending, most recent first
        return rssLentItem.pubDate.compareTo(pubDate);
    }

    protected RssLentItem(Parcel in) {
        title = in.readString();
        description = in.readString();
        long tmpPubDate = in.readLong();
        pubDate = tmpPubDate != -1 ? new Date(tmpPubDate) : null;
        image = in.readString();
        link = (URL) in.readValue(URL.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeLong(pubDate != null ? pubDate.getTime() : -1L);
        dest.writeString(image);
        dest.writeValue(link);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<RssLentItem> CREATOR = new Parcelable.Creator<RssLentItem>() {
        @Override
        public RssLentItem createFromParcel(Parcel in) {
            return new RssLentItem(in);
        }

        @Override
        public RssLentItem[] newArray(int size) {
            return new RssLentItem[size];
        }
    };
}