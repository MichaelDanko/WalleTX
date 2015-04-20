package com.bitcoin.tracker.walletx.activity.txDetail;

import android.app.Activity;
import android.content.ComponentName;
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
import android.widget.Toast;

import com.bitcoin.tracker.walletx.R;
import com.bitcoin.tracker.walletx.activity.SyncableInterface;
import com.bitcoin.tracker.walletx.api.SyncDatabase;
import com.bitcoin.tracker.walletx.model.Tx;
import com.bitcoin.tracker.walletx.model.TxCategory;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/*
 * Tx Details Activity
 *
 */
public class TxDetailActivity extends ActionBarActivity implements SyncableInterface {

    AutoCompleteTextView mTagAutoCompleteTextView;
    ImageView mTagImageView;
    Button mMoreInfoButton;
    TextView confirmTextField;
    Tx txDetail =  null;

    // Reference to activity
    static Activity mActivity;

    // displays when sync in progress
    private ProgressBar mSyncProgressBar;

    // Track if changes has been made to the tag
    private boolean mTagUpdated = false;

    private List<TxCategory> mCategories = TxCategory.getAll();
    private ArrayList<String> mTags = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tx_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mActivity = this;

        // prevent autofocus on tags autocompletetextview
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getUIViews();
        bindEvents();

        // Setup AutoCompleteTextView
        if ( mCategories != null ) {
            for (TxCategory cat : mCategories) {
                mTags.add(cat.name);
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mTags);
            mTagAutoCompleteTextView.setAdapter(adapter);
        }

        // Retrieve Extras
        String extras = getIntent().getExtras().getString("hash");
        txDetail = new Tx().getTxByHash(extras);
        final TextView timeTextField = (TextView) findViewById(R.id.time);
        final TextView dateTextField = (TextView) findViewById(R.id.tx_date);
        confirmTextField = (TextView) findViewById(R.id.textView8);
        final TextView spendReceiveLabel = (TextView) findViewById(R.id.spent_or_received_label);
        final TextView spendReceiveAmount = (TextView) findViewById(R.id.spent_or_received_amount);
        if (txDetail.amountBTC < 0) {
            spendReceiveLabel.setText("Spent");
        } else {
            spendReceiveLabel.setText("Received");
        }
        spendReceiveAmount.setText(Long.toString(Math.abs(txDetail.amountBTC) / 100000000));
        DateFormat time = new SimpleDateFormat("HH:mm:ss");
        DateFormat date = new SimpleDateFormat("MM/dd/yyyy");
        timeTextField.setText(time.format(txDetail.timestamp));
        dateTextField.setText(date.format(txDetail.timestamp));
        confirmTextField.setText(Long.toString(txDetail.confirmations));

        // populate the autocompletetextview with existing tag for this tx
        if ( txDetail.category != null ) {
            mTagAutoCompleteTextView.setText(txDetail.category.name);
        }

        // setup sync progress spinner
        mSyncProgressBar = (ProgressBar) findViewById(R.id.syncProgressBar);
        mSyncProgressBar.getIndeterminateDrawable().setColorFilter(Color.GRAY, android.graphics.PorterDuff.Mode.MULTIPLY);
        mSyncProgressBar.setVisibility(View.GONE);
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
                    String toAdd = mTagAutoCompleteTextView.getText().toString();
                    TxCategory existenceCheck = TxCategory.getBy(toAdd);
                    if ( existenceCheck != null ) {
                        System.out.println("####### 1");
                        txDetail.category = existenceCheck;
                        txDetail.save();
                        mTagUpdated = true;
                    } else if ( toAdd.equals("") ) {
                        System.out.println("####### 2");
                        txDetail.category = null;
                        txDetail.save();
                        mTagUpdated = true;
                    } else {
                        System.out.println("####### 3");
                        TxCategory newCat = new TxCategory();
                        newCat.name = toAdd;
                        newCat.save();
                        txDetail.category = newCat;
                        txDetail.save();
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
                Intent blockchaininfoIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.blockchain.info/address/" + txDetail.address));
                startActivity(blockchaininfoIntent);
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
        if (item.getItemId() == R.id.action_sync) {
            new SyncDatabase(this);
            return true;
        } else if (id == android.R.id.home) {
            if (mTagUpdated) {
                Intent intent = this.getIntent();
                setResult(RESULT_OK, intent);
            }
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    //endregion
    //region SYNC

    public void startSyncRelatedUI() {
        // Rotate progress bar
        final ProgressBar pb = (ProgressBar) mActivity.findViewById(R.id.syncProgressBar);
        if ( mActivity != null && pb != null ) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    pb.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    public void stopSyncRelatedUI() {
        // stop progress bar
        final ProgressBar pb = (ProgressBar) mActivity.findViewById(R.id.syncProgressBar);
        if ( mActivity != null && pb != null ) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    pb.setVisibility(View.GONE);
                }
            });
        }
        // Update the number of confirmations
        confirmTextField.setText(Long.toString(txDetail.confirmations));
    }

    //endregion
}
