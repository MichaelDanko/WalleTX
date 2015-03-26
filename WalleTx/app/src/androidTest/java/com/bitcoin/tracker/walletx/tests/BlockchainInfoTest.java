package com.bitcoin.tracker.walletx.tests;

import android.util.Log;

import com.bitcoin.tracker.walletx.api.BlockchainInfo;
import com.bitcoin.tracker.walletx.api.btcTransactionJSON;
import com.bitcoin.tracker.walletx.model.SingleAddressWallet;
import com.bitcoin.tracker.walletx.model.WalletGroup;
import com.bitcoin.tracker.walletx.model.WalletType;
import com.bitcoin.tracker.walletx.model.Walletx;

import junit.framework.TestCase;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by michael on 3/25/15.
 */

public class BlockchainInfoTest extends TestCase {

    public void testBlockChainInfo() {
        Walletx wtx = new Walletx("1ELMkFs5x6avEj7H4FpmHryxUeSWaUJQhy", WalletType.SINGLE_ADDRESS_WALLET, WalletGroup.getBy("My Wallets"));
        SingleAddressWallet saw = new SingleAddressWallet(wtx, "1ELMkFs5x6avEj7H4FpmHryxUeSWaUJQhy");

        btcTransactionJSON blockChainInfoTest = new btcTransactionJSON();

        runTestOnUiThread(new Runnable() {

            @Override
            public void run() {
                BlockchainInfo("1ELMkFs5x6avEj7H4FpmHryxUeSWaUJQhy", wtx, blockChainInfoTest).execute();
            });

        new BlockchainInfo("1ELMkFs5x6avEj7H4FpmHryxUeSWaUJQhy", wtx, blockChainInfoTest).execute();

        Log.v("hasher160test", blockChainInfoTest.hash160);

        assertEquals("9242db2ecdf44d0c4d6f5ac4d4ba6d4a3d46ddc4", blockChainInfoTest.hash160);
    }

}
