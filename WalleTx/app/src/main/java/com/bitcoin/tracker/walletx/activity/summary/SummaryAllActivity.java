package com.bitcoin.tracker.walletx.activity.summary;

import android.os.Bundle;

import com.bitcoin.tracker.walletx.R;
import com.bitcoin.tracker.walletx.model.Walletx;

/**
 * Summary of all walletxs
 */
public class SummaryAllActivity extends SummaryAbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityTitle(getString(R.string.walletx_summary_all_title_activity_shortened));
        // Refresh the modules now that the wtx list is populated
        refreshUi();
    }

    @Override
    protected void setActivityTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    protected void refreshUi() {
        mTxSummaryModule.refreshPieChart();
        mSpendingSummaryModule.refreshModule();
    }

}
