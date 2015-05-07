package com.bitcoin.tracker.walletx.activity.group;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.bitcoin.tracker.walletx.R;
import com.bitcoin.tracker.walletx.model.Group;

/**
 * Activity for creating a new Group.
 */
public class GroupCreateActivity extends ActionBarActivity {

    private EditText mGroupName;
    private CheckBox mSetAsDefault;
    private Button   mSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_create_activity);
        setupActionBar();
        getViews();
        bindListeners();
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
        getSupportActionBar().setTitle(R.string.group_create_activity_title_activity);
    }

    private void getViews() {
        mGroupName = (EditText) findViewById(R.id.editTextWalletGroupName);
        mSetAsDefault = (CheckBox) findViewById((R.id.checkBoxSetAsDefault));
        mSubmit = (Button) findViewById(R.id.buttonAddWalletGroup);
    }

    private void bindListeners() {

        mSubmit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String name = mGroupName.getText().toString();
                if (Group.isEmptyString(name)) {
                    String error = getString(R.string.group_create_activity_error_empty_string);
                    Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
                } else if (Group.matchesExisting(name)) {
                    String error = getString(R.string.group_create_activity_error_matches_existing);
                    Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
                } else {
                    Group.create(name, mSetAsDefault.isChecked());
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        }); // mSubmit

    } // bindListeners

} // GroupCreateActivity
