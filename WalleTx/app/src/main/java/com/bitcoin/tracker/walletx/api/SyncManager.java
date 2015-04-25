package com.bitcoin.tracker.walletx.api;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

/**
 * Created by brianhowell on 4/25/15.
 */
public class SyncManager extends AsyncTask<Void, Integer, Boolean> {

    private Context mContext;

    public SyncManager(Context context) {
        mContext = context;
    }

    public static void syncExistingWallets(Context context) {
        new SyncManager(context).execute();
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        publishProgress();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        publishProgress();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

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

    @Override
    protected void onPostExecute(Boolean result) {
        Intent intent = new Intent("com.bitcoin.tracker.walletx.SYNC_STATUS");
        intent.putExtra("sync_complete", true);
        mContext.sendBroadcast(intent);
    }

}
