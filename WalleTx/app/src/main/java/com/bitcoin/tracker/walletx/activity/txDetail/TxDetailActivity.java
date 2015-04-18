package com.bitcoin.tracker.walletx.activity.txDetail;

import android.content.ComponentName;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.bitcoin.tracker.walletx.R;
import com.bitcoin.tracker.walletx.model.Tx;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/*
 * Tx Details Activity
 *
 * TODO Update all views with information from the model
 * TODO Don't forget to change Spent/Received label to either Spent of Received
 *
 */
public class TxDetailActivity extends ActionBarActivity {

    AutoCompleteTextView mTagAutoCompleteTextView;
    ImageView mTagImageView;
    Button mMoreInfoButton;
    Tx txDetail =  null;
    // TODO Remove once real data is functional
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
        getUIViews();
        bindEvents();
        // Setup AutoCompleteTextView
        // TODO Prepare tag data for the AutoComplete ArrayAdapter and remove dummy array
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, TAGS);
        mTagAutoCompleteTextView.setAdapter(adapter);


        // Retrieve Extras
        String extras = getIntent().getExtras().getString("hash");
        txDetail = new Tx().getTxByHash(extras);
        final TextView timeTextField = (TextView) findViewById(R.id.time);
        final TextView dateTextField = (TextView) findViewById(R.id.tx_date);
        final TextView confirmTextField = (TextView) findViewById(R.id.textView8);
        final TextView txIDField = (TextView) findViewById(R.id.textView10);
        final TextView spendReceiveLabel = (TextView) findViewById(R.id.spent_or_received_label);
        final TextView spendReceiveAmount = (TextView) findViewById(R.id.spent_or_received_amount);
        if (txDetail.amountBTC < 0) {
            spendReceiveLabel.setTextColor(0xFFFF0000);
            spendReceiveLabel.setText("Spent");
        } else {
            spendReceiveLabel.setTextColor(0xFF00FF00);
            spendReceiveLabel.setText("Received");
        }
        spendReceiveAmount.setText(Long.toString(Math.abs(txDetail.amountBTC)));
        DateFormat time = new SimpleDateFormat("HH:mm:ss");
        DateFormat date = new SimpleDateFormat("MM/dd/yyyy");
        timeTextField.setText(time.format(txDetail.timestamp));
        dateTextField.setText(date.format(txDetail.timestamp));
        confirmTextField.setText(Long.toString(txDetail.confirmations));
        txIDField.setText(txDetail.hash);
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
                    mTagImageView.setImageResource(R.mipmap.ic_tag_save_update);
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
                mTagImageView.setImageResource(R.mipmap.ic_tag);
                mTagAutoCompleteTextView.clearFocus();
                InputMethodManager imm = (InputMethodManager)getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mTagAutoCompleteTextView.getWindowToken(), 0);
                // commit the tag changes
                if (mTagImageView.getTag() == "in_focus") {
                    // TODO Implement
                    Toast.makeText(getApplicationContext(), "TODO: Save tag updates or set as uncategorized", Toast.LENGTH_SHORT).show();
                    mTagImageView.setTag("out_of_focus");
                }
            }
        });

        // In browser, open Blockchain.info to the tx detail page for this tx
        mMoreInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent blockchaininfoIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.blockchain.info/address/" + txDetail.address));
                blockchaininfoIntent.setComponent(new ComponentName("com.android.browser","com.android.browser.BrowserActivity"));
                blockchaininfoIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(blockchaininfoIntent);
            }
        });

    }

    /**
     * Commits tag updates to the database if the user didn't manually.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        // commit the tag changes
        if (mTagImageView.getTag() == "in_focus") {
            // TODO Implement
            Toast.makeText(getApplicationContext(), "TODO: On destroy, save tag updates or set as uncategorized", Toast.LENGTH_SHORT).show();
        }
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
