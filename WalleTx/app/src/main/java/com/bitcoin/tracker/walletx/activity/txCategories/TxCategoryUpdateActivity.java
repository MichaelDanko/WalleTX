package com.bitcoin.tracker.walletx.activity.txCategories;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bitcoin.tracker.walletx.R;
import com.bitcoin.tracker.walletx.model.Tx;
import com.bitcoin.tracker.walletx.model.TxCategory;
import com.bitcoin.tracker.walletx.model.Walletx;

import java.util.List;

public class TxCategoryUpdateActivity extends ActionBarActivity {
    private EditText mCatName;
    private String   mCurrentName;
    private Button   mUpdate;
    private Button   mDelete;
    private TextView mCannotDeleteLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tx_category_update);
        setupActionBar();
        getViewsById();
        addCurrentCategoryNameToEditText();
        bindClickEvents();
    }

    private void setupActionBar(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit Tx Category");
    }

    private void getViewsById(){
        mCatName = (EditText) findViewById(R.id.category_update_edit_text);
        mUpdate = (Button) findViewById(R.id.category_update_button);
        mDelete = (Button) findViewById(R.id.category_delete_button);
    }

    private void addCurrentCategoryNameToEditText(){
        Intent intent = getIntent();
        mCurrentName = intent.getStringExtra("txcategory_name");
        mCatName.setText(mCurrentName);
    }

    private void bindClickEvents(){
        mUpdate.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                TxCategory catBeingUpdated = TxCategory.getBy(mCurrentName);
                String nameInEditText = mCatName.getText().toString();
                boolean nameNotChanged = nameInEditText.equals(mCurrentName);

                if(nameNotChanged){
                    finish();
                } else {
                    // TODO Model stuff in activity. Refactor.
                    if (categoryIsEmpty(nameInEditText)) {
                        Toast.makeText(getApplicationContext(), "Oops! Category cannot be an empty string", Toast.LENGTH_SHORT).show();
                    } else if (categoryAlreadyExists(nameInEditText)) {
                        Toast.makeText(getApplicationContext(), "Oops! Category already exists", Toast.LENGTH_SHORT).show();
                    } else {
                        catBeingUpdated.name = nameInEditText;
                        catBeingUpdated.save();
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finishWithResultOk();
                    }
                }
            }
        });

        mDelete.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage("Are you sure you wish to delete this Tx tag category? This action cannot be undone.");
                builder.setTitle("Delete category '" + mCurrentName + "'?");
                builder.setPositiveButton(R.string.app_confirm_yes, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int id){
                        dialog.dismiss();
                        TxCategory toDelete = TxCategory.getBy(mCurrentName);

                        // TODO Yet another place in activity with code that should be in the models
                        // Remove tag from txs
                        List<Tx> txs = toDelete.txs();
                        for (Tx tx : txs) {
                            tx.category = null;
                            tx.save();
                        }

                        toDelete.delete();
                        finishWithResultOk();
                    }
                });
                builder.setNegativeButton(R.string.app_confirm_no, new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }//end bindClickEvents

    // TODO Move validation to the model
    private boolean categoryIsEmpty(String validate) {
        if (validate.equals(""))
            return true;
        return false;
    }

    private boolean categoryAlreadyExists(String validate) {
        TxCategory existenceCheck = TxCategory.getBy(validate);
        if (existenceCheck != null)
            return true;
        return false;
    }

    private void finishWithResultOk(){
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }
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
