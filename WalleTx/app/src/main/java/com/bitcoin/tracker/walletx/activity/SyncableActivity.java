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
 * SyncableActivity is a super class for any activities from which the user
 * should be able to initiate a sync.
 *
 * SyncableActivity listens for broadcasts from the SyncManager and manages the animation
 * of the sync icon in the ActionBar accordingly. This allows the sync icon to remain in
 * animation across all activities until the sync is complete.
 */
public class SyncableActivity extends ActionBarActivity {

    // Receives broadcasts from the SyncManager
    private SyncBroadcastReceiver mSyncBroadcastReceiver;
    private IntentFilter mIntentFilter;

    // Reference to the sync actionbar menu item
    private MenuItem mSyncMenuItem;

    // Sync ActionView and rotate animation
    private ImageView mSyncActionView;
    private Animation mSyncAnimation;

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
        restartSyncIconRotation(); // if in progress
    }

    @Override
    protected void onPause() {
        super.onPause();
        // stop the sync icon animation so it doesn't appear over any other menus
        // if syncable, the next activity will re-apply the rotation to its own sync menu item
        stopSyncIconRotation();
        unregisterReceiver(mSyncBroadcastReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sync, menu);
        mSyncMenuItem = menu.findItem(R.id.action_sync);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        restartSyncIconRotation(); // if in progress
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_sync) {
            // initiate a sync of txs for all existing wallets
            SyncManager.syncExistingWallets(this.getApplicationContext());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Applies rotating ActionView to the sync menu item.
     */
    public void startSyncIconRotation() {
        if (mSyncMenuItem != null && mSyncMenuItem.getActionView() == null) {
            initializeSyncActionView();
            initializeSyncAnimation();
            mSyncActionView.startAnimation(mSyncAnimation);
            mSyncMenuItem.setActionView(mSyncActionView);
        }
    }

    /**
     * Stops the rotating ActionView.
     */
    public void stopSyncIconRotation() {
        if (mSyncMenuItem != null && mSyncMenuItem.getActionView() != null) {
            mSyncMenuItem.getActionView().clearAnimation();
            mSyncMenuItem.setActionView(null);
        }
    }

    // Restarts the rotation if a sync is in progress
    private void restartSyncIconRotation() {
        if (SyncManager.syncIsInProgress())
            startSyncIconRotation();
    }

    private void initializeSyncActionView() {
        if (mSyncActionView == null) {
            LayoutInflater inflater;
            inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mSyncActionView = (ImageView) inflater.inflate(R.layout.action_view_sync, null);
        }
    }

    private void initializeSyncAnimation() {
        if (mSyncAnimation == null) {
            mSyncAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate);
            mSyncAnimation.setRepeatCount(Animation.INFINITE);
        }
    }

    /**
     * Handles broadcasts sent from the SyncManager
     */
    private class SyncBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            boolean syncComplete = intent.getBooleanExtra("sync_complete", true);
            if (!syncComplete)
                startSyncIconRotation();
            else
                stopSyncIconRotation();
        }

    } // SyncBroadcastReceiver

} // SyncableActivity
