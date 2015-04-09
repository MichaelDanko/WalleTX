package com.bitcoin.tracker.walletx.activity.walletGroup.create;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.bitcoin.tracker.walletx.R;
import com.bitcoin.tracker.walletx.model.QueryModelTest;
import com.bitcoin.tracker.walletx.model.WalletGroup;

/**
 * Displays and handles the form associated with adding
 * new WalletGroups to the WTX database.
 */
public class WalletGroupCreateActivity extends ActionBarActivity {

    //region FIELDS

    private EditText mGroupName;
    private CheckBox mSetAsDefault;
    private Button   mSubmit;

    //endregion
    //region ACTIVITY LIFECYCLE

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
        getSupportActionBar().setTitle(R.string.wallet_group_create_title_activity);
    }

    private void getViewsById() {
        mGroupName = (EditText) findViewById(R.id.editTextWalletGroupName);
        mSetAsDefault = (CheckBox) findViewById((R.id.checkBoxSetAsDefault));
        mSubmit = (Button) findViewById(R.id.buttonAddWalletGroup);
    }

    //endregion
    //region EVENT HANDLING

    private void addSubmitButtonClickListener() {
        mSubmit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String nameEntered = mGroupName.getText().toString().toLowerCase();
                boolean dataIsValid = WalletGroup.validate(getBaseContext(), nameEntered);
                if (dataIsValid) {
                    String name = mGroupName.getText().toString();
                    WalletGroup.createWalletGroup(name, mSetAsDefault.isChecked());

                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }

    //endregion
    //region OPTIONS MENU

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

    //endregion

} // WalletGroupCreateActivity
