/* Michael Danko
 * CEN4021 Software Engineering II
 * Pretige Worldwide
 * Blockchain API Source Code for Assignment 7
 * Created 03-20-2015
 * Copyright 2015
 */
package com.bitcoin.tracker.walletx.api;

// Android requires Asynchronous Tasks to be completed in the background.
import android.os.AsyncTask;
// Android logging
import android.util.Log;

// Data Model Functionality
import com.bitcoin.tracker.walletx.model.ExchangeRate;
import com.bitcoin.tracker.walletx.model.SingleAddressWallet;
import com.bitcoin.tracker.walletx.model.Tx;
import com.bitcoin.tracker.walletx.model.Walletx;

// Gson library convert JSON to an object class
import com.google.gson.Gson;

// Used for web api calls
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

// Exception Catching
import java.io.IOException;

// Java Lists

// Used to determine when an asynchrnous call has been completed
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/*
 * Fetches Blockchain and wallet data from Blockchain.info usin*g the Blockchain.info API.
 */
public class BlockchainInfo extends AsyncTask<Void, Void, Boolean> {

    // Reference to calling fragment
    SyncableInterface caller;

    // Data will be pushed into an object that models the JSON received
    public static BlockchainInfoGson blockchainInfoWalletData = new BlockchainInfoGson();

    public LatestBlockInfo latestBlockInfo = new LatestBlockInfo();
    public TickerData tickerData = new TickerData();

    // CountDownLatch is used to determine when an asynchronous call is complete, otherwise
    // tests may fail if the data asynchronous call is slow.
    public static final CountDownLatch signal = new CountDownLatch(1);

    // Two parameter constructor, address and wallet
    public BlockchainInfo() {
        super();
    }

    // Two parameter constructor, calling fragment, address and wallet
    public BlockchainInfo(SyncableInterface caller) {
        super();
        this.caller = caller;
    }

    // Asynchronous call to complete web api pull in background.
    @Override
    protected Boolean doInBackground(Void...nothing) {

        List<Walletx> wtxs = Walletx.getAll();
        for (Walletx wtx : wtxs) {

            String jsonTicker;
            String jsonLatestBlock;
            String json;

            try {
                jsonTicker = readUrl("https://blockchain.info/ticker?format=json");
                Gson gsonTickerData = new Gson();
                tickerData = gsonTickerData.fromJson(jsonTicker, TickerData.class);
                ExchangeRate.EXCHANGE_RATE_IN_USD = tickerData.USD.sell;

                jsonLatestBlock = readUrl("https://blockchain.info/latestblock?format=json");
                Gson gsonLatestBlockInfo = new Gson();
                latestBlockInfo = gsonLatestBlockInfo.fromJson(jsonLatestBlock, LatestBlockInfo.class);

                SingleAddressWallet saw = SingleAddressWallet.getByWalletx(wtx);
                json = readUrl("https://blockchain.info/address/" + saw.publicKey + "?format=json");
                Gson gson = new Gson();
                blockchainInfoWalletData = gson.fromJson(json, BlockchainInfoGson.class);
                importBlockChainInfoData(wtx);

            } catch (Exception e) {
                Log.e(e.getClass().getName(), "ERROR:" + e.getMessage(), e);
                return false;
            }
        }
        return true;
    }

    private void importBlockChainInfoData (Walletx wtx) {

        // The tx amount can be determined by looking at the tx.result of the next tx
        Tx prevTx = new Tx();
        Tx newTx = new Tx();
        BlockchainInfoGson.txGson lastTx = null;
        SingleAddressWallet saw = SingleAddressWallet.getByWalletx(wtx);

        // Loop through each transaction associated with this wallet
        for (BlockchainInfoGson.txGson tx : blockchainInfoWalletData.txs) {

            long confirmations = Long.valueOf(0);
            if (tx.block_height != 0) {
                confirmations = (latestBlockInfo.height - tx.block_height) + 1;
                System.out.println("Confirmations -----------------------------" + confirmations);
            }

            Tx existingTx = Tx.getTxByHash(tx.hash);
            if (existingTx != null) {
                existingTx.confirmations = confirmations;
                existingTx.save();
            } else {

                // Set the amount for the previous tx and save
                if (prevTx.hash != null) {
                    prevTx.amountBTC = tx.result;
                    prevTx.save();
                }

                newTx = new Tx(saw.publicKey,
                        new Date(tx.time * 1000L),
                        wtx,
                        tx.block_height,
                        confirmations,
                        null,
                        null,
                        tx.result,
                        0,
                        tx.hash);
                if (tx.result >= 0)
                    wtx.totalReceive++;
                else
                    wtx.totalSpend++;
                wtx.finalBalance = blockchainInfoWalletData.final_balance;
                wtx.save();

                // Save on next iteration
                prevTx = newTx;

                // Use for reference in calculating the amount of last tx
                // using michael's logic
                lastTx = tx;
            }
        }

        // Although it is faster, we can't calculate the tx amount using the above method.
        // Utilizing M.Danko's logic to calculate the amount of the final tx before saving
        if (lastTx != null) {
            for (BlockchainInfoGson.inputsGson input : lastTx.inputs) {
                if ( input.prev_out.addr.equals(saw.publicKey) && (Tx.getTxIndex(lastTx.tx_index) == null) ) {
                    prevTx.amountBTC = 0 - input.prev_out.value;
                }
            }
            for (BlockchainInfoGson.outputsGson out : lastTx.out) {
                if (out.addr.equals(saw.publicKey) && (Tx.getTxIndex(lastTx.tx_index) == null)) {
                    newTx.amountBTC = out.value;
                }

            }
            newTx.save();
        }

        System.out.println("");
        System.out.println("DONE WITH ONE WALLET");
        System.out.println("");
    }

    private static String readUrl (String urlString) throws Exception {
        BufferedReader reader = null;
        try {
            URL url = new URL(urlString);
            try {
                reader = new BufferedReader(new InputStreamReader(url.openStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            StringBuffer buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            try {
                while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return buffer.toString();
        } finally {
            if (reader != null)
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onProgressUpdate(Void...argument) {}

    @Override
    protected void onPostExecute(Boolean result) {
        if (caller != null) {
            caller.stopSyncRelatedUI();
        }
    }

    @Override
    protected void onPreExecute() {
        if (caller != null) {
            caller.startSyncRelatedUI();
        }
    }

    public class LatestBlockInfo {
        public long height;
        public LatestBlockInfo() {}
    }

    public class TickerData {
        public ExchangeRatesInUSD USD;
        public TickerData() {}

        public class ExchangeRatesInUSD {
            public float sell;
            public ExchangeRatesInUSD() {}
        }
    }

} // BlockchainInfo
