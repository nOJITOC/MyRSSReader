package com.mmteams91.myrssreader.utils;


import com.mmteams91.myrssreader.data.models.RssLentItem;

import java.util.List;

public interface FeedParser {
    List<RssLentItem> parse();
}
