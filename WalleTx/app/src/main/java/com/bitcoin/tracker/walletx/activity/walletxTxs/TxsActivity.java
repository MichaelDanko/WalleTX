package com.bitcoin.tracker.walletx.activity.walletxTxs;

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
import com.bitcoin.tracker.walletx.model.Tx;
import com.bitcoin.tracker.walletx.model.Walletx;

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
    private List<Tx> txs;
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
                Toast.makeText(getApplicationContext(), "Open Tx detail activity for this tx", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void prepareData() {
        mItems.clear();

        // TODO Get txs and add to mItems (Code below written quickly, untested)
        // Get list of transactions associated with our wtxs
        //for ( Walletx w : wtxs ) {
        //    List<Tx> txsForThisWtx = w.txs();
        //    for ( Tx tx : txsForThisWtx ) {
        //        txs.add(tx);
        //    }
        //}
        //for ( Tx tx : txs ) {
        //    TxsListItem item;
        //    String date = tx...... get date as string
        //    String category = tx..... get category as string
        //    String amount = tx...... get amount as string
        //    String confs = tx..... get confirmation count as string
        //    item = new TxsListItem(date, category, amount, confs);
        //    mItems.add(item);
        //}

        // Throwing some garbage data in list view TODO replace this code
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
