package com.bitcoin.tracker.walletx.api;

/**
 * Created by michael on 3/30/15.
 */
public class SyncDatabase {

    public SyncDatabase(SyncableInterface caller) {
        if (caller != null) {
            System.out.println("SYNC CALLED");
            //new BlockchainInfo(caller).execute();
        } else {
            //new BlockchainInfo().execute();
        }
    }
}
