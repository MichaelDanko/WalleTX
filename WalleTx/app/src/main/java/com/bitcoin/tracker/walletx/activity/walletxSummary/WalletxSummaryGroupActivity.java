package com.bitcoin.tracker.walletx.activity.walletxSummary;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.bitcoin.tracker.walletx.model.SupportedSummaryType;
import com.bitcoin.tracker.walletx.model.WalletGroup;

/**
 * Summary of a walletx group.
 */
public class WalletxSummaryGroupActivity extends WalletxSummaryAbstractActivity {

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
    }

    protected void populateWalletxList() {
        WalletGroup group = WalletGroup.getBy(mGroupName);
        wtxs = group.walletxs();
    }

    protected void setActivityTitle() {
        getSupportActionBar().setTitle(mGroupName);
    }

    //endregion

    @Override
    public void onFragmentInteraction(SupportedSummaryType type){
        Toast toast = Toast.makeText(this, "TODO: Go to detail activity for transactions associated with WALLET GROUP!",Toast.LENGTH_SHORT);
        toast.show();
    }

}