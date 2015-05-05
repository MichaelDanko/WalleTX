package com.bitcoin.tracker.walletx.api;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.bitcoin.tracker.walletx.activity.Constants;
import com.bitcoin.tracker.walletx.model.Balance;
import com.bitcoin.tracker.walletx.model.Walletx;

import java.util.ArrayList;
import java.util.List;

/**
 * SyncManager is responsible for managing all interactions with the Blockchain and/or
 * other APIs, writing data to the database, and reporting progress to SyncableActivity.
 *
 * Although it is possible to use this class directly, it has been implemented with
 * several static methods that should be used to initiate a sync.
 */
public class SyncManager extends AsyncTask<Void, Integer, Boolean> {

    // Blocks multiple syncs
    private static boolean sSyncIsInProgress = false;

    private Context mContext;    // reference to context of caller
    private SyncType mSyncType;  // the type of sync to be performed
    private Walletx mWtx;        // wtx to sync if syncing a single wallet's txs

    // Error logging tag
    private static final String TAG = "SyncManager";

    //----- SyncManager API -----

    /**
     * Syncs all new transactions for all existing wallets.
     * @param context of caller
     */
    public static void syncExistingWallets(Context context) {
        if (!sSyncIsInProgress)
            new SyncManager(context, SyncType.TXS_FOR_EXISTING_WALLETS).execute();
    }

    /**
     * Syncs all transactions for a wallet just added.
     * @param context of caller
     * @param wtx wallet to sync
     */
    public static void syncNewWallet(Context context, Walletx wtx) {
        if (!sSyncIsInProgress)
            new SyncManager(context, SyncType.TXS_FOR_NEW_WALLET, wtx).execute();
    }

    /**
     * Syncs daily bitcoin price data
     * @param context of caller
     */
    public static void syncBitcoinPriceData(Context context) {
        if (!sSyncIsInProgress)
            new SyncManager(context, SyncType.BTC_PRICE_DATA).execute();
    }

    /**
     * Getter for sSyncIsInProgress
     */
    public static boolean syncIsInProgress() {
        return sSyncIsInProgress;
    }

    //----- end SyncManager API -----

    public SyncManager(Context context, SyncType syncType) {
        super();
        mContext = context;
        mSyncType = syncType;
    }

    public SyncManager(Context context, SyncType syncType, Walletx wtx) {
        super();
        mContext = context;
        mSyncType = syncType;
        mWtx = wtx;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        sSyncIsInProgress = true;
        switch (mSyncType) {
            case TXS_FOR_EXISTING_WALLETS:
                syncTxsForExistingWallets();
                break;
            case TXS_FOR_NEW_WALLET:
                syncTxsForNewWallet(mWtx);
                break;
            case BTC_PRICE_DATA:
                syncBtcPriceData();
                break;
            default:
                Log.w(TAG, "Invalid SyncType");
                break;
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        broadcastSyncIsInProgress();
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        super.onProgressUpdate(progress);
        broadcastSyncIsInProgress();
    }

    @Override
    protected void onPostExecute(Boolean result) {
        broadcastSyncIsComplete();
    }

    private void broadcastSyncIsInProgress() {
        Intent intent = new Intent(Constants.SYNC_MANAGER_STATUS);
        intent.putExtra(Constants.EXTRA_SYNC_MGR_COMPLETE, false);
        mContext.sendBroadcast(intent);
    }

    private void broadcastSyncIsComplete() {
        sSyncIsInProgress = false;
        Intent intent = new Intent(Constants.SYNC_MANAGER_STATUS);
        intent.putExtra(Constants.EXTRA_SYNC_MGR_COMPLETE, true);
        mContext.sendBroadcast(intent);
    }

    //----- Sync methods -----

    private void syncTxsForExistingWallets() {
        List<Walletx> wtxs = Walletx.getAll();
        List<Walletx> saws = new ArrayList<>();

        // Separate wtxs into lists based on type
        // This should allow us to maximize the efficiency of our API calls
        for (Walletx wtx : wtxs) {
            switch (wtx.type) {
                case SINGLE_ADDRESS_WALLET:
                    saws.add(wtx);
                    break;
                default:
                    Log.w(TAG, "Invalid SupportedWalletType");
                    break;
            }
        }

        // Call sync APIs
        new BlockchainInfo().syncTxsFor(saws);
    }

    private void syncTxsForNewWallet(Walletx wtx) {
        switch (wtx.type) {
            case SINGLE_ADDRESS_WALLET:
                new BlockchainInfo().syncTxsForNewWallet(wtx);
                break;
            default:
                Log.w(TAG, "Invalid SupportedWalletType");
                break;
        }
    }

    private void syncBtcPriceData() {
        // TODO Implement
    }

    /**
     * Various types of syncs that the SyncManager can initiate.
     */
    private enum SyncType {
        TXS_FOR_EXISTING_WALLETS,
        TXS_FOR_NEW_WALLET,
        BTC_PRICE_DATA
    }

} // SyncManager
