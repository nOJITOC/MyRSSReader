package com.mmteams91.myrssreader.utils;

import com.mmteams91.myrssreader.data.models.RssLentItem;

import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class SaxFeedParser extends BaseFeedParser {

    public SaxFeedParser(String feedUrl){
        super(feedUrl);
    }

    public List<RssLentItem> parse() {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            SAXParser parser = factory.newSAXParser();
            RssHandler handler = new RssHandler();
            parser.parse(this.getInputStream(), handler);
            return handler.getRssLentItems();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
