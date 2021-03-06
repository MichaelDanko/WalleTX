package com.bitcoin.tracker.walletx.activity.walletx;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.bitcoin.tracker.walletx.R;
import com.bitcoin.tracker.walletx.activity.Constants;
import com.bitcoin.tracker.walletx.activity.SharedData;
import com.bitcoin.tracker.walletx.model.Walletx;

/**
 * Handles the forms for adding new user wallets of various types.
 */
public class WalletxCreateActivity extends ActionBarActivity implements
        AdapterView.OnItemSelectedListener,
        WalletxCreateSingleAddressWalletFragment.OnFragmentInteractionListener {

    // Blocks spinner onItemSelected from firing without user interaction.
    private boolean isUserInteraction = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.walletx_create_activity);
        setupActionBar();
        setupWalletTypeSpinner();
        if (savedInstanceState == null)
            changeWalletTypeFragmentTo(0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    private void setupActionBar(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.walletx_create_activity_title_activity);
    }

    private void setupWalletTypeSpinner() {
        Spinner walletTypeSpinner = (Spinner) findViewById(R.id.walletTypeSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.walletx_create_activity_spinner_supported_wallet_types,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        walletTypeSpinner.setAdapter(adapter);
        walletTypeSpinner.setOnItemSelectedListener(this);
        walletTypeSpinner.setOnTouchListener(spinnerOnTouchListener);
    }

    /**
     * Changes wallet type form fragment that is displayed
     * in response to the spinner selection.
     */
    private void changeWalletTypeFragmentTo(int walletTypeFragmentLayoutResource) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        switch (walletTypeFragmentLayoutResource) {
            case R.layout.walletx_create_single_address_wallet_fragment:
                ft.replace(R.id.walletTypeFragmentContainer,
                        WalletxCreateSingleAddressWalletFragment.newInstance());
                break;
            // case R.layout.fragment_future_wallet_type:
            //     ft.replace(R.id.walletTypeFragmentContainer,
            //             FutureWalletTypeFragment.newInstance());
            //     break;
            default:
                ft.replace(R.id.walletTypeFragmentContainer,
                        WalletxCreateSingleAddressWalletFragment.newInstance());
                break;
        }
        ft.commit();
    }

    private View.OnTouchListener spinnerOnTouchListener = new View.OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event){
            if (event.getAction() == MotionEvent.ACTION_DOWN)
                isUserInteraction = true;
            return false;
        }
    };

    /**
     * Updates the form fragment based on spinner selection.
     * isUserInteraction is required because orientation changes cause the fragment to be
     * replaced multiple times and thus any content added to EditText fields is lost.
     */
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        if (isUserInteraction) {
            int fragment = 0;
            switch (pos) {
                case 0:
                    fragment = R.layout.walletx_create_single_address_wallet_fragment;
                    break;
                // case 1:
                //     fragment = R.layout.fragment_walletx_create_future_wallet_type;
                //     break;
                default:
                    fragment = R.layout.walletx_create_single_address_wallet_fragment;
                    break;
            }
            changeWalletTypeFragmentTo(fragment);
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    /**
     * Closes the activity in response an onSubmit event in a wallet type fragment.
     */
    public void onFragmentInteraction(String name_of_wtx_added) {
        SharedData.ADDED_WTX = Walletx.getBy(name_of_wtx_added);
        Intent intent = new Intent();
        intent.putExtra(Constants.EXTRA_WTX_ADDED, true);
        setResult(RESULT_OK, intent);
        finish();
    }

} // WalletxCreateActivity
