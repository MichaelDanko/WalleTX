package com.bitcoin.tracker.walletx.activity.walletGroup;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bitcoin.tracker.walletx.R;
import com.bitcoin.tracker.walletx.model.WalletGroup;
import com.bitcoin.tracker.walletx.model.Walletx;

import java.util.List;

import static com.bitcoin.tracker.walletx.model.WalletGroup.*;

/**
 * Displays and handles the form associated with updating and deleting
 * WalletGroups from the WTX database.
 */
public class WalletGroupUpdateActivity extends ActionBarActivity {

    //region FIELDS

    private EditText mGroupName;
    private String   mCurrentName;
    private CheckBox mSetAsDefault;
    private Button   mUpdate;
    private Button   mDelete;
    private TextView mCannotDeleteLabel;

    //endregion
    //region ACTIVITY LIFECYCLE

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walletgroup_update);
        setupActionBar();
        getViewsById();
        bindClickEvents();
        addCurrentGroupNameToEditText();
        disableElementsForDefaultGroup();
    }

    private void setupActionBar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.wallet_group_update_title_activity);
    }

    private void getViewsById() {
        mGroupName = (EditText) findViewById(R.id.editTextWalletGroupName);
        mSetAsDefault = (CheckBox) findViewById((R.id.checkBoxSetAsDefault));
        mUpdate = (Button) findViewById(R.id.buttonUpdateWalletGroup);
        mDelete = (Button) findViewById(R.id.buttonDeleteWalletGroup);
        mCannotDeleteLabel = (TextView) findViewById(R.id.labelDefaultGroupCannotBeDeleted);
    }

    //endregion
    //region EVENT HANDLING

    private void bindClickEvents() {

        // Update button functionality.
        mUpdate.setOnClickListener(new View.OnClickListener() {

            /**
             * Updates the group name and default value before finishing this activity.
             */
            @Override
            public void onClick(View v) {
                String nameEntered = mGroupName.getText().toString().toLowerCase();
                String nameExisting = mCurrentName.toLowerCase();

                if (nameEntered.equals(nameExisting)) {
                    // Update default field only.
                    WalletGroup group = getBy(mCurrentName);
                    if (mSetAsDefault.isChecked()) {
                        WalletGroup currentDefault = getDefault();
                        currentDefault.setAsDefault(0);
                        currentDefault.save();
                        group.setAsDefault(1);
                    }
                    group.save();
                    finish();
                } else if (validateGroupName()) {
                    // Update name and default field.
                    WalletGroup group = getBy(mCurrentName);
                    group.name = mGroupName.getText().toString();
                    if (mSetAsDefault.isChecked()) {
                        WalletGroup currentDefault = getDefault();
                        currentDefault.setAsDefault(0);
                        currentDefault.save();
                        group.setAsDefault(1);
                    }
                    group.save();
                    finish();
                }
            }
        });

        // Delete button functionality.
        mDelete.setOnClickListener(new View.OnClickListener() {

            /**
             * Creates and displays an AlertDialog asking user if they wish to delete the group.
             */
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage(R.string.wallet_group_update_alert_message_delete);
                builder.setTitle("Delete group '" + mCurrentName + "'?");
                builder.setPositiveButton(R.string.app_confirm_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        deleteWalletGroup();
                    }
                });
                builder.setNegativeButton(R.string.app_confirm_no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    } // bindClickEvents

    /**
     * Validates that the wallet group name entered does not already exist
     * and is not an empty string.
     */
    private boolean validateGroupName() {
        List<WalletGroup> groups = WalletGroup.getAll();
        String nameEntered = mGroupName.getText().toString().toLowerCase();

        // Cannot be empty string.
        if (nameEntered.isEmpty()) {
            String error = getString(R.string.wallet_group_create_toast_name_empty);
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
            return false;
        }

        // Cannot already exist in the database.
        for (WalletGroup group : groups) {
            if (group.name.toLowerCase().equals(nameEntered)) {
                String error = getString(R.string.wallet_group_create_toast_name_exists);
                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
                return false;
            }
        }
        return true;
    }

    private void deleteWalletGroup() {
        WalletGroup delete = getBy(mCurrentName);

        // TODO Verify that this functionality is implemented correctly once Walletx implementation is complete.
        List<Walletx> wallets = Walletx.getAll();
        WalletGroup defaultGroup = WalletGroup.getDefault();
        for (Walletx wtx : wallets) {
            if (wtx.group == delete) {
                wtx.group = defaultGroup;
                wtx.save();
            }
        }

        // Change the display order of groups after the deleted group
        List<WalletGroup> groups = WalletGroup.getAllWithDisplayOrderGreaterThan(delete.displayOrder);
        for (WalletGroup group : groups) {
            group.displayOrder = group.displayOrder - 1;
            group.save();
        }

        delete.delete();
        finish();
    }

    private void addCurrentGroupNameToEditText() {
        Intent intent = getIntent();
        mCurrentName = intent.getStringExtra("wallet_group_name");
        mGroupName.setText(mCurrentName);
    }

    /**
     * Modifies form elements that should not be available
     * for this wallet group.
     */
    private void disableElementsForDefaultGroup() {
        WalletGroup group = getBy(mCurrentName);
        if (group.isDefault()) {
            mSetAsDefault.setVisibility(View.GONE);
            mDelete.setVisibility(View.GONE);
        } else {
            mCannotDeleteLabel.setVisibility(View.GONE);
        }
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
}
