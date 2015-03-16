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

import java.util.List;

/**
 * Fragment that displays an overview summary of transactions
 * associated with a wallet or group.
 */
public class WalletxSummaryModuleTxs extends Fragment {

    private OnFragmentInteractionListener mListener;

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
        View view = inflater.inflate(R.layout.fragment_walletx_summary_module_txs, container, false);
        bindClickEvents(view);
        updateUI(view);
        return view;
    }

    // Binds listener to this module
    private void bindClickEvents(View view) {
        RelativeLayout module = (RelativeLayout) view.findViewById(R.id.txsModuleContainer);
        module.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mListener.onFragmentInteraction(SupportedSummaryType.TRANSACTION_SUMMARY);
            }
        });
    }

    // Updates the module so that it reflects the model.
    private void updateUI(View view) {
        // get the list of walletxs being summarized from parent activity
        List<Walletx> wtxs = ((WalletxSummaryAbstractActivity)this.getActivity()).getWtxs();
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
