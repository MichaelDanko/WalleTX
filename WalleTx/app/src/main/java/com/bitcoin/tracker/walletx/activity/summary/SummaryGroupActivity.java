package com.bitcoin.tracker.walletx.activity.summary;

import android.os.Bundle;

import com.bitcoin.tracker.walletx.activity.SharedData;
import com.bitcoin.tracker.walletx.model.Walletx;

/**
 * Summary of a walletx group.
 */
public class SummaryGroupActivity extends SummaryAbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Walletx wtx = SharedData.WTXS_TO_SUMMARIZE.get(0);
        setActivityTitle(wtx.group.name);
    }

    @Override
    protected void refreshUi() {
        mTxSummaryModule.refreshModule();
        mSpendingSummaryModule.refreshModule();
    }

    @Override
    protected void setActivityTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

}