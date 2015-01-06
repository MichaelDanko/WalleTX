package com.bitcoin.tracker.walletx.activity.walletx.create;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.bitcoin.tracker.walletx.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

/**
 * Manages form elements associated with adding a new single address wallet.
 */
public class WalletxCreateSingleAddressWalletFragment extends Fragment {

    //region FIELDS

    private Fragment fragment;
    private OnFragmentInteractionListener mListener;
    private ImageButton qrCode;
    private Button submitButton;

    // EditText fields with content that needs to persist after orientation changes.
    private EditText publicKey;
    private EditText name;

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
        fragment = this;
    }

    /**
     * Inflates the layout for this fragment and binds click events to buttons.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_walletx_create_single_address_wallet, container, false);
        publicKey = (EditText) view.findViewById(R.id.editTextPublicKey);
        qrCode = (ImageButton) view.findViewById(R.id.scanQrCode);
        qrCode.setOnClickListener(qrCodeListener);
        name = (EditText) view.findViewById(R.id.editTextSaWalletName);
        submitButton = (Button) view.findViewById(R.id.submitButton);
        submitButton.setOnClickListener(submitButtonListener);
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

    private View.OnClickListener qrCodeListener = new View.OnClickListener() {
        public void onClick(View v) {
            IntentIntegrator integrator = IntentIntegrator.forFragment(fragment);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
            integrator.setPrompt("Scan public key");
            integrator.setResultDisplayDuration(0);
            integrator.initiateScan();
        }
    };

    private View.OnClickListener submitButtonListener = new View.OnClickListener() {
        public void onClick(View v) {
            onSubmit();
        }
    };

    /**
     * Retrieves QRCode scan result and adds to public key EditText field.
     */
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            publicKey.setText(scanResult.getContents()); // add code to edit text field
        }
    }

    /**
     * Adds new single address wallet to the database which includes fetching all tx data.
     * Upon completion calls on WalletCreateActivity to finish.
     */
    public void onSubmit() {

        //----------------------------------------
        // TODO Add new SAWallet to the database
        // Remember to fetch txs too
        //----------------------------------------

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
