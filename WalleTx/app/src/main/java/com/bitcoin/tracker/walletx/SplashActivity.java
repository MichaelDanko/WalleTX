package com.bitcoin.tracker.walletx;

import android.app.Activity;
import android.os.Bundle;

/**
 * SplashActivity is the entry portal for Bitcoin WalleTx.
 *
 * Activity should start a background service to fetch new transactions & price data
 * before redirecting to the MainActivity. Display for a minimum of 2 seconds.
 *
 */
public class SplashActivity extends Activity {

    final private long delay = 2000; //minimum amount of time to display

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // TODO - Start service to fetch new tx and price data
        // TODO - Add TaskTimer that redirects to main

    }

} // SplashActivity