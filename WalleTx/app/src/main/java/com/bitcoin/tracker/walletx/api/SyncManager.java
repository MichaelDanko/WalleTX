package com.bitcoin.tracker.walletx.api;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.bitcoin.tracker.walletx.model.Walletx;

import java.util.List;

/**
 * SyncManager is responsible for managing all interactions with the Blockchain
 * or other API and reporting progress to the SyncableActivity.
 *
 * Although it is possible to use this class directly, it has been implemented with
 * several static methods that should be used to initiate a sync.
 */
public class SyncManager extends AsyncTask<Void, Integer, Boolean> {

    private Context mContext; // reference to context of caller
    public static boolean sSyncIsInProgress = false;  // block multiple syncs

    /*----------------------------------------/
     *  Static Sync Methods - Use these ...   /
     *---------------------------------------*/

    public static void syncExistingWallets(Context context) {
        if (!sSyncIsInProgress)
            new SyncManager(context).execute();
        else
            System.out.println("################ BLOCKED");
    }

    public SyncManager(Context context) {
        super();
        mContext = context;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        sSyncIsInProgress = true;

        // get list of all walletxs
        List<Walletx> wtxs = Walletx.getAll();

        //BlockchainInfo blockchainInfo = new BlockchainInfo(wtxs);
        //boolean done = blockchainInfo.syncWallets();

        // Do a Blockchain sync


        // Temp code
        for (int i = 0; i < 20; i++) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            publishProgress();
        }

        return null;
    }






    // Notifies the SyncableActivity that this sync is ongoing
    @Override
    protected void onPreExecute() {
        Intent intent = new Intent("com.bitcoin.tracker.walletx.SYNC_STATUS");
        intent.putExtra("sync_complete", false);
        mContext.sendBroadcast(intent);
    }

    // Notifies the SyncableActivity that this sync is ongoing
    @Override
    protected void onProgressUpdate(Integer... progress) {
        super.onProgressUpdate(progress);;
        Intent intent = new Intent("com.bitcoin.tracker.walletx.SYNC_STATUS");
        intent.putExtra("sync_complete", false);
        mContext.sendBroadcast(intent);
    }

    // Notifies the SyncableActivity that this sync is complete
    @Override
    protected void onPostExecute(Boolean result) {
        sSyncIsInProgress = false;
        Intent intent = new Intent("com.bitcoin.tracker.walletx.SYNC_STATUS");
        intent.putExtra("sync_complete", true);
        mContext.sendBroadcast(intent);
    }

}
