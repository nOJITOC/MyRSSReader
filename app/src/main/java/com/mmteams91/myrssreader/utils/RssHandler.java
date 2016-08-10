package com.mmteams91.myrssreader.utils;

import com.mmteams91.myrssreader.data.models.RssLentItem;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

public class RssHandler extends DefaultHandler {
    private List<RssLentItem> RssLentItems;
    private RssLentItem currentRssLentItem;
    private StringBuilder builder;

    public List<RssLentItem> getRssLentItems() {
        return this.RssLentItems;
    }

    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        super.characters(ch, start, length);
        builder.append(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String name)
            throws SAXException {
        super.endElement(uri, localName, name);
        if (this.currentRssLentItem != null) {
            if (localName.equalsIgnoreCase("TITLE")) {
                currentRssLentItem.setTitle(builder.toString().replaceAll("[\n\t]", ""));
            } else if (localName.equalsIgnoreCase("LINK")) {
                currentRssLentItem.setLink(builder.toString());
            } else if (localName.equalsIgnoreCase("DESCRIPTION")) {
                currentRssLentItem.setDescription(builder.toString());
            } else if (localName.equalsIgnoreCase("PUBDATE")) {
                currentRssLentItem.setPubDate(builder.toString());
            } else if (localName.equalsIgnoreCase("ITEM")) {
                RssLentItems.add(currentRssLentItem);
            }
            builder.setLength(0);
        }
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
        RssLentItems = new ArrayList<RssLentItem>();
        builder = new StringBuilder();
    }

    @Override
    public void startElement(String uri, String localName, String name,
                             Attributes attributes) throws SAXException {
        super.startElement(uri, localName, name, attributes);
        if (localName.equalsIgnoreCase("ITEM")) {
            this.currentRssLentItem = new RssLentItem();
        }

        for (int i = 0; i < attributes.getLength(); i++) {
            if (attributes.getValue(i).contains(".jpg")) {
                if (currentRssLentItem != null)
                    currentRssLentItem.setImage(attributes.getValue(i));
            }
        }

    }
}
