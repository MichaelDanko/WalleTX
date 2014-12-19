package com.bitcoin.tracker.walletx.activity.navDrawer.walletGroups;

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

import com.bitcoin.tracker.walletx.R;
import com.bitcoin.tracker.walletx.model.wallet.WalletGroup;

import java.util.List;

import static com.bitcoin.tracker.walletx.model.wallet.WalletGroup.*;

public class EditWalletGroupActivity extends ActionBarActivity {

    private EditText mGroupName;
    private String   mCurrentName;
    private CheckBox mSetAsDefault;
    private Button   mUpdate;
    private Button   mDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_wallet_group);
        setupActionBar();
        getViewsById();
        bindClickEvents();
        addCurrentGroupNameToEditText();
        disableElementsForDefaultGroup();
    }

    private void setupActionBar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.title_activity_edit_wallet_group);
    }

    private void getViewsById() {
        mGroupName = (EditText) findViewById(R.id.editTextWalletGroupName);
        mSetAsDefault = (CheckBox) findViewById((R.id.checkBoxSetAsDefault));
        mUpdate = (Button) findViewById(R.id.buttonUpdateWalletGroup);
        mDelete = (Button) findViewById(R.id.buttonDeleteWalletGroup);
    }

    private void bindClickEvents() {

        mUpdate.setOnClickListener(new View.OnClickListener() {

            /**
             * Updated the group name and default value before
             * finishing this activity.
             */
            @Override
            public void onClick(View v) {
                WalletGroup group = getBy(mCurrentName);
                group.name = mGroupName.getText().toString();
                // Update default. Current default must be set to 0.
                if (mSetAsDefault.isChecked()) {
                    WalletGroup currentDefault = getDefault();
                    currentDefault.setAsDefault(0);
                    currentDefault.save();
                    group.setAsDefault(1);
                }
                group.save();
                finish();
            }
        });

        mDelete.setOnClickListener(new View.OnClickListener() {

            /**
             * Creates and displays an AlertDialog
             * asking user if they wish to delete the group.
             */
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage(R.string.alert_message_delete_wallet_group);
                builder.setTitle("Delete group '" + mCurrentName + "'?");
                builder.setPositiveButton(R.string.confirm_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        deleteWalletGroup();
                    }
                });
                builder.setNegativeButton(R.string.confirm_no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    } // bindClickEvents

    private void deleteWalletGroup() {
        WalletGroup delete = getBy(mCurrentName);

        //-------------------------------------------------------------------------------
        // TODO Change the group of any Walletx's under this group to the default group.
        //-------------------------------------------------------------------------------

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
     * Modifies form elements that should not be available to
     * the default wallet group.
     */
    private void disableElementsForDefaultGroup() {
        WalletGroup group = getBy(mCurrentName);
        if (group.isDefault()) {
            mSetAsDefault.setEnabled(false);
            mSetAsDefault.setText(R.string.checkbox_current_default_group);
            mDelete.setVisibility(View.INVISIBLE);
        }
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
}
