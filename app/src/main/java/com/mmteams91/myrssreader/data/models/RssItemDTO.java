package com.mmteams91.myrssreader.data.models;




public class RssItemDTO {
    private String title;
    private String description;
    private String pubDate;
    private String image;
    private String link;

    public RssItemDTO(RssLentItem item) {
        this.title = item.getTitle();
        this.description = item.getDescription();
        this.pubDate = item.getPubDate().toString();
        this.image = item.getImage();
        this.link = item.getLink().toString();
    }
}
