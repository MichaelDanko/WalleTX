package com.bitcoin.tracker.walletx.activity.walletxTxs;

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
import android.widget.Toast;

import com.bitcoin.tracker.walletx.R;
import com.bitcoin.tracker.walletx.activity.SyncableInterface;
import com.bitcoin.tracker.walletx.activity.txDetail.TxDetailActivity;
import com.bitcoin.tracker.walletx.api.SyncDatabase;
import com.bitcoin.tracker.walletx.model.Tx;
import com.bitcoin.tracker.walletx.model.WalletGroup;
import com.bitcoin.tracker.walletx.model.Walletx;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Activity displays a ListView of Txs associated with a group of Walletx's.
 * TODO Refactor to clean up code
 * TODO If Walletx implements parcelable refactor
 * TODO Finish OnItemClick event
 * TODO Color Amount/Amount label based on sent or received
 */
public class TxsActivity extends ActionBarActivity implements SyncableInterface {

    private static final int TX_CAT_UPDATED = 1;

    private ListView mListView;
    private TxsAdapter mAdapter;
    private List<Walletx> wtxs;
    private List<Tx> mTxs;
    private ArrayList<TxsListItem> mItems = new ArrayList<>();

    // Reference to activity
    static Activity mActivity;

    // displays when sync in progress
    private ProgressBar mSyncProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walletx_txs);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mActivity = this;

        // Build wtx list passed from summary activity by walletx name.
        // TODO Remove this code if Walletx implements parcelable like it should
        ArrayList<String> wtxNames = getIntent().getStringArrayListExtra("wtx_names");
        wtxs = new ArrayList<>(wtxNames.size());
        for ( String name : wtxNames ) {
            Walletx w = Walletx.getBy(name);
            wtxs.add(w);
        }

        mTxs = new ArrayList<>();
        for (Walletx wtx : wtxs) {
            List<Tx> txs = wtx.txs();
            for (Tx tx : txs) {
                mTxs.add(tx);
            }
        }

        // Setup list view header
        mListView = (ListView) findViewById(R.id.txs_list);
        View header = getLayoutInflater().inflate(R.layout.fragment_walletx_txs_list_header, null);
        mListView.addHeaderView(header);

        // Display group name associated with the tx's being listed
        TextView groupName = (TextView) header.findViewById(R.id.txs_header_group_name);
        groupName.setText(getIntent().getStringExtra("group_name"));

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

        // setup sync progress spinner
        mSyncProgressBar = (ProgressBar) findViewById(R.id.syncProgressBar);
        mSyncProgressBar.getIndeterminateDrawable().setColorFilter(Color.GRAY, android.graphics.PorterDuff.Mode.MULTIPLY);
        mSyncProgressBar.setVisibility(View.GONE);
    }

    private void prepareData() {
        mItems.clear();

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
            String amount = new Tx().formattedBTCValue(tx.amountBTC);
            String confirmations = Long.toString(tx.confirmations);
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
        if (item.getItemId() == R.id.action_sync) {
            new SyncDatabase(this);
            return true;
        } else if (id == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    //endregion
    //region SYNC

    public void startSyncRelatedUI() {
        // Rotate progress bar
        final ProgressBar pb = (ProgressBar) mActivity.findViewById(R.id.syncProgressBar);
        if ( mActivity != null && pb != null ) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    pb.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    public void stopSyncRelatedUI() {
        // stop progress bar
        final ProgressBar pb = (ProgressBar) mActivity.findViewById(R.id.syncProgressBar);
        if ( mActivity != null && pb != null ) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    pb.setVisibility(View.GONE);
                }
            });
        }
        // update list view
        prepareData();
        mAdapter.updateData(mItems);
    }

    //endregion
}
