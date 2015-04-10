package com.bitcoin.tracker.walletx.activity.txCategories;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.bitcoin.tracker.walletx.R;

/*

  TODO Validate input (no empty tags)
  TODO Add the tag


 */
public class TxCategoryCreateActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_tx_category);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    //region OPTIONS MENU

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

    //endregion

}
