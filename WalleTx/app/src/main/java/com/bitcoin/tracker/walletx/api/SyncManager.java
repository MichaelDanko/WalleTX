package com.bitcoin.tracker.walletx.api;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.ProgressBar;

import com.bitcoin.tracker.walletx.model.Walletx;

import java.util.List;

/**
 * SyncManager is responsible for managing all interactions with the Blockchain and/or
 * other APIs and reporting progress to the SyncableActivity.
 *
 * Although it is possible to use this class directly, it has been implemented with
 * several static methods that should be used to initiate a sync.
 */
public class SyncManager extends AsyncTask<Void, Integer, Boolean> {

    private Context mContext; // reference to context of caller
    private static boolean sSyncIsInProgress = false;  // blocks multiple syncs

    //------------------------------------------------------------------//
    //  SyncManager API                                                 //
    //  Use these methods rather than invoking SyncManager directly     //
    //------------------------------------------------------------------//

    /**
     * Syncs all new transactions for all existing wallets.
     * @param context
     */
    public static void syncExistingWallets(Context context) {
        if (!sSyncIsInProgress)
            new SyncManager(context).execute();
    }

    /**
     * Syncs all transactions for a wallet just added.
     * @param context
     * @param wtx
     */
    public static void syncNewWallet(Context context, Walletx wtx) {
        // TODO Implemente
    }

    /**
     * @return true if a sync of any type is already in progress
     */
    public static boolean syncIsInProgress() {
        if (sSyncIsInProgress)
            return true;
        else
            return false;
    }

    // end SyncManager API -----

    public SyncManager(Context context) {
        super();
        mContext = context;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        sSyncIsInProgress = true;

        // get list of all walletxs
        //List<Walletx> wtxs = Walletx.getAll();

        //BlockchainInfo blockchainInfo = new BlockchainInfo(wtxs);
        //boolean done = blockchainInfo.syncWallets();

        System.out.println("15 SECONDS TO GO");
        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("AND WE'RE DONE");

        // Do a Blockchain sync

        return null;
    }

    @Override
    protected void onPreExecute() {
        broadcastSyncIsInProgress();
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        super.onProgressUpdate(progress);;
        broadcastSyncIsInProgress();
    }

    // Notifies the SyncableActivity that this sync is complete
    @Override
    protected void onPostExecute(Boolean result) {
        broadcastSyncIsComplete();
    }

    private void broadcastSyncIsInProgress() {
        Intent intent = new Intent("com.bitcoin.tracker.walletx.SYNC_STATUS");
        intent.putExtra("sync_complete", false);
        mContext.sendBroadcast(intent);
    }

    private void broadcastSyncIsComplete() {
        sSyncIsInProgress = false;
        Intent intent = new Intent("com.bitcoin.tracker.walletx.SYNC_STATUS");
        intent.putExtra("sync_complete", true);
        mContext.sendBroadcast(intent);
    }

} // SyncManager
