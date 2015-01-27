package com.bitcoin.tracker.walletx.activity.walletx.create;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.bitcoin.tracker.walletx.R;
import com.bitcoin.tracker.walletx.model.SingleAddressWallet;
import com.bitcoin.tracker.walletx.model.WalletGroup;
import com.bitcoin.tracker.walletx.model.WalletType;
import com.bitcoin.tracker.walletx.model.Walletx;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages form elements associated with adding a new single address wallet.
 */
public class WalletxCreateSingleAddressWalletFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    //region FIELDS

    private Fragment fragment;
    private OnFragmentInteractionListener mListener;
    private EditText mPublicKey;
    private EditText mWalletName;
    private Spinner mGroupNameSpinner;
    private ArrayAdapter<CharSequence> mAdapter;

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
        mPublicKey = (EditText) view.findViewById(R.id.editTextPublicKey);
        ImageButton qrCode = (ImageButton) view.findViewById(R.id.scanQrCode);
        qrCode.setOnClickListener(qrCodeListener);
        mWalletName = (EditText) view.findViewById(R.id.editTextSaWalletName);
        setupGroupNameSpinner(view);
        Button submitButton = (Button) view.findViewById(R.id.submitButton);
        submitButton.setOnClickListener(submitButtonListener);

        // Set the wallet group spinner to the default wallet group only
        // when activity is first created.
        if (savedInstanceState == null) {
            WalletGroup defaultGroup = WalletGroup.getDefault();
            int pos = mAdapter.getPosition(defaultGroup.name);
            mGroupNameSpinner.setSelection(pos);
        }

        return view;
    }

    private void setupGroupNameSpinner(View view) {

        List<WalletGroup> groups = WalletGroup.getAllSortedByName();

        ArrayList<String> groupNames = new ArrayList<>();
        for (WalletGroup group : groups) {
            groupNames.add(group.name);
        }

        mGroupNameSpinner = (Spinner) view.findViewById(R.id.groupSpinner);
        mAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, groupNames);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mGroupNameSpinner.setAdapter(mAdapter);
        mGroupNameSpinner.setOnItemSelectedListener(this);
    }

    /**
     * Updates the form fragment based on spinner selection.
     * isUserInteraction is required because orientation changes cause the fragment to be
     * replaced multiple times and thus any content added to EditText fields is lost.
     */
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        // Handle on item selection
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
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
            integrator.setPrompt("");
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
            mPublicKey.setText(scanResult.getContents()); // add code to edit text field
        }
    }

    /**
     * Adds new single address wallet to the database which includes fetching all tx data.
     * Upon completion calls on WalletCreateActivity to finish.
     */
    public void onSubmit() {

        boolean addressIsValid = SingleAddressWallet.isValidAddress(mPublicKey.getText().toString());
        boolean nameIsEmptyString = mWalletName.getText().toString().equals("");

        if (addressIsValid && !nameIsEmptyString) {

            Spinner walletTypeSpinner = (Spinner) getActivity().findViewById(R.id.walletTypeSpinner);
            String selection = walletTypeSpinner.getSelectedItem().toString().toLowerCase();

            switch (selection) {
                case "single address wallet":

                    // TODO Validate that public key doesn't already exist before adding.

                    Walletx wtx = new Walletx();
                    wtx.name = mWalletName.getText().toString();
                    wtx.type = WalletType.SINGLE_ADDRESS_WALLET;
                    String groupName = mGroupNameSpinner.getSelectedItem().toString();
                    WalletGroup group = WalletGroup.getBy(groupName);
                    wtx.group = group;
                    wtx.save();

                    SingleAddressWallet saWallet = new SingleAddressWallet();
                    saWallet.publicKey = mPublicKey.getText().toString();
                    saWallet.wtx = wtx;
                    saWallet.save();

                    break;
                default:
                    throw new IllegalArgumentException();
            }

            // Notify parent activity
            if (mListener != null) {
                mListener.onFragmentInteraction();
            }

        } else if (!addressIsValid) {

            // Alert user that public key is invalid
            Toast.makeText(getActivity(),
                    R.string.walletx_create_toast_invalid_public_key,
                    Toast.LENGTH_SHORT).show();

        } else if (nameIsEmptyString) {

            // Alert user that name field is required
            Toast.makeText(getActivity(),
                    R.string.walletx_create_toast_name_is_empty_string,
                    Toast.LENGTH_SHORT).show();
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