package com.mmteams91.myrssreader.ui.activities;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.mmteams91.myrssreader.R;
import com.mmteams91.myrssreader.data.managers.DataManager;
import com.mmteams91.myrssreader.data.models.Lents;
import com.mmteams91.myrssreader.data.models.LentsDao;
import com.mmteams91.myrssreader.data.models.RssLentItem;
import com.mmteams91.myrssreader.data.operation.RssParseOperation;
import com.mmteams91.myrssreader.ui.adapters.RssLentAdapter;
import com.mmteams91.myrssreader.ui.fragments.RetainedFragment;
import com.mmteams91.myrssreader.utils.eventbus.ChargingEvent;
import com.redmadrobot.chronos.ChronosConnector;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.greenrobot.event.EventBus;

public class MainActivity extends AppCompatActivity {

    private static final String RSS = "rss";
    private static final String TAG = "TAG";
    private static final String EDIT_MODE_KEY = "EDIT_MODE_KEY";
    private static final long RUN_DELAY = 1000l;
    private static final String PARCELABLE_KEY = "PARCELABLE_KEY";
    RecyclerView mRecyclerView;
    Toolbar mToolbar;
    FloatingActionButton mFab;

    EventBus mBus;
    RetainedFragment mRetainedFragment;
    List<Lents> mRssLents = new ArrayList<>();
    private ChronosConnector mConnector;
    private boolean mCurrentEditMode = false;
    private CoordinatorLayout mCoordinatorLayout;

    private DataManager mDataManager;
    private LentsDao mLentsDao;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBus = EventBus.getDefault();
        mBus.register(this);
        mConnector = new ChronosConnector();
        mConnector.onCreate(this, savedInstanceState);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setupToolbar();

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        setupSwipe();

        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_coordinator_container);
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentEditMode = !mCurrentEditMode;
                changeEditMode();

            }
        });
        mDataManager = DataManager.getInstance();
        mLentsDao = mDataManager.getDaoSession().getLentsDao();
        mRecyclerView = (RecyclerView) findViewById(R.id.rss_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        FragmentManager fm = getFragmentManager();

        mRetainedFragment = (RetainedFragment) fm.findFragmentByTag("news");
        if (mRetainedFragment == null) {
            mRetainedFragment = new RetainedFragment();
            mRetainedFragment.setData(new ArrayList<RssLentItem>());
            fm.beginTransaction().add(mRetainedFragment, "news").commit();
            loadRssLentsFromDb();


        } else
            mBus.post(new ChargingEvent(RSS));


        if (savedInstanceState == null)

        {
        } else

        {
            mCurrentEditMode = savedInstanceState.getBoolean(EDIT_MODE_KEY, false);
            changeEditMode();
        }


    }

    private void changeEditMode() {
        TextInputLayout til = (TextInputLayout) findViewById(R.id.rss_til);

        if (mCurrentEditMode) {
            til.setVisibility(View.VISIBLE);
            til.requestFocus();
            mFab.setImageResource(R.drawable.ic_done_white_24dp);
        } else {
            mFab.setImageResource(R.drawable.ic_add_white_24dp);

            EditText et = (EditText) til.getChildAt(0);
            String rssLent = et.getText().toString().trim().toLowerCase();
            et.setText("");
            boolean cond = true;
            for (Lents lent : mRssLents) {
                if (lent.getRss().equals(rssLent)) {
                    cond = false;
                    showSnackbar(getString(R.string.lent_already_exist));
                }
            }

            if (cond && !rssLent.isEmpty()) {
                mRssLents.add(new Lents(rssLent));
                mConnector.runOperation(new RssParseOperation(rssLent), false);
            }
            til.setVisibility(View.GONE);
        }
    }

    public void showSnackbar(String message) {
        Snackbar.make(mCoordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    private void showProgressDialog() {
        pd = ProgressDialog.show(MainActivity.this, getString(R.string.progress_dialog_up), getString(R.string.progress_dialog_mid), true, false);

    }

    private void hideProgressDialog() {

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (pd != null) pd.hide();
            }
        }, RUN_DELAY);
    }

    private void loadRssLentsFromDb() {
        mRssLents = mDataManager.getLentListFromDb();
        if (mRssLents.size() == 0) {
            showSnackbar(getString(R.string.empty_lent));

        }else {
            showProgressDialog();
        }

        mRetainedFragment.setData(new ArrayList<RssLentItem>());
        for (Lents rssLent : mRssLents) {
            mConnector.runOperation(new RssParseOperation(rssLent.getRss()), false);
        }
    }

    private void setupSwipe() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {

                loadRssLentsFromDb();
                mSwipeRefreshLayout.setRefreshing(false);


            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mConnector.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(EDIT_MODE_KEY, mCurrentEditMode);
        mConnector.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mConnector.onPause();
        mBus.unregister(this);
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);

    }


    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEvent(ChargingEvent event) {
        if (event.message == RSS) {
            RssLentAdapter.OnItemClickListener listener = new RssLentAdapter.OnItemClickListener() {
                @Override
                public void onItemClicked(int position) {
                    Intent profileIntent = new Intent(MainActivity.this, RSSActivity.class);
                    profileIntent.putExtra(PARCELABLE_KEY, mRetainedFragment.getData().get(position));
                    startActivity(profileIntent);
                }
            };
            RssLentAdapter adapter = new RssLentAdapter(mRetainedFragment.getData(),listener);
            mRecyclerView.setAdapter(adapter);
        }
    }

    //_________________________Chronos region__________________________
    public void onOperationFinished(final RssParseOperation.Result result) {
        Log.d(TAG, "onOperationFinished: ");
        hideProgressDialog();
        if (result.isSuccessful()) {
            mRetainedFragment.getData().addAll(result.getOutput());
            Collections.sort(mRetainedFragment.getData());
            mBus.post(new ChargingEvent(RSS));
        } else {
            showSnackbar(getString(R.string.lent_not_added));

            mLentsDao.delete(mRssLents.remove(mRssLents.size() - 1));
        }
    }
}
