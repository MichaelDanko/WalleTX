package com.bitcoin.tracker.walletx.api;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.bitcoin.tracker.walletx.model.Walletx;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * SyncManager is responsible for managing all interactions with the Blockchain
 * or other API and reporting progress to the SyncableActivity.
 *
 * Although it is possible to use this class directly, it has been implemented with
 * several static methods that should be used to initiate a sync.
 */
public class SyncManager extends AsyncTask<Void, Integer, Boolean> {

    // Reference to context of caller
    private Context mContext;

    // Block multiple syncs
    private static boolean mSyncIsInProgress = false;

    /*----------------------------------------/
     *  Static Sync Methods - Use these ...   /
     *---------------------------------------*/

    public static void syncExistingWallets(Context context) {
        if (!mSyncIsInProgress)
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

        //SyncManager.mSyncIsInProgress = true;

        // get list of all walletxs
        List<Walletx> wtxs = Walletx.getAll();

        //new BlockchainInfo().execute();


        AsyncTask<Void, Void, Boolean> blockchainTask = new BlockchainInfo(this);
        blockchainTask.execute();

        while (blockchainTask.getStatus() != Status.FINISHED) {
           System.out.println("NOT CANC");
        }






        return null;
    }





    public static void resetSyncIsInProgress() {
        System.out.println("AM I CALLED");
        //SyncManager.mSyncIsInProgress = false;
    }

    /**
     * Notifies the SyncableActivity that this sync is ongoing.
     */
    @Override
    protected void onPreExecute() {
        Intent intent = new Intent("com.bitcoin.tracker.walletx.SYNC_STATUS");
        intent.putExtra("sync_complete", false);
        mContext.sendBroadcast(intent);
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        super.onProgressUpdate(progress);;
        Intent intent = new Intent("com.bitcoin.tracker.walletx.SYNC_STATUS");
        intent.putExtra("sync_complete", false);
        mContext.sendBroadcast(intent);
    }

    /**
     * Notifies the SyncableActivity that this sync is complete.
     */
    @Override
    protected void onPostExecute(Boolean result) {
        Intent intent = new Intent("com.bitcoin.tracker.walletx.SYNC_STATUS");
        intent.putExtra("sync_complete", true);
        mContext.sendBroadcast(intent);
    }

}
