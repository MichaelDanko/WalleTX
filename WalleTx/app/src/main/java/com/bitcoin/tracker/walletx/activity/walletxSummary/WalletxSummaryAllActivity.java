package com.bitcoin.tracker.walletx.activity.walletxSummary;

import android.os.Bundle;
import android.widget.Toast;

import com.bitcoin.tracker.walletx.R;
import com.bitcoin.tracker.walletx.model.SupportedSummaryType;
import com.bitcoin.tracker.walletx.model.Walletx;

/**
 * Summary of all walletxs
 */
public class WalletxSummaryAllActivity extends WalletxSummaryAbstractActivity {

    //region ACTIVITY LIFECYCLE

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityTitle();
        populateWalletxList();
    }

    protected void populateWalletxList() {
        wtxs = Walletx.getAll();
    }

    protected void setActivityTitle() {
        getSupportActionBar().setTitle(R.string.walletx_summary_all_title_activity_shortened);
    }

    //endregion

    @Override
    public void onFragmentInteraction(SupportedSummaryType type){
        if (type.equals(SupportedSummaryType.TRANSACTION_SUMMARY)) {
            Toast toast = Toast.makeText(this, "TODO: Go to detail activity for transactions associated with ALL WALLETS!",Toast.LENGTH_SHORT);
            toast.show();
        }
    }

}
