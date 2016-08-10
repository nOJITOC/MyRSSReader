package com.mmteams91.myrssreader.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.facebook.stetho.Stetho;
import com.mmteams91.myrssreader.data.models.DaoMaster;
import com.mmteams91.myrssreader.data.models.DaoSession;

import org.greenrobot.greendao.database.Database;

public class MyRssReaderApplication extends Application {

    private static Context sContext;
    private static DaoSession sDaoSession;

    public static Context getContext() {
        return sContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "rsslents = db");
        Database db = helper.getWritableDb();
        sDaoSession = new DaoMaster(db).newSession();

        Stetho.initializeWithDefaults(this);

    }

    public static DaoSession getDaoSession() {
        return sDaoSession;
    }

}