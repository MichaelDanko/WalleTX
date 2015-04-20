package com.bitcoin.tracker.walletx.activity.walletxSummary.module;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.bitcoin.tracker.walletx.R;
import com.bitcoin.tracker.walletx.activity.walletxSummary.WalletxSummaryAbstractActivity;
import com.bitcoin.tracker.walletx.model.SupportedSummaryType;
import com.bitcoin.tracker.walletx.model.Tx;
import com.bitcoin.tracker.walletx.model.TxCategory;
import com.bitcoin.tracker.walletx.model.Walletx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.PieChartView;

/**
 * Created by brianhowell on 4/20/15.
 */
public class WalletxSummaryModuleSpendingBreakdownByCategory  extends Fragment {

    private OnFragmentInteractionListener mListener;
    private MaterialRippleLayout mModuleContainer;
    private MaterialRippleLayout mChartContainer;
    private PieChartView mChart;
    private PieChartData mData;

    private List<Tx> mTxs = new LinkedList<>();
    private HashMap<String, Integer> mCats;

    private TextView mLegendText1;
    private RelativeLayout mLegendBox1;
    private String mLegendCatName1 = null;
    private int mLegendCatCount1 = 0;

    private TextView mLegendText2;
    private RelativeLayout mLegendBox2;
    private String mLegendCatName2 = null;
    private int mLegendCatCount2 = 0;

    private TextView mLegendText3;
    private RelativeLayout mLegendBox3;
    private String mLegendCatName3 = null;
    private int mLegendCatCount3 = 0;

    // Required empty public constructor
    public WalletxSummaryModuleSpendingBreakdownByCategory() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_walletx_summary_module_spending_breakdown_by_catgeory, container, false);
        getViewsFrom(v);
        setPieChartData();
        createPieChart();
        bindClickEvents();
        return v;
    }

    private void getViewsFrom (View v) {
        mChart = (PieChartView) v.findViewById(R.id.chart);
        mModuleContainer = (MaterialRippleLayout) v.findViewById(R.id.ripple);
        mChartContainer = (MaterialRippleLayout) v.findViewById(R.id.ripple1);
        mLegendBox1 = (RelativeLayout) v.findViewById(R.id.spending_module_legend_box_1);
        mLegendBox2 = (RelativeLayout) v.findViewById(R.id.spending_module_legend_box_2);
        mLegendBox3 = (RelativeLayout) v.findViewById(R.id.spending_module_legend_box_3);
        mLegendText1 = (TextView) v.findViewById(R.id.spending_module_legend_text_1);
        mLegendText2 = (TextView) v.findViewById(R.id.spending_module_legend_text_2);
        mLegendText3 = (TextView) v.findViewById(R.id.spending_module_legend_text_3);
    }

    private void buildCategoryHashMap() {
        mCats = new HashMap<>();

        // Get list of wallets to summarize from parent
        List<Walletx> wtxs = ((WalletxSummaryAbstractActivity)this.getActivity()).getWtxs();

        // Build list of all associated transactions
        for ( Walletx wtx : wtxs ) {
            List<Tx> txsForThisWtx = wtx.txs();
            for ( Tx tx : txsForThisWtx ) {
                mTxs.add(tx);
            }
        }

        // Build category count hash table
        mCats.put("Uncategorized", 0);
        for ( Tx tx : mTxs ) {

            if ( tx.amountBTC > 0 ) {
                // only include spends
                continue;
            }

            // TODO only add txs within a certain time period

            TxCategory cat = tx.category;
            if ( cat != null ) {
                String catName = cat.name;
                if ( mCats.containsKey(catName) ) {
                    mCats.put(catName, mCats.get(catName) + 1);
                } else {
                    mCats.put(catName, 1);
                }
            } else {
                mCats.put("Uncategorized", mCats.get("Uncategorized") + 1);
            }

        }
    }

    private void calculateTopThreeCategories() {
        // Determine top 3 categories to summarize
        Map.Entry<String, Integer> first = null;
        Map.Entry<String, Integer> second = null;
        Map.Entry<String, Integer> third = null;

        // Determine #1
        for (HashMap.Entry<String, Integer> entry : mCats.entrySet())
        {
            if (first == null || entry.getValue().compareTo(first.getValue()) > 0)
                first = entry;
        }

        // Determine #2
        if ( mCats.size() > 1 ) {
            for (HashMap.Entry<String, Integer> entry : mCats.entrySet())
            {
                if (entry.equals(first))
                    continue;
                if (second == null || entry.getValue().compareTo(second.getValue()) > 0)
                    second = entry;
            }
        }

        // Determine #3
        if ( mCats.size() > 2 ) {
            for (HashMap.Entry<String, Integer> entry : mCats.entrySet())
            {
                if (entry.equals(first) || entry.equals(second))
                    continue;
                if (third == null || entry.getValue().compareTo(third.getValue()) > 0)
                    third = entry;
            }
        }

        // Save the top 3 so we can update UI
        mLegendCatName1 = first.getKey();
        mLegendCatCount1 = first.getValue();

        if (second != null) {
            mLegendCatName2 = second.getKey();
            mLegendCatCount2 = second.getValue();
        }
        if (third != null) {
            mLegendCatName3 = third.getKey();
            mLegendCatCount3 = third.getValue();
        }
    }

    private void setupChartLegend() {
        mLegendBox1.setBackgroundColor(ChartUtils.COLOR_BLUE);
        mLegendBox2.setBackgroundColor(ChartUtils.COLOR_ORANGE);
        mLegendBox3.setBackgroundColor(ChartUtils.COLOR_VIOLET);
        mLegendText3.setVisibility(View.VISIBLE);
        mLegendBox3.setVisibility(View.VISIBLE);
        mLegendText2.setVisibility(View.VISIBLE);
        mLegendBox2.setVisibility(View.VISIBLE);

        // We want to summarize the top 3 categories.
        // Set the text views for the top 3 and
        // Hide legend elements if there are less than 3 in total.
        mLegendText1.setText(mLegendCatName1);
        if ( mLegendCatCount2 == 0 ) {
            mLegendText2.setVisibility(View.GONE);
            mLegendBox2.setVisibility(View.GONE);
        } else {
            mLegendText2.setText(mLegendCatName2);
        }
        if ( mLegendCatCount3 == 0 ) {
            mLegendText3.setVisibility(View.GONE);
            mLegendBox3.setVisibility(View.GONE);
        } else {
            mLegendText3.setText(mLegendCatName3);
        }
    }

    private void setPieChartData() {
        List<SliceValue> values = new ArrayList<>();
        values.add(new SliceValue((float) mLegendCatCount1, ChartUtils.COLOR_BLUE));
        if (mLegendCatCount2 != 0)
            values.add(new SliceValue((float) mLegendCatCount2, ChartUtils.COLOR_ORANGE));
        if (mLegendCatCount3 != 0)
            values.add(new SliceValue((float) mLegendCatCount3, ChartUtils.COLOR_VIOLET));
        mData = new PieChartData(values);
    }

    private void createPieChart() {
        mChart.setChartRotationEnabled(false);
        mChart.setPieChartData(mData);
    }

    public void refreshModule() {
        buildCategoryHashMap();
        calculateTopThreeCategories();
        setupChartLegend();
        setPieChartData();
        mChart.setPieChartData(null);
        createPieChart();

        System.out.format("CATCOUNT1: %s\n", mLegendCatCount1);
        System.out.format("CATCOUNT2: %s\n", mLegendCatCount2);
        System.out.format("CATCOUNT3: %s\n", mLegendCatCount3);
    }

    /**
     * Binds listener to the module container element
     */
    private void bindClickEvents() {
        mModuleContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onFragmentInteraction(SupportedSummaryType.SPENDING_BY_CATEGORY);
            }
        });
        mChartContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onFragmentInteraction(SupportedSummaryType.SPENDING_BY_CATEGORY);
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(SupportedSummaryType type);
    }
}
