package com.bitcoin.tracker.walletx.activity;

import com.bitcoin.tracker.walletx.model.Walletx;

import java.util.List;

/**
 * Share data across activities.
 */
public class SharedData {

    //
    public static List<Walletx> WTXS_TO_SUMMARIZE;

    //
    public static Walletx WTX_TO_SUMMARIZE;

    // Temporarily holds a Walletx that has just been added.
    public static Walletx ADDED_WTX;

}
