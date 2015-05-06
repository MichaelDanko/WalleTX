package com.bitcoin.tracker.walletx.activity.summary;

import android.os.Bundle;

import com.bitcoin.tracker.walletx.R;
import com.bitcoin.tracker.walletx.model.Walletx;

/**
 * Summary of all walletxs
 */
public class SummaryAllActivity extends SummaryAbstractActivity {

    //region ACTIVITY LIFECYCLE

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityTitle();
        populateWalletxList();

        // Refresh the modules now that the wtx list is populated
        mTxSummaryModule.refreshPieChart();
        mSpendingSummaryModule.refreshModule();
    }

    protected void populateWalletxList() {
        wtxs = Walletx.getAll();
    }

    protected void setActivityTitle() {
        getSupportActionBar().setTitle(R.string.walletx_summary_all_title_activity_shortened);
    }

    protected void refreshUi() {

    }

    //endregion

}
