package com.bitcoin.tracker.walletx.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.bitcoin.tracker.walletx.R;
import com.bitcoin.tracker.walletx.api.BlockchainInfo;
import com.bitcoin.tracker.walletx.model.SingleAddressWallet;
import com.bitcoin.tracker.walletx.model.WalletGroup;
import com.bitcoin.tracker.walletx.model.WalletType;
import com.bitcoin.tracker.walletx.model.Walletx;
import com.google.bitcoin.core.BlockChain;

import org.json.JSONException;

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
        WalletGroup.initDefaultGroup(this);

        // TODO - Fetch new tx and price data?


        Walletx wtx = new Walletx("1ELMkFs5x6avEj7H4FpmHryxUeSWaUJQhy", WalletType.SINGLE_ADDRESS_WALLET, WalletGroup.getBy("My Wallets"));
        SingleAddressWallet saw = new SingleAddressWallet(wtx, "1ELMkFs5x6avEj7H4FpmHryxUeSWaUJQhy");
        new BlockchainInfo("1ELMkFs5x6avEj7H4FpmHryxUeSWaUJQhy", wtx).execute();
        wtx.save();
        saw.save();

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

=======
>>>>>>> models
} // SplashActivity
