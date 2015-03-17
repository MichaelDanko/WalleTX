package com.bitcoin.tracker.walletx.activity.walletxSummary.module;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.bitcoin.tracker.walletx.R;
import com.bitcoin.tracker.walletx.activity.walletxSummary.WalletxSummaryAbstractActivity;
import com.bitcoin.tracker.walletx.model.SupportedSummaryType;
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
public class WalletxSummaryModuleTxs extends Fragment {

    private OnFragmentInteractionListener mListener;
    private RelativeLayout mModuleContainer;
    private PieChartView mChart;
    private PieChartData mData;
    private int mSentCount;
    private int mReceivedCount;

    // Required empty public constructor
    public WalletxSummaryModuleTxs() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_walletx_summary_module_txs, container, false);
        getViewsFrom(v);
        setPieChartData();
        createPieChart();
        bindClickEvents();
        return v;
    }

    private void getViewsFrom (View v) {
        mChart = (PieChartView) v.findViewById(R.id.chart);
        mModuleContainer = (RelativeLayout) v.findViewById(R.id.module);
    }

    private void setPieChartData() {
        // get the list of walletxs being summarized from parent activity
        List<Walletx> wtxs = ((WalletxSummaryAbstractActivity)this.getActivity()).getWtxs();

        mReceivedCount = 11; // TODO @dc add query to return the number of txs received (replace # with query)
        mSentCount = 7; // TODO @dc add query to return the number of txs sent (replace # with query)

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
        mData.setCenterText1FontSize(45);
        mChart.setChartRotationEnabled(false);
        mChart.setPieChartData(mData);
    }

    /**
     * Binds listener to the module container element
     */
    private void bindClickEvents() {
        mModuleContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onFragmentInteraction(SupportedSummaryType.TRANSACTION_SUMMARY);
            }
        });
        mChart.setOnClickListener(new View.OnClickListener() {
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
