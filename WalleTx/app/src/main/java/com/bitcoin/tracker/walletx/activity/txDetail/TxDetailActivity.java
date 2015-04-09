package com.bitcoin.tracker.walletx.activity.txDetail;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.bitcoin.tracker.walletx.R;

/*
 * Tx Details Activity
 *
 * TODO Save any tag updates before the view is destroyed.
 * We can vastly improve upon this... Time permitting
 *
 */
public class TxDetailActivity extends ActionBarActivity {

    AutoCompleteTextView mTag;

    private static final String[] TAGS = new String[] {
            "Pizza","Beer","Movies", "Clothes", "Income", "Shoes", "Dog Food"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tx_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // prevent autofocus on tags autocompletetextview
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, TAGS);
        mTag = (AutoCompleteTextView) findViewById(R.id.tag);
        mTag.setAdapter(adapter);

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
