package com.bitcoin.tracker.walletx.activity.category;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bitcoin.tracker.walletx.R;
import com.bitcoin.tracker.walletx.model.Category;

/**
 * Activity for creating a new Category.
 */
public class CategoryCreateActivity extends ActionBarActivity {

    private EditText mCategoryName;
    private Button mSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_create_activity);
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

    private void setupActionBar(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.category_create_activity_title);
    }

    private void getViews(){
        mCategoryName = (EditText) findViewById(R.id.category_create_edit_text);
        mSubmit = (Button) findViewById(R.id.category_create_button);
    }

    private void bindListeners(){

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mCategoryName.getText().toString();
                if (Category.isEmptyString(name)) {
                    Toast.makeText(getApplicationContext(),
                            R.string.category_create_activity_error_empty_string,
                            Toast.LENGTH_SHORT).show();
                } else if (Category.matchesExisting(name)) {
                    Toast.makeText(getApplicationContext(),
                            R.string.category_create_activity_error_matches_existing,
                            Toast.LENGTH_SHORT).show();
                } else {
                    // valid category name
                    Category.create(name);
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        }); // mSubmit

    } // bindListeners

} // CategoryCreateActivity
