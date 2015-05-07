package com.bitcoin.tracker.walletx.activity.tx;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bitcoin.tracker.walletx.R;
import com.bitcoin.tracker.walletx.activity.Constants;
import com.bitcoin.tracker.walletx.activity.SharedData;
import com.bitcoin.tracker.walletx.activity.SyncableActivity;
import com.bitcoin.tracker.walletx.api.BlockchainInfo;
import com.bitcoin.tracker.walletx.helper.Formatter;
import com.bitcoin.tracker.walletx.model.Category;
import com.bitcoin.tracker.walletx.model.Tx;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Tx Details Activity
 * TODO Move all of this functionality into the TxsActivity in the form of dialogs
 */
public class TxDetailActivity extends SyncableActivity {

    AutoCompleteTextView mTagAutoCompleteTextView;
    ImageView mTagImageView;
    Button mMoreInfoButton;
    TextView confirmTextField;

    // Track if changes has been made to the tag
    private boolean mTagUpdated = false;

    private List<Category> mCategories = Category.getAll();
    private ArrayList<String> mTags = new ArrayList<>();

    @Override
    protected void refreshUi() {
        // TODO This class requires refactoring so that we can reuse code from onCreate
        //      However, if this content is moved in the TxsActivity
        //      this class will simply be deleted.
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tx_detail_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // prevent autofocus on tags autocompletetextview
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getUIViews();
        bindEvents();

        // Setup AutoCompleteTextView
        if ( mCategories != null ) {
            for (Category cat : mCategories)
                mTags.add(cat.name);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1, mTags);
            mTagAutoCompleteTextView.setAdapter(adapter);
        }

        final TextView timeTextField = (TextView) findViewById(R.id.time);
        final TextView dateTextField = (TextView) findViewById(R.id.tx_date);
        confirmTextField = (TextView) findViewById(R.id.textView8);
        final TextView spendReceiveLabel = (TextView) findViewById(R.id.spent_or_received_label);
        final TextView spendReceiveAmount = (TextView) findViewById(R.id.spent_or_received_amount);

        if (SharedData.TX_TO_GET_DETAILS.amountBTC < 0)
            spendReceiveLabel.setText("Spent");
        else
            spendReceiveLabel.setText("Received");

        spendReceiveAmount.setText(Formatter.btcToString(SharedData.TX_TO_GET_DETAILS.amountBTC));
        DateFormat time = new SimpleDateFormat("HH:mm:ss");
        DateFormat date = new SimpleDateFormat("MM/dd/yyyy");
        timeTextField.setText(time.format(SharedData.TX_TO_GET_DETAILS.timestamp));
        dateTextField.setText(date.format(SharedData.TX_TO_GET_DETAILS.timestamp));
        long confs = BlockchainInfo.getLatestBlock(
                getApplicationContext()) - SharedData.TX_TO_GET_DETAILS.block + 1;
        confirmTextField.setText(String.valueOf(confs));

        // populate the autocompletetextview with existing tag for this tx
        if ( SharedData.TX_TO_GET_DETAILS.category != null )
            mTagAutoCompleteTextView.setText(SharedData.TX_TO_GET_DETAILS.category.name);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            if (mTagUpdated) {
                Intent intent = this.getIntent();
                setResult(RESULT_OK, intent);
            }
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void getUIViews() {
        mTagAutoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.tagAutoCompleteTextView);
        mTagImageView = (ImageView) findViewById(R.id.tagImageView);
        mMoreInfoButton = (Button) findViewById(R.id.blockchain_button);
    }

    private void bindEvents() {

        // When the tag AutoCompleteTextView has focus change the image mTagImageView
        // so user knows they must select it in order to commit their tag updates
        mTagAutoCompleteTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mTagImageView.setImageResource(R.drawable.tx_detail_save);
                    mTagImageView.setTag("in_focus");
                }
            }
        });

        // When mTagImageView is selected...
        // If the image tag is 'in_focus' then mTagAutoCompleteTextView has focus
        // and the user is updating. Commit the changes and restore image.
        mTagImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // restore button image, clear tag focus, and hide keyboard
                mTagImageView.setImageResource(R.drawable.tx_detail_tag);
                mTagAutoCompleteTextView.clearFocus();
                InputMethodManager imm = (InputMethodManager)getSystemService(
                        getApplicationContext().INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mTagAutoCompleteTextView.getWindowToken(), 0);
                // commit the tag changes
                if (mTagImageView.getTag() == "in_focus") {
                    String toAdd = mTagAutoCompleteTextView.getText().toString();
                    Category existenceCheck = Category.getBy(toAdd);
                    if ( existenceCheck != null ) {
                        SharedData.TX_TO_GET_DETAILS.category = existenceCheck;
                        SharedData.TX_TO_GET_DETAILS.save();
                        mTagUpdated = true;
                    } else if ( toAdd.equals("") ) {
                        SharedData.TX_TO_GET_DETAILS.category = null;
                        SharedData.TX_TO_GET_DETAILS.save();
                        mTagUpdated = true;
                    } else {
                        Category newCat = new Category();
                        newCat.name = toAdd;
                        newCat.save();
                        SharedData.TX_TO_GET_DETAILS.category = newCat;
                        SharedData.TX_TO_GET_DETAILS.save();
                        mTagUpdated = true;
                    }
                    mTagImageView.setTag("out_of_focus");
                }
            }
        });

        // In browser, open Blockchain.info to the tx detail page for this tx
        mMoreInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent blockchaininfoIntent =
                        new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://blockchain.info/tx/" + SharedData.TX_TO_GET_DETAILS.hash));
                startActivity(blockchaininfoIntent);
            }
        });
    }

}
