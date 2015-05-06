package com.bitcoin.tracker.walletx.activity.walletx;

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
import android.widget.Toast;

import com.bitcoin.tracker.walletx.R;
import com.bitcoin.tracker.walletx.activity.Constants;
import com.bitcoin.tracker.walletx.model.Balance;
import com.bitcoin.tracker.walletx.model.Group;
import com.bitcoin.tracker.walletx.model.SingleAddressWallet;
import com.bitcoin.tracker.walletx.model.SupportedWalletType;
import com.bitcoin.tracker.walletx.model.Tx;
import com.bitcoin.tracker.walletx.model.Walletx;

import java.util.ArrayList;
import java.util.List;

public class WalletxUpdateActivity extends ActionBarActivity {

    private EditText mWalletxName;
    private String mCurrentName;
    private Spinner mGroupNameSpinner;
    private ArrayAdapter<CharSequence> mAdapter;
    private Button mUpdate;
    private Button mDelete;
    private Walletx mWalletxBeingUpdated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.walletx_update_activity);
        setupActionBar();
        getViewsById();
        setupGroupNameSpinner();
        bindListeners();
        addCurrentGroupNameToEditText();
        setSpinnerToCurrentGroup();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
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
        List<Group> groups = Group.getAllSortedByName();
        ArrayList<String> groupNames = new ArrayList<>();
        for (Group group : groups)
            groupNames.add(group.name);
        mAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, groupNames);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mGroupNameSpinner.setAdapter(mAdapter);
    }

    private void addCurrentGroupNameToEditText() {
        Intent intent = getIntent();
        mCurrentName = intent.getStringExtra(Constants.EXTRA_WTX_SELECTED_TO_EDIT);
        mWalletxBeingUpdated = Walletx.getBy(mCurrentName);
        mWalletxName.setText(mCurrentName);
    }

    private void setSpinnerToCurrentGroup() {
        Walletx wtx = Walletx.getBy(mCurrentName);
        int pos = mAdapter.getPosition((wtx.group).toString());
        mGroupNameSpinner.setSelection(pos);
    }

    private void bindListeners() {

        mUpdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String name = mWalletxName.getText().toString();
                if (Walletx.isEmptyString(name)) {
                    Toast.makeText(getApplicationContext(),
                            R.string.walletx_update_activity_error_empty_string,
                            Toast.LENGTH_SHORT).show();
                } else if (Walletx.matchesExisting(name, mWalletxBeingUpdated)) {
                    Toast.makeText(getApplicationContext(),
                            R.string.walletx_update_activity_error_matches_existing,
                            Toast.LENGTH_SHORT).show();
                } else {
                    // valid Walletx name
                    mWalletxBeingUpdated.name = name;
                    mWalletxBeingUpdated.save();
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finishWithResultOk();
                }
            }

        }); // mUpdate

        mDelete.setOnClickListener(new View.OnClickListener() {

            /**
             * Creates and displays an AlertDialog asking user if they wish to delete the group.
             */
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage(R.string.walletx_update_alert_message_delete);
                builder.setTitle("Delete wallet '" + mCurrentName + "'?");
                builder.setPositiveButton(R.string.app_confirm_yes,
                        new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        Walletx wtx = Walletx.getBy(mCurrentName);
                        Walletx.delete(wtx);
                        finishWithResultOk();
                    }
                });
                builder.setNegativeButton(R.string.app_confirm_no,
                        new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        }); // mDelete

    } // bindListeners

    private void finishWithResultOk() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

} // WalletxUpdateActivity
