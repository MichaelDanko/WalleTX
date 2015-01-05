package com.bitcoin.tracker.walletx.activity.walletx.create;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.bitcoin.tracker.walletx.R;

/**
 * Handles the form for adding new user wallets.
 */
public class WalletxCreateActivity extends ActionBarActivity implements AdapterView.OnItemSelectedListener,
        WalletxCreateSingleAddressWalletFragment.OnFragmentInteractionListener {

    //region FIELDS

    Spinner walletTypeSpinner;

    //endregion
    //region ACTIVITY LIFECYCLE

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walletx_create);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_activity_add_wallet);
        setupWalletTypeSpinner();
    }

    private void setupWalletTypeSpinner() {
        walletTypeSpinner = (Spinner) findViewById(R.id.walletTypeSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_supported_wallet_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        walletTypeSpinner.setAdapter(adapter);
        walletTypeSpinner.setOnItemSelectedListener(this);
    }

    //endregion
    //region EVENT HANDLING

    // Updates the form based on spinner selection.
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        if ( pos == 0 ) {
            // Single address wallet
            Toast toast = Toast.makeText(this, "TODO: Add Single Address Wallet Form",Toast.LENGTH_SHORT); toast.show();

        } else if ( pos == 1 ) {
            // Single address wallet
            Toast toast = Toast.makeText(this, "TODO: Form should swap out.",Toast.LENGTH_SHORT); toast.show();
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    public void onFragmentInteraction(Uri uri) {
        Toast toast = Toast.makeText(this, "Wheeee!",Toast.LENGTH_SHORT); toast.show();
    }

    //endregion
    //region OPTIONS MENU

    /**
     * Display the global options menu.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.global, menu);
        return true;
    }

    /**
     * Home button closes the activity.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    //endregion
}
