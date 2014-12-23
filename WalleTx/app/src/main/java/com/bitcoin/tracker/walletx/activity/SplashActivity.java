package com.bitcoin.tracker.walletx.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.bitcoin.tracker.walletx.R;
import com.bitcoin.tracker.walletx.activity.navDrawer.MainActivity;
import com.bitcoin.tracker.walletx.model.wallet.WalletGroup;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * SplashActivity is the entry portal for Bitcoin WalleTx.
 * Starts a background service to fetch new transactions & price data
 * before redirecting to the MainActivity.
 */
public class SplashActivity extends Activity {

    /**
     * Time duration for displaying the splash activity.
     */
    final private long SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setupDefaultWalletGroup();

        // TODO - Fetch new tx and price data?

        applySplashScreenTimeOut();
    } // onCreate

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

    // Adds 'My Wallets' group to the WalletGroups table on first run
    // and sets it as the default group.
    private void setupDefaultWalletGroup() {
        List<WalletGroup> groups = WalletGroup.getAll();
        if ( groups.size() < 1 ) {
            WalletGroup defaultGroup = new WalletGroup();
            defaultGroup.name = getString(R.string.wtx_default_wallet_group);
            defaultGroup.setAsDefault(1);
            defaultGroup.displayOrder = 1;
            defaultGroup.save();
        }
    }

} // SplashActivity
