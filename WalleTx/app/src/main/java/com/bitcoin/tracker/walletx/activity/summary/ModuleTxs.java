package com.bitcoin.tracker.walletx.activity.summary;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.balysv.materialripple.MaterialRippleLayout;
import com.bitcoin.tracker.walletx.R;
import com.bitcoin.tracker.walletx.activity.SharedData;
import com.bitcoin.tracker.walletx.model.SupportedSummaryType;
import com.bitcoin.tracker.walletx.model.Tx;
import com.bitcoin.tracker.walletx.model.Walletx;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.PieChartView;

/**
 * Fragment that displays an overview summary of transactions
 * associated with a wallet or group.
 */
public class ModuleTxs extends Fragment {

    private OnFragmentInteractionListener mListener;
    private MaterialRippleLayout mModuleContainer;
    private MaterialRippleLayout mChartContainer;
    private PieChartView mChart;
    private PieChartData mData;
    private int mSentCount;
    private int mReceivedCount;

    private RelativeLayout mReceivedLegend;
    private RelativeLayout mSentLegend;

    // Required empty public constructor
    public ModuleTxs() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflate the layout for this fragment
        View v = inflater.inflate(R.layout.summary_module_fragment_txs, container, false);
        getViewsFrom(v);
        setupChartLegend();
        setPieChartData();
        createPieChart();
        bindListeners();
        return v;
    }

    private void getViewsFrom (View v) {
        mChart = (PieChartView) v.findViewById(R.id.chart);
        mModuleContainer = (MaterialRippleLayout) v.findViewById(R.id.ripple);
        mChartContainer = (MaterialRippleLayout) v.findViewById(R.id.ripple1);
        mReceivedLegend = (RelativeLayout) v.findViewById(R.id.tx_module_legend_received_box);
        mSentLegend = (RelativeLayout) v.findViewById(R.id.tx_module_legend_sent_box);
    }

    private void setupChartLegend() {
        mReceivedLegend.setBackgroundColor(ChartUtils.COLOR_GREEN);
        mSentLegend.setBackgroundColor(ChartUtils.COLOR_RED);
    }

    private void setPieChartData() {

        mSentCount = 0;
        mReceivedCount = 0;

        for (Walletx wtx: SharedData.WTXS_TO_SUMMARIZE) {
            List<Tx> txs = wtx.txs();
            for (Tx tx : txs) {
                if (tx.amountBTC >= 0)
                    mReceivedCount++;
                else
                    mSentCount++;
            }
        }

        List<SliceValue> values = new ArrayList<>();
        values.add(new SliceValue((float) mReceivedCount, ChartUtils.COLOR_GREEN));
        values.add(new SliceValue((float) mSentCount, ChartUtils.COLOR_RED));

        mData = new PieChartData(values);
    }

    private void createPieChart() {
        mData.setHasLabels(true);
        mData.setHasLabelsOutside(false);
        mData.setHasCenterCircle(true);
        mData.setCenterText1(Integer.toString(mSentCount + mReceivedCount));
        mData.setCenterText2(getString(R.string.walletx_summary_module_txs_pie_chart_center));
        mData.setCenterText1FontSize(25);
        mChart.setChartRotationEnabled(false);
        mChart.setPieChartData(mData);
    }

    public void refreshModule() {
        setPieChartData();
        mChart.setPieChartData(null);
        createPieChart();
    }

    /**
     * Binds listener to the module container element
     */
    private void bindListeners() {
        mModuleContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onFragmentInteraction(SupportedSummaryType.TRANSACTION_SUMMARY);
            }
        });
        mChartContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onFragmentInteraction(SupportedSummaryType.TRANSACTION_SUMMARY);
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
