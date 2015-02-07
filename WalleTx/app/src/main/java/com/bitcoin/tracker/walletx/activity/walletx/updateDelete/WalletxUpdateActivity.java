package com.bitcoin.tracker.walletx.activity.walletx.updateDelete;


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

import static com.bitcoin.tracker.walletx.model.WalletGroup.getBy;

public class WalletxUpdateActivity extends ActionBarActivity {

    //region FIELDS

    private EditText mWalletxName;
    private String   mCurrentName;
    private Button   mUpdate;
    private Button   mDelete;

    //endregion
    //region ACTIVITY LIFECYCLE

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walletgroup_update);
        setupActionBar();
        getViewsById();
        bindClickEvents();
        //addCurrentGroupNameToEditText();
    }

    private void setupActionBar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.wallet_group_update_title_activity);
    }

    private void getViewsById() {
        mWalletxName = (EditText) findViewById(R.id.editTextWalletxName);
        mUpdate = (Button) findViewById(R.id.buttonUpdateWalletGroup);
        mDelete = (Button) findViewById(R.id.buttonDeleteWalletGroup);
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

                Toast.makeText(getApplicationContext(), "TODO. Update and finish.", Toast.LENGTH_SHORT).show();
                /*
                Walletx walletxBeingUpdated = Walletx.getBy(mCurrentName);
                String nameInEditText = mWalletxName.getText().toString().toLowerCase();
                String nameOfWalletxBeingUpdated = mCurrentName.toLowerCase();
                boolean nameNotChanged = nameInEditText.equals(nameOfWalletxBeingUpdated);

                if (nameNotChanged) {
                    // walletx name not changed. do nothing...
                    finish();
                } else if (validate(nameInEditText)) {
                    // default group name changed. update name only.
                    WalletGroup update = WalletGroup.getBy(mCurrentName);
                    update.updateName(mGroupName.getText().toString());
                    finishWithResultOk();
                }
                */
            }
        });

        //private boolean validate(String walletxName) {
        //    return true;
        //}

        // Delete button functionality.
        mDelete.setOnClickListener(new View.OnClickListener() {

            /**
             * Creates and displays an AlertDialog asking user if they wish to delete the group.
             */
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "TODO. Dialog confirm, delete and finish.", Toast.LENGTH_SHORT).show();
                /*
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage(R.string.wallet_group_update_alert_message_delete);
                builder.setTitle("Delete group '" + mCurrentName + "'?");
                builder.setPositiveButton(R.string.app_confirm_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        WalletGroup toDelete = WalletGroup.getBy(mCurrentName);
                        WalletGroup.deleteGroup(toDelete);
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
                */
            }
        });

    } // bindClickEvents

    /*
    private void addCurrentGroupNameToEditText() {
        Intent intent = getIntent();
        mCurrentName = intent.getStringExtra("wallet_group_name");
        mGroupName.setText(mCurrentName);
    }
    */

    /*
    private void finishWithResultOk() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }
    */

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
