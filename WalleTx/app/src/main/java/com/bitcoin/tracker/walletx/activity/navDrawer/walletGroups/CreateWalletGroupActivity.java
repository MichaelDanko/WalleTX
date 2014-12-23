package com.bitcoin.tracker.walletx.activity.navDrawer.walletGroups;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.bitcoin.tracker.walletx.R;
import com.bitcoin.tracker.walletx.model.wallet.WalletGroup;

import java.util.List;

/**
 * Displays and handles the form associated with adding
 * new WalletGroups to the WTX database.
 */
public class CreateWalletGroupActivity extends ActionBarActivity {

    private EditText mGroupName;
    private CheckBox mSetAsDefault;
    private Button   mSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walletgroup_create);
        setupActionBar();
        getViewsById();
        addSubmitButtonClickListener();
    }

    private void setupActionBar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_activity_add_wallet_group);
    }

    private void getViewsById() {
        mGroupName = (EditText) findViewById(R.id.editTextWalletGroupName);
        mSetAsDefault = (CheckBox) findViewById((R.id.checkBoxSetAsDefault));
        mSubmit = (Button) findViewById(R.id.buttonAddWalletGroup);
    }

    private void addSubmitButtonClickListener() {
        mSubmit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (validateGroupName()) {
                    addGroupToDatabase();
                    finish();
                }
            }
        });
    }

    /**
     * Validates that the wallet group name entered does not already exist
     * and is not an empty string.
     */
    private boolean validateGroupName() {
        List<WalletGroup> groups = WalletGroup.getAll();
        String nameEntered = mGroupName.getText().toString().toLowerCase();

        // Cannot be empty string.
        if (nameEntered.isEmpty()) {
            String error = getString(R.string.error_wallet_group_empty);
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
            return false;
        }

        // Cannot already exist in the database.
        for (WalletGroup group : groups) {
            if (group.name.toLowerCase().equals(nameEntered)) {
                String error = getString(R.string.error_wallet_group_exists);
                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
                return false;
            }
        }
        return true;
    }

    private void addGroupToDatabase() {
        WalletGroup newGroup = new WalletGroup();
        newGroup.name = mGroupName.getText().toString();

        // Set group as default.
        if (mSetAsDefault.isChecked()) {
            List<WalletGroup> existingGroups = WalletGroup.getAll();
            for (WalletGroup group : existingGroups) {
                if (group.getDefaultGroup() == 1) {
                    group.setAsDefault(0);
                    group.save();
                }
            }
            newGroup.setAsDefault(1);
        } else {
            newGroup.setAsDefault(0);
        }

        // Set group display order and save.
        WalletGroup last = WalletGroup.getLast();
        newGroup.displayOrder = last.displayOrder + 1;
        newGroup.save();
    }

    /**
     * Closes activity when the home button is selected.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

} // CreateWalletGroupActivity
