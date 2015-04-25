package com.bitcoin.tracker.walletx.activity.group;

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

import com.bitcoin.tracker.walletx.R;
import com.bitcoin.tracker.walletx.model.Group;

import static com.bitcoin.tracker.walletx.model.Group.*;

/**
 * Displays and handles the form associated with updating and deleting
 * WalletGroups from the WTX database.
 */
public class GroupUpdateActivity extends ActionBarActivity {

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
        setContentView(R.layout.group_update_activity);
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

                Group groupBeingUpdated = Group.getBy(mCurrentName);
                String nameInEditText = mGroupName.getText().toString().toLowerCase();
                String nameOfGroupBeingUpdated = mCurrentName.toLowerCase();
                boolean nameNotChanged = nameInEditText.equals(nameOfGroupBeingUpdated);

                // There are quite a few different scenarios for updating groups
                // based on user input.
                // This probably can be cleaned up and be made more concise,
                // but at the moment it is functional and working correctly.
                if (groupBeingUpdated.isDefault() && nameNotChanged) {

                    // default group name not changed. do nothing...
                    finish();

                } else if (groupBeingUpdated.isDefault() && Group.validate(getBaseContext(), nameInEditText)) {

                    // default group name changed. update name only.
                    Group update = Group.getBy(mCurrentName);
                    update.updateName(mGroupName.getText().toString());
                    finishWithResultOk();

                } else if (nameNotChanged && !mSetAsDefault.isChecked() && !groupBeingUpdated.isDefault()) {

                    // not default group and name not changed. do nothing.
                    finish();

                } else if (nameNotChanged && mSetAsDefault.isChecked()) {

                    // not default group and name not changed. set as default.
                    Group update = Group.getBy(mCurrentName);
                    update.updateDefault(true);
                    finishWithResultOk();

                } else if (Group.validate(getBaseContext(), nameInEditText)) {

                    // not default group and name changed. update name and defaultGroup.
                    Group update = Group.getBy(mCurrentName);
                    if (mSetAsDefault.isChecked())
                        update.update(mGroupName.getText().toString(), true);
                    else
                        update.updateName(mGroupName.getText().toString());
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
                builder.setMessage(R.string.wallet_group_update_alert_message_delete);
                builder.setTitle("Delete group '" + mCurrentName + "'?");
                builder.setPositiveButton(R.string.app_confirm_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        Group toDelete = Group.getBy(mCurrentName);
                        Group.deleteGroup(toDelete);
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
        Group group = getBy(mCurrentName);
        if (group.isDefault()) {
            mSetAsDefault.setVisibility(View.GONE);
            mDelete.setVisibility(View.GONE);
        } else {
            mCannotDeleteLabel.setVisibility(View.GONE);
        }
    }

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
