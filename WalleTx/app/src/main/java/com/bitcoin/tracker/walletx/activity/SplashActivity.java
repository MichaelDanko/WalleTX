package com.bitcoin.tracker.walletx.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.bitcoin.tracker.walletx.R;
import com.bitcoin.tracker.walletx.activity.navDrawer.MainActivity;
import com.bitcoin.tracker.walletx.api.SyncManager;
import com.bitcoin.tracker.walletx.model.Group;

import java.util.Timer;
import java.util.TimerTask;

/**
 * SplashActivity is the entry portal for Bitcoin WalleTx.
 * It sets up the default group on first run,
 * and initiates tx & price data sync before redirecting to the MainActivity.
 */
public class SplashActivity extends Activity {

    // Time duration for displaying the splash activity.
    final private long SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        Group.initDefaultGroup(this);
        SyncManager.syncExistingWallets(this.getApplicationContext());
        applySplashScreenTimeOut();
    }

    private void applySplashScreenTimeOut() {
        new Timer().schedule( new TimerTask() {
            @Override
            public void run() {
                Intent mainIntent = new Intent( SplashActivity.this, MainActivity.class );
                startActivity( mainIntent );
                finish();
            }
        }, SPLASH_TIME_OUT );
    }

} // SplashActivity
