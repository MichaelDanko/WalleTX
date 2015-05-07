package com.bitcoin.tracker.walletx.activity;

import com.bitcoin.tracker.walletx.model.Tx;
import com.bitcoin.tracker.walletx.model.Walletx;

import java.util.List;

/**
 * Share data across activities.
 * Our ActiveAndroid model classes are not parcelable, so we can use SharedData
 * to pass between activities and fragments.
 */
public class SharedData {

    /**
     * List of Walletxs selected to summarize
     */
    public static List<Walletx> WTXS_TO_SUMMARIZE;

    /**
     * Tx selected to view details
     */
    public static Tx TX_TO_GET_DETAILS;

    /**
     * Temporarily holds a Walletx that has just been added.
     */
    public static Walletx ADDED_WTX;

}
