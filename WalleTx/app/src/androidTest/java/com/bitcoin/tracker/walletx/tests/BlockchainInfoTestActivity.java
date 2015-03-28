/* Michael Danko
 * CEN4021 Software Engineering II
 * Pretige Worldwide
 * Blockchain API Source Code for Assignment 7
 * Created 03-27-2015
 * Copyright 2015
 */

/*
 * Test involved pulling known data from a static source and verifying it.
 */

package com.bitcoin.tracker.walletx.tests;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.util.Log;

import com.bitcoin.tracker.walletx.activity.SplashActivity;
import com.bitcoin.tracker.walletx.api.BlockchainInfo;
import com.bitcoin.tracker.walletx.api.btcTransactionJSON;
import com.bitcoin.tracker.walletx.model.SingleAddressWallet;
import com.bitcoin.tracker.walletx.model.WalletGroup;
import com.bitcoin.tracker.walletx.model.WalletType;
import com.bitcoin.tracker.walletx.model.Walletx;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.CountDownLatch;

/**
 * Created by michael on 3/27/15.
 */
public class BlockchainInfoTestActivity extends ActivityUnitTestCase<SplashActivity> {
    private Intent _startIntent;
    final CountDownLatch signal = BlockchainInfo.signal;
    final static btcTransactionJSON blockChainInfoTest = new btcTransactionJSON();

    // Set activity to test with, avtivity is needed to provide a UI thread to run tests on.
    // If a true async thread is used the tests may fail due to conditions being tested prior
    // to completion of the asynctask.
    public BlockchainInfoTestActivity() {
        super(com.bitcoin.tracker.walletx.activity.SplashActivity.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
        _startIntent = new Intent(Intent.ACTION_MAIN);
        try {
            runAPI();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

    public final void runAPI() throws Throwable {
        startActivity(_startIntent, null, null);

        runTestOnUiThread(new Runnable() {

            @Override
            // Call blockchain API with known inputs and test results
            public void run() {
                Walletx wtx = new Walletx("1ELMkFs5x6avEj7H4FpmHryxUeSWaUJQhy", WalletType.SINGLE_ADDRESS_WALLET, WalletGroup.getBy("My Wallets"));
                SingleAddressWallet saw = new SingleAddressWallet(wtx, "1ELMkFs5x6avEj7H4FpmHryxUeSWaUJQhy");
                new BlockchainInfo("1ELMkFs5x6avEj7H4FpmHryxUeSWaUJQhy", wtx, blockChainInfoTest).execute();
            }
        });

        signal.await(30, TimeUnit.SECONDS);

    }

    public final void testHash160() throws Throwable {
        Log.v("hasher160assert", blockChainInfoTest.hash160);
        assertEquals("9242db2ecdf44d0c4d6f5ac4d4ba6d4a3d46ddc4", blockChainInfoTest.hash160);
    }

    public final void testntx() throws Throwable {
        assertEquals("2", blockChainInfoTest.n_tx);
    }

    public final void testTotalReceived() throws Throwable {
        assertEquals("100000", blockChainInfoTest.total_received);
    }

    public final void testTotalSent() throws Throwable {
        assertEquals("100000", blockChainInfoTest.total_sent);
    }

    public final void testAddress() throws Throwable {
        assertEquals("1ELMkFs5x6avEj7H4FpmHryxUeSWaUJQhy", blockChainInfoTest.address);
    }

    public final void testBalance() throws Throwable {
        assertEquals("0", blockChainInfoTest.final_balance);
    }

    public final void testTxs() throws Throwable {
        for (int i = 0; i < blockChainInfoTest.txs.size(); i++) {
            for (int j = 0; j < blockChainInfoTest.txs.get(i).inputs.size(); j++) {
                assertEquals("4294967295", blockChainInfoTest.txs.get(i).inputs.get(j).sequence);
            }
        }
    }

}
