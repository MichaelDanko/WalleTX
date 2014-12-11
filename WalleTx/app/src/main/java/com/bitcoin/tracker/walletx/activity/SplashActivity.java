package com.bitcoin.tracker.walletx.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.bitcoin.tracker.walletx.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 * SplashActivity is the entry portal for Bitcoin WalleTx.
 *
 * Activity should start a background service to fetch new transactions & price data
 * before redirecting to the MainActivity. Display for a minimum of 2 seconds.
 *
 */
public class SplashActivity extends Activity {

    final private long SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        /*
        ----------------------------------------------------------------
        TODO - Start background service to fetch new tx and price data
        ----------------------------------------------------------------
        */

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

} // SplashActivity
