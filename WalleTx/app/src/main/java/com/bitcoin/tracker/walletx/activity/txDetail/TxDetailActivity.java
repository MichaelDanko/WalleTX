package com.bitcoin.tracker.walletx.activity.txDetail;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.bitcoin.tracker.walletx.R;

public class TxDetailActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tx_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    //region OPTIONS MENU

    /**
     * Display the global options menu.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.global, menu);
        return true;
    }

    /**
     * Home button closes the activity.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    //endregion
}
