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
import android.widget.Toast;

import com.bitcoin.tracker.walletx.R;
import com.bitcoin.tracker.walletx.activity.Constants;
import com.bitcoin.tracker.walletx.model.Group;

/**
 * Activity for updating or deleting a Group.
 */
public class GroupUpdateActivity extends ActionBarActivity {

    private EditText mGroupName;
    private CheckBox mSetAsDefault;
    private Button mUpdate;
    private Button mDelete;
    private TextView mCannotDeleteLabel;
    private Group mGroupBeingUpdated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_update_activity);
        setupActionBar();
        getViews();
        bindListeners();
        addCurrentGroupNameToEditText();
        disableFormElementsForDefaultGroup();
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
        getSupportActionBar().setTitle(R.string.group_update_activity_title_activity);
    }

    private void getViews() {
        mGroupName = (EditText) findViewById(R.id.editTextWalletGroupName);
        mSetAsDefault = (CheckBox) findViewById((R.id.checkBoxSetAsDefault));
        mUpdate = (Button) findViewById(R.id.buttonUpdateWalletGroup);
        mDelete = (Button) findViewById(R.id.buttonDeleteWalletGroup);
        mCannotDeleteLabel = (TextView) findViewById(R.id.labelDefaultGroupCannotBeDeleted);
    }

    private void bindListeners() {

        mUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mGroupName.getText().toString();
                if (Group.isEmptyString(name)) {
                    Toast.makeText(getApplicationContext(),
                            R.string.group_create_activity_error_empty_string,
                            Toast.LENGTH_SHORT).show();
                } else if (Group.matchesExisting(name, mGroupBeingUpdated)) {
                    Toast.makeText(getApplicationContext(),
                            R.string.group_create_activity_error_matches_existing,
                            Toast.LENGTH_SHORT).show();
                } else {
                    // valid Group name
                    mGroupBeingUpdated.name = name;
                    mGroupBeingUpdated.save();
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finishWithResultOk();
                }
            }
        }); // mUpdate

        mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage(R.string.group_update_activity_delete_dialog_confirmation);
                String deleteThis = getString(R.string.group_update_activity_delete_dialog_title_1);
                String questionMark = getString(R.string.group_update_activity_delete_dialog_title_2);
                builder.setTitle(deleteThis + mGroupBeingUpdated + questionMark);
                builder.setPositiveButton(R.string.app_confirm_yes, confirmDeleteListener);
                builder.setNegativeButton(R.string.app_confirm_no, cancelDeleteListener);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    } // bindListeners

    private DialogInterface.OnClickListener confirmDeleteListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int id){
            dialog.dismiss();
            Group.delete(mGroupBeingUpdated);
            finishWithResultOk();
        }
    };

    private DialogInterface.OnClickListener cancelDeleteListener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int id){
            dialog.dismiss();
        }
    };

    private void addCurrentGroupNameToEditText() {
        Intent intent = getIntent();
        String name = intent.getStringExtra(Constants.EXTRA_GROUP_SELECTED_TO_EDIT);
        mGroupBeingUpdated = Group.getBy(name);
        mGroupName.setText(mGroupBeingUpdated.toString());
    }

    private void disableFormElementsForDefaultGroup() {
        if (mGroupBeingUpdated.isDefault()) {
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

} // GroupUpdateActivity
