package com.bitcoin.tracker.walletx.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;

import com.bitcoin.tracker.walletx.R;

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
        hideStatusBar();
        setContentView(R.layout.activity_splash);

        // TODO - Start service to fetch new tx and price data
        // TODO - Add TaskTimer that redirects to main

    }

    private void hideStatusBar() {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

} // SplashActivity
