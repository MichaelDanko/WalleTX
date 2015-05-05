package com.bitcoin.tracker.walletx.activity.walletx;

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
import com.bitcoin.tracker.walletx.model.Group;
import com.bitcoin.tracker.walletx.model.SingleAddressWallet;
import com.bitcoin.tracker.walletx.model.Walletx;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages form elements associated with adding a new single address wallet.
 */
public class WalletxCreateSingleAddressWalletFragment extends Fragment
        implements AdapterView.OnItemSelectedListener {

    private Fragment fragment;
    private OnFragmentInteractionListener mListener;
    private EditText mPublicKey;
    private ImageButton mQrCode;
    private EditText mWalletName;
    private Spinner mGroupNameSpinner;
    private Button mSubmit;
    private ArrayAdapter<CharSequence> mAdapter;

    public static WalletxCreateSingleAddressWalletFragment newInstance() {
        return new WalletxCreateSingleAddressWalletFragment();
    }

    public WalletxCreateSingleAddressWalletFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragment = this;
    }

    @Override
    public View onCreateView(LayoutInflater i, ViewGroup c, Bundle savedInstanceState) {
        View view = i.inflate(R.layout.walletx_create_single_address_wallet_fragment, c, false);
        getViews(view);
        bindListeners();
        setupGroupNameSpinner(savedInstanceState);
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

    private void getViews(View view) {
        mPublicKey = (EditText) view.findViewById(R.id.editTextPublicKey);
        mQrCode = (ImageButton) view.findViewById(R.id.scanQrCode);
        mWalletName = (EditText) view.findViewById(R.id.editTextSaWalletName);
        mGroupNameSpinner = (Spinner) view.findViewById(R.id.groupSpinner);
        mSubmit = (Button) view.findViewById(R.id.submitButton);
    }

    private void bindListeners() {
        mQrCode.setOnClickListener(qrCodeListener);
        mSubmit.setOnClickListener(submitButtonListener);
    }

    private void setupGroupNameSpinner(Bundle savedInstanceState) {
        List<Group> groups = Group.getAllSortedByName();
        ArrayList<String> names = new ArrayList<>();
        for (Group group : groups)
            names.add(group.name);
        mAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, names);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mGroupNameSpinner.setAdapter(mAdapter);
        mGroupNameSpinner.setOnItemSelectedListener(this);
        // Set the wallet group spinner to the default wallet group only
        // when activity is first created.
        if (savedInstanceState == null) {
            Group defaultGroup = Group.getDefault();
            int pos = mAdapter.getPosition(defaultGroup.name);
            mGroupNameSpinner.setSelection(pos);
        }
    }

    /**
     * Updates the form fragment based on spinner selection.
     * isUserInteraction is required because orientation changes cause the fragment to be
     * replaced multiple times and thus any content added to EditText fields is lost.
     */
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {}
    public void onNothingSelected(AdapterView<?> parent) {}

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
        if (scanResult != null)
            mPublicKey.setText(scanResult.getContents());
    }

    /**
     * Adds new single address wallet to the database which includes fetching all tx data.
     * Upon completion calls on WalletCreateActivity to finish.
     */
    public boolean onSubmit() {
        String name = mWalletName.getText().toString();
        String address = mPublicKey.getText().toString();
        if (Walletx.isEmptyString(name)) {
            Toast.makeText(getActivity(),
                    R.string.walletx_create_activity_error_empty_string,
                    Toast.LENGTH_SHORT).show();
        } else if (!SingleAddressWallet.isValidAddress(address)) {
            Toast.makeText(getActivity(),
                    R.string.walletx_create_activity_error_invalid_public_key,
                    Toast.LENGTH_SHORT).show();
        } else if (SingleAddressWallet.publicKeyExists(address)) {
            Toast.makeText(getActivity(),
                    R.string.walletx_create_activity_error_existing_public_key,
                    Toast.LENGTH_LONG).show();
        } else if (Walletx.matchesExisting(name)) {
            Toast.makeText(getActivity(),
                    R.string.walletx_create_activity_error_existing_name,
                    Toast.LENGTH_SHORT).show();
        } else {
            Group group = Group.getBy(mGroupNameSpinner.getSelectedItem().toString());
            Walletx.createTypeSingleAddressWallet(name, group, address);
            // Notify parent activity
            if (mListener != null) {
                mListener.onFragmentInteraction(name);
            }
            return true;
        }
        return false;
    } // onSubmit

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(String name_of_wtx_added);
    }

} // WalletxCreateSingleAddressWalletFragment
