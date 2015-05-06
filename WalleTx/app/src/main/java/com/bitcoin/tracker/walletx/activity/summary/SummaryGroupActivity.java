package com.bitcoin.tracker.walletx.activity.summary;

import android.content.Intent;
import android.os.Bundle;

import com.bitcoin.tracker.walletx.model.Group;

/**
 * Summary of a walletx group.
 */
public class SummaryGroupActivity extends SummaryAbstractActivity {

    // Name of the group we are summarizing
    private String mGroupName;

    //region ACTIVITY LIFECYCLE

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        mGroupName = intent.getStringExtra("group_name");
        setActivityTitle();
        populateWalletxList();

        // Refresh the modules now that the wtx list is populated
        mTxSummaryModule.refreshPieChart();
        mSpendingSummaryModule.refreshModule();
    }

    protected void populateWalletxList() {
        Group group = Group.getBy(mGroupName);
        wtxs = group.walletxs();
    }

    protected void setActivityTitle() {
        getSupportActionBar().setTitle(mGroupName);
    }

    protected void refreshUi() {

    }

    //endregion

}