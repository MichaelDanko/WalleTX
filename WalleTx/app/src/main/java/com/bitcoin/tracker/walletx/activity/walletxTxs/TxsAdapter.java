package com.bitcoin.tracker.walletx.activity.walletxTxs;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bitcoin.tracker.walletx.R;

import java.util.ArrayList;

/**
 * Populates custom list items in the TxsActivity list view.
 */
public class TxsAdapter extends ArrayAdapter<TxsListItem> {

    //region FIELDS

    private final Activity activity;
    private ArrayList<TxsListItem> mItemsArrayList;

    private View mRowView;
    private LayoutInflater inflater;

    private TextView mDate;
    private TextView mCategory;
    private TextView mAmount;
    private TextView mConfirmations;

    //endregion
    //region ADAPTER

    public TxsAdapter(Activity activity, ArrayList<TxsListItem> itemsArrayList) {
        super(activity, R.layout.fragment_walletx_txs_list_item, itemsArrayList);
        this.activity = activity;
        this.mItemsArrayList = itemsArrayList;
    }

    public void updateData(ArrayList<TxsListItem> itemsArrayList) {
        this.mItemsArrayList = itemsArrayList;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        getViewsById(parent);
        setupTextLabels(position);
        return mRowView;
    }

    private void getViewsById(ViewGroup parent) {
        mRowView = inflater.inflate(R.layout.fragment_walletx_txs_list_item, parent, false);
        mDate = (TextView) mRowView.findViewById(R.id.txs_date);
        mCategory = (TextView) mRowView.findViewById(R.id.txs_category);
        mAmount = (TextView) mRowView.findViewById(R.id.txs_amount);
        mConfirmations = (TextView) mRowView.findViewById(R.id.txs_confirmations);
    }

    private void setupTextLabels(int position) {
        mDate.setText(mItemsArrayList.get(position).getmDate());
        mCategory.setText(mItemsArrayList.get(position).getmCategory());
        mAmount.setText(mItemsArrayList.get(position).getmAmount());
        mConfirmations.setText(mItemsArrayList.get(position).getmConfirmations());
    }

    //endregion

}
