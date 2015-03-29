package com.bitcoin.tracker.walletx.activity.walletxSummary;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.bitcoin.tracker.walletx.R;

import com.bitcoin.tracker.walletx.activity.walletxSummary.module.WalletxSummaryModuleTxs;
import com.bitcoin.tracker.walletx.activity.walletxTxs.TxsActivity;
import com.bitcoin.tracker.walletx.model.SupportedSummaryType;
import com.bitcoin.tracker.walletx.model.Walletx;

import java.util.ArrayList;
import java.util.List;

/**
 * Walletx summary abstract super class.
 */
public abstract class WalletxSummaryAbstractActivity extends ActionBarActivity
        implements WalletxSummaryModuleTxs.OnFragmentInteractionListener {

    /**
     * The list of walletx's that we are going to summarize.
     * Set by child class.
     */
    protected List<Walletx> wtxs;

    public List<Walletx> getWtxs() {
        return wtxs;
    }

    // Required implementation by child classes
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

    @Override
    public void onFragmentInteraction(SupportedSummaryType type){
        switch (type) {
            case TRANSACTION_SUMMARY:
                Intent intent = new Intent( this, TxsActivity.class );
                intent.putExtra("group_name", getSupportActionBar().getTitle().toString());
                // The proper was to do this would be to make Walletx model parcelable so
                // we can pass the wtxs list to the new intent, but we're short on time
                // and the use of Active Android complicates things a little. For purposes of
                // course I'm using a little hack, passing ArrayList<String> containing
                // the names of each wtx in wtxs. The new intent can then re-query them
                // since there shouldn't be too many, and then get the tx list.
                // TODO Time permitting please DO replace this with proper method to passing parcelable
                ArrayList<String> wtxNames = new ArrayList<>(wtxs.size());
                for ( Walletx w : wtxs ) {
                    wtxNames.add(w.name);
                }
                intent.putStringArrayListExtra("wtx_names", wtxNames);
                startActivity(intent);
        }
    }
}
