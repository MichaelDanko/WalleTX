package com.bitcoin.tracker.walletx.activity.summary;

import android.os.Bundle;

import com.bitcoin.tracker.walletx.activity.SharedData;
import com.bitcoin.tracker.walletx.model.Walletx;

/**
 * Summary for a single walletx
 */
public class SummarySingleActivity extends SummaryAbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Walletx wtx = SharedData.WTXS_TO_SUMMARIZE.get(0);
        setActivityTitle(wtx.name);
        // Refresh the modules now that the wtx list is populated
        refreshUi();
    }

    @Override
    protected void setActivityTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    protected void refreshUi() {
        mTxSummaryModule.refreshModule();
        mSpendingSummaryModule.refreshModule();
    }

}
