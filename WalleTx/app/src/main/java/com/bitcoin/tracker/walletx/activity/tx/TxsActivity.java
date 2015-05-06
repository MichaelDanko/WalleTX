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
 * TODO Refactor to clean up code
 * TODO If Walletx implements parcelable refactor
 * TODO Finish OnItemClick event
 * TODO Color Amount/Amount label based on sent or received
 */
public class TxsActivity extends ActionBarActivity {

    private static final int TX_CAT_UPDATED = 1;

    private ListView mListView;
    private TxsAdapter mAdapter;
    private List<Tx> mTxs = new ArrayList<>();
    private ArrayList<TxsListItem> mItems = new ArrayList<>();

    // Reference to activity
    static Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.txs_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mActivity = this;

        // Setup list view header
        mListView = (ListView) findViewById(R.id.txs_list);
        View header = getLayoutInflater().inflate(R.layout.txs_activity_list_header, null);
        mListView.addHeaderView(header);

        // Display group name associated with the tx's being listed
        TextView groupName = (TextView) header.findViewById(R.id.txs_header_group_name);
        groupName.setText(getIntent().getStringExtra(Constants.EXTRA_GROUP_TO_SUMMARIZE));

        String title = getIntent().getStringExtra(Constants.EXTRA_GROUP_TO_SUMMARIZE);
        getSupportActionBar().setTitle(title);

        // Setup list view
        prepareData();
        if (mAdapter == null)
            mAdapter = new TxsAdapter(this, mItems);
        if (mListView != null)
            mListView.setAdapter(mAdapter);

        // Bind on item click listener
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Again... We should pass the Tx object but it is not parcelable
                // So an interim fix is to pass something to id the Tx and then
                // re-query it in the next activity
                if (position != 0) {
                    Intent intent = new Intent( getBaseContext(), TxDetailActivity.class );
                    // TODO Uncomment and test once data is present
                    Tx tx = mTxs.get(position - 1);
                    intent.putExtra( "hash", tx.hash );
                    startActivityForResult( intent, TX_CAT_UPDATED );
                }
            }
        });

    }

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

        TextView txsHeaderCount = (TextView) findViewById(R.id.txs_header_tx_count);

        long tempTxsCount = 0;

        // Get list of transactions associated with our wtxs
        for (Tx tx : mTxs) {
            TxsListItem item;
            String date = df.format(tx.timestamp);
            // This cause a null pointer expection, unsure if the TxCategory is being created
            // correctly in BlockChainInfo.java or if there is some other error.
            String category;
            //txsForThisWtx.get(i).category.name;
            if (tx.category != null) {
                category = tx.category.name;
            } else {
                category = "Uncategorized";
            }
            String amount = Formatter.btcToString(tx.amountBTC);


            // TODO CALC REALTIME
            String confirmations = Long.toString(1);


            String hash = tx.hash;
            item = new TxsListItem(date, category, amount, confirmations, hash);
            mItems.add(item);
            tempTxsCount++;
        }

        txsHeaderCount.setText(Long.toString(tempTxsCount));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TX_CAT_UPDATED) {
            int currentPos = mListView.getFirstVisiblePosition();
            prepareData();
            mAdapter.updateData(mItems);
            mListView.setSelection(currentPos);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sync, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

}
