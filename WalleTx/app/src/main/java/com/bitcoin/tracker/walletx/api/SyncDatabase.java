package com.bitcoin.tracker.walletx.api;

import android.util.Log;

import com.bitcoin.tracker.walletx.activity.SyncableInterface;
import com.bitcoin.tracker.walletx.model.SingleAddressWallet;
import com.bitcoin.tracker.walletx.model.Walletx;

import java.util.List;

/**
 * Created by michael on 3/30/15.
 */
public class SyncDatabase {

    public SyncDatabase(SyncableInterface caller) {
    List<Walletx> wtxs = Walletx.getAll();
    for (Walletx wtx : wtxs) {
            if (caller != null) {
                new BlockchainInfo(caller, wtx).execute();
            } else {
                new BlockchainInfo(wtx).execute();
            }
        }
    }
}
