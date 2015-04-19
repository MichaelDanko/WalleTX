package com.bitcoin.tracker.walletx.activity.walletxSummary;

import android.content.Intent;
import android.os.Bundle;

import com.bitcoin.tracker.walletx.activity.walletxSummary.module.WalletxSummaryModuleTxs;
import com.bitcoin.tracker.walletx.model.Walletx;

import java.util.LinkedList;

/**
 * Summary for a single walletx
 */
public class WalletxSummarySingleActivity extends WalletxSummaryAbstractActivity {

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

        // Refresh the pie chart to display data for this wallet
        mTxSummaryModule.refreshPieChart();
    }

    protected void populateWalletxList() {
        wtxs = new LinkedList<>();
        wtxs.add(Walletx.getBy(mWalletxName));
    }

    protected void setActivityTitle() {
        getSupportActionBar().setTitle(mWalletxName);
    }

    //endregion

}
