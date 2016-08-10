package com.mmteams91.myrssreader.data.managers;

import android.content.Context;

import com.mmteams91.myrssreader.data.models.DaoSession;
import com.mmteams91.myrssreader.data.models.Lents;
import com.mmteams91.myrssreader.utils.MyRssReaderApplication;

import java.util.ArrayList;
import java.util.List;






/**
 * Class-Singleton
 */
public class DataManager {

    private static DataManager INSTANCE = null;

    private Context mContext;

    private DaoSession mDaoSession;

    public DataManager() {
        this.mContext = MyRssReaderApplication.getContext();
        this.mDaoSession = MyRssReaderApplication.getDaoSession();
    }


    public DaoSession getDaoSession() {
        return mDaoSession;
    }


    public static DataManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DataManager();
        }
        return INSTANCE;

    }

    public Context getContex() {
        return mContext;
    }


    //============================Database===============
    public List<Lents> getLentListFromDb() {
        List<Lents> lents = new ArrayList<>();
        try {
            lents = mDaoSession.queryBuilder(Lents.class)
                    .build()
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lents;
    }
    //endregoion
}

