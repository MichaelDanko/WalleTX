package com.bitcoin.tracker.walletx.activity.addWallet;

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
public class AddWalletActivity extends ActionBarActivity
        implements AdapterView.OnItemSelectedListener {

    Spinner walletTypeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_wallet);
        setTitle(R.string.add_wallet_activity_title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupWalletTypeSpinner();
    }

    private void setupWalletTypeSpinner() {
        walletTypeSpinner = (Spinner) findViewById(R.id.walletTypeSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.supported_wallet_type_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        walletTypeSpinner.setAdapter(adapter);
        walletTypeSpinner.setOnItemSelectedListener(this);
    }

    // Updates the form based on spinner selection.
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        // An item was selected.
        // You can retrieve the selected item using parent.getItemAtPosition(pos)
        Toast toast = Toast.makeText(this, "TODO: Update form based on spinner",Toast.LENGTH_SHORT); toast.show();
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.global, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
             finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
