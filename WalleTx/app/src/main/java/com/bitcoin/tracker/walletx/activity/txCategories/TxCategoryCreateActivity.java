package com.bitcoin.tracker.walletx.activity.txCategories;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.bitcoin.tracker.walletx.R;
import com.bitcoin.tracker.walletx.model.TxCategory;

/*

  TODO Validate input (no empty tags)
  TODO Add the tag


 */
public class TxCategoryCreateActivity extends ActionBarActivity {

    private EditText mCatName;
    private Button mSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_tx_category);
        setupActionBar();
        getViewsById();
        addSubmitButtonClickListener();
    }

    private void setupActionBar(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Add New Category");
    }

    private void getViewsById(){
        mCatName = (EditText) findViewById(R.id.category_create_edit_text);
        mSubmit = (Button) findViewById(R.id.category_create_button);
    }

    //region OPTIONS MENU
    private void addSubmitButtonClickListener(){
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameEntered = mCatName.getText().toString().toLowerCase();
                String name = mCatName.getText().toString();
                TxCategory.createTxCategory(name, false);

                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });
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

    //endregion

}
