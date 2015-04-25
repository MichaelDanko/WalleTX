package com.bitcoin.tracker.walletx.activity.summary;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.bitcoin.tracker.walletx.R;

import com.bitcoin.tracker.walletx.api.SyncableInterface;
import com.bitcoin.tracker.walletx.activity.chart.ChartSpendingByCategoryActivity;
import com.bitcoin.tracker.walletx.activity.tx.TxsActivity;
import com.bitcoin.tracker.walletx.api.SyncDatabase;
import com.bitcoin.tracker.walletx.model.SupportedSummaryType;
import com.bitcoin.tracker.walletx.model.Walletx;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Walletx summary abstract super class.
 */
public abstract class SummaryAbstractActivity extends ActionBarActivity implements
        ModuleTxs.OnFragmentInteractionListener,
        ModuleSpending.OnFragmentInteractionListener,
        SyncableInterface {

    /**
     * The list of walletx's that we are going to summarize.
     * Set by child class.
     */
    protected List<Walletx> wtxs = new LinkedList<>();

    public List<Walletx> getWtxs() {
        return wtxs;
    }

    // Reference to activity
    static Activity mActivity;

    // displays when sync in progress
    private ProgressBar mSyncProgressBar;

    protected ModuleTxs mTxSummaryModule;
    protected ModuleSpending mSpendingSummaryModule;

    // Required implementation by child classes
    protected abstract void populateWalletxList();
    protected abstract void setActivityTitle();

    //region ACTIVITY LIFECYCLE

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summary_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mActivity = this;

        mTxSummaryModule = (ModuleTxs) getFragmentManager().findFragmentById(R.id.moduleTxs);
        mSpendingSummaryModule = (ModuleSpending) getFragmentManager().findFragmentById(R.id.moduleSpendingBreakdown);

        // setup sync progress spinner
        mSyncProgressBar = (ProgressBar) findViewById(R.id.syncProgressBar);
        mSyncProgressBar.getIndeterminateDrawable().setColorFilter(Color.GRAY, android.graphics.PorterDuff.Mode.MULTIPLY);
        mSyncProgressBar.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSpendingSummaryModule.refreshModule();
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
        if (item.getItemId() == R.id.action_sync) {
            new SyncDatabase(this);
            return true;
        } else if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    //endregion

    @Override
    public void onFragmentInteraction(SupportedSummaryType type){

        Intent intent;
        // The proper was to do this would be to make Walletx model parcelable so
        // we can pass the wtxs list to the new intent, but we're short on time
        // and the use of Active Android complicates things a little. For purposes of
        // course I'm using a little hack, passing ArrayList<String> containing
        // the names of each wtx in wtxs. The new intent can then re-query them
        // since there shouldn't be too many, and then get the tx list.
        // TODO Time permitting please DO replace this with proper method to passing parcelable
        ArrayList<String> wtxNames = new ArrayList<>(wtxs.size());
        for ( Walletx w : wtxs )
            wtxNames.add(w.name);

        switch (type) {
            case TRANSACTION_SUMMARY:
                intent = new Intent( this, TxsActivity.class );
                intent.putExtra("group_name", getSupportActionBar().getTitle().toString());
                intent.putStringArrayListExtra("wtx_names", wtxNames);
                startActivity( intent );
                break;
            case SPENDING_BY_CATEGORY:
                intent = new Intent( this, ChartSpendingByCategoryActivity.class );
                intent.putExtra("group_name", getSupportActionBar().getTitle().toString());
                // See above comment
                wtxNames = new ArrayList<>(wtxs.size());
                for ( Walletx w : wtxs )
                    wtxNames.add(w.name);
                intent.putStringArrayListExtra("wtx_names", wtxNames);
                startActivity( intent );
                break;
            default:
                break;
        }
    }

    //region SYNC

    public void startSyncRelatedUI() {
        // Rotate progress bar
        final ProgressBar pb = (ProgressBar) mActivity.findViewById(R.id.syncProgressBar);
        if ( mActivity != null && pb != null ) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    pb.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    public void stopSyncRelatedUI() {
        // stop progress bar
        final ProgressBar pb = (ProgressBar) mActivity.findViewById(R.id.syncProgressBar);
        if ( mActivity != null && pb != null ) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    pb.setVisibility(View.GONE);
                }
            });
        }

        mTxSummaryModule.refreshPieChart();
    }

    //endregion
}
