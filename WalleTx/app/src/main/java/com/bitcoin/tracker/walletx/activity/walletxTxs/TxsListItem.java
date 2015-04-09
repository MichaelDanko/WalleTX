package com.bitcoin.tracker.walletx.activity.walletxTxs;

/**
 * Custom Txs list item
 */
public class TxsListItem {

    String mDate;
    String mCategory;
    String mAmount;
    String mConfirmations;

    public TxsListItem(String date, String category, String amount, String confs) {
        super();
        mDate = date;
        mCategory = category;
        mAmount = amount;
        mConfirmations = confs;
    }

    public String getmDate() {
        return mDate;
    }

    public String getmCategory() {
        return mCategory;
    }

    public String getmAmount() {
        return mAmount;
    }

    public String getmConfirmations() {
        return mConfirmations;
    }

}
