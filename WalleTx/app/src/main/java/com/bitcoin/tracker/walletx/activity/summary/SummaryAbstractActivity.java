package com.bitcoin.tracker.walletx.activity.summary;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.bitcoin.tracker.walletx.R;

import com.bitcoin.tracker.walletx.activity.Constants;
import com.bitcoin.tracker.walletx.activity.SyncableActivity;
import com.bitcoin.tracker.walletx.activity.chart.ChartSpendingByCategoryActivity;
import com.bitcoin.tracker.walletx.activity.tx.TxsActivity;
import com.bitcoin.tracker.walletx.model.SupportedSummaryType;

/**
 * Walletx summary abstract super class.
 */
public abstract class SummaryAbstractActivity extends SyncableActivity implements
        ModuleTxs.OnFragmentInteractionListener,
        ModuleSpending.OnFragmentInteractionListener {

    // Summary modules
    protected ModuleTxs mTxSummaryModule;
    protected ModuleSpending mSpendingSummaryModule;

    // Required implementation by child classes
    protected abstract void refreshUi();
    protected abstract void setActivityTitle(String title);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summary_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mTxSummaryModule = (ModuleTxs) getFragmentManager().findFragmentById(R.id.moduleTxs);
        mSpendingSummaryModule = (ModuleSpending) getFragmentManager().
                findFragmentById(R.id.moduleSpendingBreakdown);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTxSummaryModule.refreshModule();
        mSpendingSummaryModule.refreshModule();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(SupportedSummaryType type){

        Intent intent;
        switch (type) {
            case TRANSACTION_SUMMARY:
                intent = new Intent( this, TxsActivity.class );
                intent.putExtra(Constants.EXTRA_GROUP_TO_SUMMARIZE,
                        getSupportActionBar().getTitle().toString());
                startActivity( intent );
                break;
            case SPENDING_BY_CATEGORY:
                intent = new Intent( this, ChartSpendingByCategoryActivity.class );
                intent.putExtra(Constants.EXTRA_GROUP_TO_SUMMARIZE,
                        getSupportActionBar().getTitle().toString());
                startActivity( intent );
                break;
            default:
                break;
        }
    }

} // SummaryAbstractActivity
