package com.bitcoin.tracker.walletx.activity.category;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bitcoin.tracker.walletx.R;
import com.bitcoin.tracker.walletx.activity.Constants;
import com.bitcoin.tracker.walletx.model.Category;

/**
 * Activity for updating or deleting a Category.
 */
public class CategoryUpdateActivity extends ActionBarActivity {

    private EditText mCategoryName;
    private Category mCategoryBeingUpdated;
    private Button   mUpdate;
    private Button   mDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_update_activity);
        setupActionBar();
        getViews();
        addCategoryBeingUpdatedNameToEditText();
        bindListeners();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    private void setupActionBar(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.category_update_activity_title);
    }

    private void getViews(){
        mCategoryName = (EditText) findViewById(R.id.category_update_edit_text);
        mUpdate = (Button) findViewById(R.id.category_update_button);
        mDelete = (Button) findViewById(R.id.category_delete_button);
    }

    private void addCategoryBeingUpdatedNameToEditText(){
        Intent intent = getIntent();
        String name = intent.getStringExtra(Constants.EXTRA_CATEGORY_SELECTED_TO_EDIT);
        mCategoryBeingUpdated = Category.getBy(name);
        mCategoryName.setText(mCategoryBeingUpdated.toString());
    }

    private void bindListeners(){

        mUpdate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String name = mCategoryName.getText().toString();
                if (Category.isEmptyString(name)) {
                    Toast.makeText(getApplicationContext(),
                            R.string.category_create_activity_error_empty_string,
                            Toast.LENGTH_SHORT).show();
                } else if (Category.matchesExisting(name, mCategoryBeingUpdated)) {
                    Toast.makeText(getApplicationContext(),
                            R.string.category_create_activity_error_matches_existing,
                            Toast.LENGTH_SHORT).show();
                } else {
                    // valid category name
                    mCategoryBeingUpdated.name = mCategoryName.getText().toString();
                    mCategoryBeingUpdated.save();
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finishWithResultOk();
                }
            }
        }); // mUpdate

        mDelete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage(R.string.category_update__activity_delete_dialog_confirmation);
                String deleteThis = getString(R.string.category_update_activity_delete_dialog_title_1);
                String questionMark = getString(R.string.category_update_activity_delete_dialog_title_2);
                builder.setTitle(deleteThis + mCategoryBeingUpdated.toString() + questionMark);
                builder.setPositiveButton(R.string.app_confirm_yes, confirmDeleteListener);
                builder.setNegativeButton(R.string.app_confirm_no, cancelDeleteListener);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }); // mDelete

    } // bindListeners

    private DialogInterface.OnClickListener confirmDeleteListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int id){
            dialog.dismiss();
            Category.delete(mCategoryBeingUpdated);
            finishWithResultOk();
        }
    };

    private DialogInterface.OnClickListener cancelDeleteListener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int id){
            dialog.dismiss();
        }
    };

    private void finishWithResultOk(){
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

} // CategoryUpdateActivity
