package com.mmteams91.myrssreader.data.operation;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.mmteams91.myrssreader.data.managers.DataManager;
import com.mmteams91.myrssreader.data.models.Lents;
import com.mmteams91.myrssreader.data.models.LentsDao;
import com.mmteams91.myrssreader.data.models.RssLentItem;
import com.mmteams91.myrssreader.utils.MyRssReaderApplication;
import com.mmteams91.myrssreader.utils.SaxFeedParser;
import com.redmadrobot.chronos.ChronosOperation;
import com.redmadrobot.chronos.ChronosOperationResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Иван on 09.08.2016.
 */
public class RssParseOperation extends ChronosOperation<List<RssLentItem>> {

    String mUri;
    List<RssLentItem> mLentItems = new ArrayList<>();

    public RssParseOperation(String uri) {
        this.mUri = uri;
    }

    @Nullable
    @Override
    public List<RssLentItem> run() {
        DataManager manager = DataManager.getInstance();
        SaxFeedParser parser = new SaxFeedParser(mUri);
        mLentItems.addAll(parser.parse());
        Collections.sort(mLentItems);
        LentsDao lentsDao = MyRssReaderApplication.getDaoSession().getLentsDao();
        lentsDao.insertOrReplace(new Lents(mUri));
        return mLentItems;
    }

    @NonNull
    @Override
    public Class<? extends ChronosOperationResult<List<RssLentItem>>> getResultClass() {
        return Result.class;
    }

    public static final class Result extends ChronosOperationResult<List<RssLentItem>> {

    }
}
