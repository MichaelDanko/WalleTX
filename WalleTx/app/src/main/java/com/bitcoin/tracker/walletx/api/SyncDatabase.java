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
    public SyncDatabase() {
        List<Walletx> wtxs = Walletx.getAll();

        Log.v("Syncing Database"," \n");

        for (Walletx wtx : wtxs) {
            Log.v("Sync Wallets"," \n");
            System.out.printf(
                    "%-20s %-35s %-36s\n",
                    wtx.name,
                    wtx.type,
                    wtx.group);

            List<SingleAddressWallet> saws = SingleAddressWallet.getAll();
            for (SingleAddressWallet saw : saws) {
            Log.v("Sync Saw"," \n");
            System.out.printf(
                    "%-40s %-15s\n",
                    saw.publicKey,
                    saw.wtx.name);
                  new BlockchainInfo(saw.publicKey, wtx).execute();
            }
        }
    }

    public SyncDatabase(SyncableInterface caller) {

        List<Walletx> wtxs = Walletx.getAll();

        Log.v("Syncing Database"," \n");

        for (Walletx wtx : wtxs) {
            Log.v("Sync Wallets"," \n");
            System.out.printf(
                    "%-20s %-35s %-36s\n",
                    wtx.name,
                    wtx.type,
                    wtx.group);

            List<SingleAddressWallet> saws = SingleAddressWallet.getAll();
            for (SingleAddressWallet saw : saws) {
                Log.v("Sync Saw"," \n");
                System.out.printf(
                        "%-40s %-15s\n",
                        saw.publicKey,
                        saw.wtx.name);
                if (caller != null) {
                    new BlockchainInfo(caller, saw.publicKey, wtx).execute();
                } else {
                    new BlockchainInfo(saw.publicKey, wtx).execute();
                }
            }
        }

    }
}
