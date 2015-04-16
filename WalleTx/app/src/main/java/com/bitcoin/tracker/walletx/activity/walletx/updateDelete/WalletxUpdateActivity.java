package com.bitcoin.tracker.walletx.activity.walletx.updateDelete;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.bitcoin.tracker.walletx.R;
import com.bitcoin.tracker.walletx.model.SingleAddressWallet;
import com.bitcoin.tracker.walletx.model.Tx;
import com.bitcoin.tracker.walletx.model.WalletGroup;
import com.bitcoin.tracker.walletx.model.WalletType;
import com.bitcoin.tracker.walletx.model.Walletx;

import java.util.ArrayList;
import java.util.List;

public class WalletxUpdateActivity extends ActionBarActivity {

    //region FIELDS

    private EditText mWalletxName;
    private String mCurrentName;
    private Spinner mGroupNameSpinner;
    private ArrayAdapter<CharSequence> mAdapter;
    private Button mUpdate;
    private Button mDelete;

    //endregion
    //region ACTIVITY LIFECYCLE

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walletx_update);
        setupActionBar();
        getViewsById();
        setupGroupNameSpinner();
        bindClickEvents();
        addCurrentGroupNameToEditText();
        setSpinnerToCurrentGroup();
    }

    private void setupActionBar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.walletx_update_title_activity);
    }

    private void getViewsById() {
        mWalletxName = (EditText) findViewById(R.id.editTextWalletxName);
        mGroupNameSpinner = (Spinner) findViewById(R.id.groupSpinner);
        mUpdate = (Button) findViewById(R.id.buttonUpdateWalletx);
        mDelete = (Button) findViewById(R.id.buttonDeleteWalletx);
    }

    private void setupGroupNameSpinner() {
        List<WalletGroup> groups = WalletGroup.getAllSortedByName();
        ArrayList<String> groupNames = new ArrayList<>();
        for (WalletGroup group : groups) {
            groupNames.add(group.name);
        }
        mAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, groupNames);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mGroupNameSpinner.setAdapter(mAdapter);
    }

    private void addCurrentGroupNameToEditText() {
        Intent intent = getIntent();
        mCurrentName = intent.getStringExtra("walletx_name");
        mWalletxName.setText(mCurrentName);
    }

    private void setSpinnerToCurrentGroup() {
        Walletx wtx = Walletx.getBy(mCurrentName);
        int pos = mAdapter.getPosition((wtx.group).toString());
        mGroupNameSpinner.setSelection(pos);
    }

    //endregion
    //region EVENT HANDLING

    private void bindClickEvents() {

        // Update button functionality.
        mUpdate.setOnClickListener(new View.OnClickListener() {

            /**
             * Updates the walletx name
             */
            @Override
            public void onClick(View v) {

                Walletx walletBeingUpdated = Walletx.getBy(mCurrentName);
                String nameInEditText = mWalletxName.getText().toString().toLowerCase();
                String nameOfWalletxBeingUpdated = mCurrentName.toLowerCase();
                boolean nameNotChanged = nameInEditText.equals(nameOfWalletxBeingUpdated);
                String groupSelected = mGroupNameSpinner.getSelectedItem().toString();
                boolean groupNotChanged = groupSelected.equals(walletBeingUpdated.group.toString());

                if (nameNotChanged && groupNotChanged) {
                    // do nothing ...
                    finish();
                } else if (nameNotChanged && !groupNotChanged) {
                    // update group only ...

                    /*
                     * TODO @dc @as Refactor this into Walletx model
                     *
                     */
                    walletBeingUpdated.group = WalletGroup.getBy(groupSelected);
                    walletBeingUpdated.save();


                    finishWithResultOk();

                /*
                 * TODO @dc @as Add static Walletx method to validate name. Validation should
                 *              ensure the name is unique and not an empty string. Any invalid data
                 *              should display a toast message regarding error and return false.
                 *              Replace 'true' below with method call, something like
                 *              Walletx.validateName(nameInEditText);
                 *
                 *              Note: I've already added to strings to the string.xml file
                 *              for being displayed in toast messages
                 */
                } else if (true) {
                    // name was changed and it is valid. update name and group.

                    /*
                     * TODO @ dc @as Refactor this code into a Walletx update method
                     *               Walletx should handle the name, group update and save.
                     *
                     */
                    walletBeingUpdated.name = mWalletxName.getText().toString();
                    WalletGroup group = WalletGroup.getBy(groupSelected);
                    walletBeingUpdated.group = group;
                    walletBeingUpdated.save();


                    finishWithResultOk();
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
                builder.setMessage(R.string.walletx_update_alert_message_delete);
                builder.setTitle("Delete wallet '" + mCurrentName + "'?");
                builder.setPositiveButton(R.string.app_confirm_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();

                        /*
                         * TODO @dc @as Refactor this delete into the Walletx model. Deleting a Walletx must also do many things such as:
                         *              Delete any SAW or other WalletType associated with the Walletx
                         *              Delete all tx's associated with the wallet
                         *              Delete all balances, etc..
                         *
                         */
                        Walletx wtx = Walletx.getBy(mCurrentName);

                        List<Tx> txs = wtx.txs();
                        for ( Tx tx : txs ) {
                            tx.delete();
                        }

                        if (wtx.type == WalletType.SINGLE_ADDRESS_WALLET) {
                            SingleAddressWallet saw = SingleAddressWallet.getByWalletx(wtx);
                            saw.delete();
                        }

                        wtx.delete();

                        // Return to parent activity
                        finishWithResultOk();
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

    private void finishWithResultOk() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
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
