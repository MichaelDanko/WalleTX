package com.bitcoin.tracker.walletx.activity.chart;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.bitcoin.tracker.walletx.R;
import com.bitcoin.tracker.walletx.model.Category;
import com.bitcoin.tracker.walletx.model.Tx;
import com.bitcoin.tracker.walletx.model.Walletx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.PieChartView;

/*
 * TODO This activity requires refactoring.
 *      However, it is my intent to replace HelloCharts since it sucks.
 *      I'll refactor when I do this. BH
 */
public class ChartSpendingByCategoryActivity extends ActionBarActivity {

    private PieChartView mChart;
    private PieChartData mData;
    private String mGroupName;
    private ArrayList<String> mWtxs;
    private HashMap<String, Long> mCats;
    private List<Tx> mTxs = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart_spending_by_category_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        mGroupName = intent.getStringExtra("group_name");
        mWtxs = intent.getStringArrayListExtra("wtx_names");

        getSupportActionBar().setTitle(mGroupName);

        mChart = (PieChartView) findViewById(R.id.chart);
        buildCategoryHashMap();
        setPieChartData();
        createPieChart();
    }

    private void buildCategoryHashMap() {
        mCats = new HashMap<>();

        // Get list of wallets to summarize passed from parent
        List<Walletx> wtxs = new ArrayList<>();
        for (String wtxName : mWtxs) {
            wtxs.add(Walletx.getBy(wtxName));
        }

        // Build list of all associated transactions
        for ( Walletx wtx : wtxs ) {
            List<Tx> txsForThisWtx = wtx.txs();
            for ( Tx tx : txsForThisWtx ) {
                mTxs.add(tx);
            }
        }

        // Build category count hash table
        for ( Tx tx : mTxs ) {
            // only include spends
            if ( tx.amountBTC > 0 ) {
                continue;
            }
            Category cat = tx.category;
            if ( cat != null ) {
                String catName = cat.name;
                if ( mCats.containsKey(catName) ) {
                    mCats.put(catName, mCats.get(catName) + tx.amountBTC);
                } else {
                    mCats.put(catName, tx.amountBTC);
                }
            } else {
                if (mCats.get("Uncategorized") != null) {
                    mCats.put("Uncategorized", mCats.get("Uncategorized") + tx.amountBTC);
                } else {
                    mCats.put("Uncategorized", tx.amountBTC);
                }
            }
        }
    }

    private void setPieChartData() {
        List<SliceValue> values = new ArrayList<>();
        for (Map.Entry<String, Long> entry : mCats.entrySet()) {
            Float value = -1 * Float.valueOf(entry.getValue());
            SliceValue sv = new SliceValue(value, ChartUtils.pickColor());
            sv.setLabel(entry.getKey());
            values.add(sv);
        }
        mData = new PieChartData(values);
    }

    private void createPieChart() {
        mData.setHasLabels(true);
        mData.setHasLabelsOutside(false);
        mChart.setOnValueTouchListener(new PieChartOnValueSelectListener() {
            @Override
            public void onValueSelected(int i, SliceValue sliceValue) {
                Float convertValue = sliceValue.getValue() / 100000000;
                String label = convertValue +  " BTC";
                Toast.makeText(getApplicationContext(), label, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onValueDeselected() {

            }
        });
        mChart.setZoomEnabled(true);
        mChart.setPieChartData(mData);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}
