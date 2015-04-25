package com.bitcoin.tracker.walletx.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.bitcoin.tracker.walletx.R;
import com.bitcoin.tracker.walletx.api.SyncManager;

/**
 * SyncableActivity is a super class for any activities from which
 * the user should be able to initiate a sync.
 */
public class SyncableActivity extends ActionBarActivity {

    // Receives broadcasts from the SyncManager
    private SyncBroadcastReceiver mSyncBroadcastReceiver;
    private IntentFilter mIntentFilter;

    // Reference to the sync actionbar menu item
    private MenuItem mSyncMenuItem;

    //
    private ImageView mSyncActionView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // initialize the broadcast receiver
        mSyncBroadcastReceiver = new SyncBroadcastReceiver();
        mIntentFilter = new IntentFilter("com.bitcoin.tracker.walletx.SYNC_STATUS");
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mSyncBroadcastReceiver, mIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopSyncIconRotation();
        unregisterReceiver(mSyncBroadcastReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.global, menu);
        mSyncMenuItem = menu.findItem(R.id.action_sync);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_sync) {
            SyncManager.syncExistingWallets(this.getApplicationContext());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startSyncIconRotation() {
        if (mSyncMenuItem != null && mSyncMenuItem.getActionView() == null) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mSyncActionView = (ImageView) inflater.inflate(R.layout.action_view_sync, null);
            Animation rotation = AnimationUtils.loadAnimation(this, R.anim.rotate);
            rotation.setRepeatCount(Animation.INFINITE);
            mSyncActionView.startAnimation(rotation);
            mSyncMenuItem.setActionView(mSyncActionView);
        }
    }

    private void stopSyncIconRotation() {
        if (mSyncMenuItem != null && mSyncMenuItem.getActionView() != null) {
            mSyncMenuItem.getActionView().clearAnimation();
            mSyncMenuItem.setActionView(null);
        }
    }

    /**
     * Handles broadcasts sent from the SyncManager
     */
    private class SyncBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            System.out.println("On progress received!!!!!");

            boolean syncComplete = intent.getBooleanExtra("sync_complete", true);
            if (!syncComplete) {
                startSyncIconRotation();
            } else {
                stopSyncIconRotation();
            }
        }

    } // SyncBroadcastReceiver

} // SyncableActivity
