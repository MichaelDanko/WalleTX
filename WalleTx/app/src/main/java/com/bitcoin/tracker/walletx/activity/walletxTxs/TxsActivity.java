package com.bitcoin.tracker.walletx.activity.walletxTxs;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.bitcoin.tracker.walletx.R;

import java.util.ArrayList;

/**
 *
 * TODO complete click events, comment and clean code
 *      so team can update content
 *
 *
 *
 *
 */
public class TxsActivity extends ActionBarActivity {

    private ListView mListView;
    private TxsAdapter mAdapter;
    private ArrayList<TxsListItem> mItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walletx_txs);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mListView = (ListView) findViewById(R.id.txs_list);

        View header = getLayoutInflater().inflate(R.layout.fragment_walletx_txs_list_header, null);
        mListView.addHeaderView(header);
        // TODO Access header views so they can be updated

        prepareData();

        if (mAdapter == null)
            mAdapter = new TxsAdapter(this, mItems);
        if (mListView != null)
            mListView.setAdapter(mAdapter);

    }

    private void prepareData() {
        mItems.clear();
        // TODO Get list of txs associated with current group
        // for (Txs tx : txs) {
        // TODO Loop through list and setup list items
        for (int i = 0; i < 5; i++) {
            TxsListItem item;
            item = new TxsListItem("28 Mar 2015", "Uncategorized", "5", "7");
            mItems.add(item);
        }
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
