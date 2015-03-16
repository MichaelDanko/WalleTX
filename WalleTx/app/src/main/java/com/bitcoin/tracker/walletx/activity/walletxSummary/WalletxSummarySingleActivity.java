package com.bitcoin.tracker.walletx.activity.walletxSummary;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.bitcoin.tracker.walletx.model.SupportedSummaryType;
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
    }

    protected void populateWalletxList() {
        wtxs = new LinkedList<Walletx>();
        wtxs.add(Walletx.getBy(mWalletxName));
    }

    protected void setActivityTitle() {
        getSupportActionBar().setTitle(mWalletxName);
    }

    //endregion

    @Override
    public void onFragmentInteraction(SupportedSummaryType type){
        Toast toast = Toast.makeText(this, "TODO: Go to detail activity for transactions associated with SINGLE WALLET!",Toast.LENGTH_SHORT);
        toast.show();
    }

}
