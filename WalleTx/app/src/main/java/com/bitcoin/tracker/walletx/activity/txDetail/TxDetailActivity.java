package com.bitcoin.tracker.walletx.activity.txDetail;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bitcoin.tracker.walletx.R;

/*
 * Tx Details Activity
 *
 * TODO Save any tag updates before the view is destroyed.
 * We can vastly improve upon this... Time permitting
 *
 */
public class TxDetailActivity extends ActionBarActivity {

    AutoCompleteTextView mTagAutoCompleteTextView;
    ImageView mTagImageView;

    private static final String[] TAGS = new String[] {
            "Pizza","Beer","Movies", "Clothes", "Income", "Shoes", "Dog Food"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tx_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // prevent autofocus on tags autocompletetextview
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mTagAutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.tagAutoCompleteTextView);
        mTagImageView = (ImageView) findViewById(R.id.tagImageView);

        mTagImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTagImageView.setImageResource(R.mipmap.ic_tag);
                mTagAutoCompleteTextView.clearFocus();

                InputMethodManager imm = (InputMethodManager)getSystemService(
                        getApplicationContext().INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mTagAutoCompleteTextView.getWindowToken(), 0);

                /*

                 TODO save the tag update
                 */
            }
        });


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, TAGS);
        mTagAutoCompleteTextView.setAdapter(adapter);

        mTagAutoCompleteTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mTagImageView.setImageResource(R.mipmap.ic_tag_save_update);
                }
            }
        });

    }

    //region OPTIONS MENU

    /**
     * Display the global options menu.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.global, menu);
        return true;
    }

    /**
     * Home button closes the activity.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    //endregion
}
