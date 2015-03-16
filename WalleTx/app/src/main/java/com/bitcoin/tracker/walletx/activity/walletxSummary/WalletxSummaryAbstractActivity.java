package com.bitcoin.tracker.walletx.activity.walletxSummary;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;

import com.bitcoin.tracker.walletx.R;

import com.bitcoin.tracker.walletx.model.Walletx;

import java.util.List;

/**
 * Walletx summary abstract super class.
 * Define the summary modules that will be displayed
 * for group of walletxs to summarize.
 */
public abstract class WalletxSummaryAbstractActivity extends ActionBarActivity {

    /**
     * The list of walletx's that we are going to summarize.
     * Set by child class.
     */
    protected List<Walletx> wtxs;

    /**
     * Require implementation by child classes
     */
    protected abstract void populateWalletxList();
    protected abstract void setActivityTitle();

    //region ACTIVITY LIFECYCLE

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walletx_summary);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    //endregion
    //region OPTIONS MENU

    /**
     * Display the global options menu.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.global, menu);
        return true;
    }

    /**
     * Home button closes the activity.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    //endregion

}
