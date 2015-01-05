package com.bitcoin.tracker.walletx.activity.walletx.create;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bitcoin.tracker.walletx.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WalletxCreateSingleAddressWalletFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WalletxCreateSingleAddressWalletFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WalletxCreateSingleAddressWalletFragment extends Fragment {

    //region FIELDS

    private OnFragmentInteractionListener mListener;

    //endregion
    //region FRAGMENT LIFECYCLE

    public static WalletxCreateSingleAddressWalletFragment newInstance() {
        return new WalletxCreateSingleAddressWalletFragment();
    }

    // Required empty public constructor
    public WalletxCreateSingleAddressWalletFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Inflates the layout for this fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_walletx_create_single_address_wallet, container, false);
        Button submitButton = (Button) view.findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onSubmit();
            }
        });
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    //endregion
    //region EVENT HANDLING

    /**
     * Adds new single address wallet to the database which includes fetching all tx data.
     * Upon completion calls on WalletCreateActivity to finish.
     */
    public void onSubmit() {
        // TODO Add new SAWallet to the database
        if (mListener != null) {
            mListener.onFragmentInteraction();
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction();
    }

    //endregion

}
