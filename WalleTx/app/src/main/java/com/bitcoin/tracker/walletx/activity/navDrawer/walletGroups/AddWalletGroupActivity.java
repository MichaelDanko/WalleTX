package com.bitcoin.tracker.walletx.activity.navDrawer.walletGroups;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import com.bitcoin.tracker.walletx.R;

/**
 * Display and handles the form associated with adding
 * new WalletGroups to the database.
 */
public class AddWalletGroupActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_wallet_group);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_activity_add_wallet_group);
    }

    /**
     * Closes activity when the home button is selected.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

} // AddWalletGroupActivity
