package com.bitcoin.tracker.walletx.activity.tx;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bitcoin.tracker.walletx.R;
import com.bitcoin.tracker.walletx.activity.Constants;
import com.bitcoin.tracker.walletx.activity.SharedData;
import com.bitcoin.tracker.walletx.activity.SyncableActivity;
import com.bitcoin.tracker.walletx.api.BlockchainInfo;
import com.bitcoin.tracker.walletx.helper.Formatter;
import com.bitcoin.tracker.walletx.model.Tx;
import com.bitcoin.tracker.walletx.model.Walletx;

import java.text.DateFormat;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Activity displays a ListView of Txs associated with a group of Walletx's.
 */
public class TxsActivity extends SyncableActivity {

    private ListView mListView;
    private TxsAdapter mAdapter;
    private List<Tx> mTxs = new ArrayList<>();
    private ArrayList<TxsListItem> mItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.txs_activity);
        setupActionBar();
        setupListView();
        setupListView();
        bindListeners();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.RESULT_TX_UPDATED)
            refreshUi();
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
    protected void refreshUi() {
        int currentPos = mListView.getFirstVisiblePosition();
        prepareData();
        mAdapter.updateData(mItems);
        mListView.setSelection(currentPos);
    }

    private void setupActionBar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String title = getIntent().getStringExtra(Constants.EXTRA_GROUP_TO_SUMMARIZE);
        getSupportActionBar().setTitle(title);
    }

    private void setupListView() {
        mListView = (ListView) findViewById(R.id.txs_list);
        prepareData();
        if (mAdapter == null)
            mAdapter = new TxsAdapter(this, mItems);
        if (mListView != null)
            mListView.setAdapter(mAdapter);
    }

    private void bindListeners() {

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Include TxDetailActivity functionality here rather than in a new activity.
                Intent intent = new Intent(getBaseContext(), TxDetailActivity.class);
                Tx tx = mTxs.get(position - 1);
                intent.putExtra( "hash", tx.hash );
                startActivityForResult( intent, Constants.RESULT_TX_UPDATED );
            }

        });

    } // bindListeners

    private void prepareData() {
        mItems.clear();
        mTxs.clear();

        // Build and sort the tx list
        for (Walletx wtx : SharedData.WTXS_TO_SUMMARIZE) {
            List<Tx> txs = wtx.txs();
            for (Tx tx : txs) {
                mTxs.add(tx);
            }
        }

        Collections.sort(mTxs, new Comparator<Tx>() {
            @Override
            public int compare(Tx tx1, Tx tx2) {
                return tx2.timestamp.compareTo(tx1.timestamp);
            }
        });

        DateFormat df = new SimpleDateFormat("MM-dd-yyyy h:mm:ss a");

        // Get list of transactions associated with our wtxs
        for (Tx tx : mTxs) {
            TxsListItem item;
            String date = df.format(tx.timestamp);
            String category;
            if (tx.category != null)
                category = tx.category.name;
            else
                category = "Uncategorized";
            String amount = Formatter.btcToString(tx.amountBTC);
            String confirmations;
            long latestBlock = BlockchainInfo.getLatestBlock(getApplicationContext()) - tx.block + 1;
            if (latestBlock - 1 == BlockchainInfo.getLatestBlock(getApplicationContext()))
                confirmations = "0";
            else
                confirmations = String.valueOf(latestBlock);
            String hash = tx.hash;
            item = new TxsListItem(date, category, amount, confirmations, hash);
            mItems.add(item);
        }

    }

} // TxActivity
