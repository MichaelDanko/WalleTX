package com.bitcoin.tracker.walletx.activity.summary;

import android.content.Intent;
import android.os.Bundle;

import com.bitcoin.tracker.walletx.model.Walletx;

import java.util.LinkedList;

/**
 * Summary for a single walletx
 */
public class SummarySingleActivity extends SummaryAbstractActivity {

    // Name of the group we are summarizing
    private String mWalletxName;

    //region ACTIVITY LIFECYCLE

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        mWalletxName = intent.getStringExtra("walletx_name");
        setActivityTitle();
        populateWalletxList();

        // Refresh the modules now that the wtx list is populated
        mTxSummaryModule.refreshPieChart();
        mSpendingSummaryModule.refreshModule();
    }

    protected void populateWalletxList() {
        wtxs = new LinkedList<>();
        wtxs.add(Walletx.getBy(mWalletxName));
    }

    protected void setActivityTitle() {
        getSupportActionBar().setTitle(mWalletxName);
    }

    protected void refreshUi() {

    }

    //endregion

}
