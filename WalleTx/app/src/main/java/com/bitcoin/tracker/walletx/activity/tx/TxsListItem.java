package com.bitcoin.tracker.walletx.activity.tx;

/**
 * Custom Txs list item
 */
public class TxsListItem {

    String mDate;
    String mCategory;
    String mAmount;
    String mConfirmations;
    String mHash;

    public TxsListItem(String date, String category, String amount, String confs, String hash) {
        super();
        mDate = date;
        mCategory = category;
        mAmount = amount;
        mConfirmations = confs;
        mHash = hash;
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

    public String getmConfirmations() {return mConfirmations; }

    public String getmHash160() {return mHash; }

}
