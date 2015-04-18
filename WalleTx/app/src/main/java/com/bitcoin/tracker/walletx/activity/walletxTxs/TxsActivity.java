package com.bitcoin.tracker.walletx.activity.walletxTxs;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bitcoin.tracker.walletx.R;
import com.bitcoin.tracker.walletx.activity.txDetail.TxDetailActivity;
import com.bitcoin.tracker.walletx.model.Tx;
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
public class TxsActivity extends ActionBarActivity {

    private ListView mListView;
    private TxsAdapter mAdapter;
    private ArrayList<Walletx> wtxs;
    private List<Tx> mTxs;
    private ArrayList<TxsListItem> mItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walletx_txs);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Build wtx list passed from summary activity by walletx name.
        // TODO Remove this code if Walletx implements parcelable like it should
        ArrayList<String> wtxNames = getIntent().getStringArrayListExtra("wtx_names");
        wtxs = new ArrayList<>(wtxNames.size());
        for ( String name : wtxNames ) {
            Walletx w = Walletx.getBy(name);
            wtxs.add(w);
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
                    startActivity(intent);
                }
            }
        });
    }

    private void prepareData() {
        mItems.clear();

        DateFormat df = new SimpleDateFormat("MM-dd-yyyy h:mm:ss a");

        TextView txsHeaderCount = (TextView) findViewById(R.id.txs_header_tx_count);

        long tempTxsCount = 0;

        // Get list of transactions associated with our wtxs
        for (Walletx w : wtxs) {

            List<Tx> txsForThisWtx = w.txs();
            mTxs = txsForThisWtx;

            tempTxsCount = w.totalReceive + w.totalSpend;

            for (int i = 0; i < txsForThisWtx.size(); i++) {
                TxsListItem item;
                String date = df.format(txsForThisWtx.get(i).timestamp);
                // This cause a null pointer expection, unsure if the TxCategory is being created
                // correctly in BlockChainInfo.java or if there is some other error.
                String category = txsForThisWtx.get(i).category.name;
                String amount = Long.toString(txsForThisWtx.get(i).amountBTC);
                String confirmations = Long.toString(txsForThisWtx.get(i).confirmations);
                String hash = txsForThisWtx.get(i).hash;
                item = new TxsListItem(date, category, amount, confirmations, hash);
                mItems.add(item);
            }
        }

        txsHeaderCount.setText(Long.toString(tempTxsCount));
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
