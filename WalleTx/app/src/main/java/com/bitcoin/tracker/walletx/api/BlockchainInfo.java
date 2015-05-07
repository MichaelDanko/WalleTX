package com.bitcoin.tracker.walletx.api;

import android.content.Context;
import android.content.SharedPreferences;

import com.bitcoin.tracker.walletx.activity.Constants;
import com.bitcoin.tracker.walletx.helper.JsonHelper;
import com.bitcoin.tracker.walletx.model.Balance;
import com.bitcoin.tracker.walletx.model.SingleAddressWallet;

import com.bitcoin.tracker.walletx.model.Tx;
import com.bitcoin.tracker.walletx.model.Walletx;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.net.URL;

import java.util.Date;
import java.util.List;

/**
 * BlockchainInfo handles Blockchain.info API calls
 * and saves results to the database.
 *
 * TODO This class requires thorough testing.
 *
 */
public class BlockchainInfo {

    // Strings to build url's for Blockchain.info API calls
    private static final String URL_SINGLEADDR = "https://blockchain.info/address/";
    private static final String URL_JSON = "?format=json";
    private static final String URL_APPLY_LIMIT = "&limit=";
    private static final String URL_APPLY_OFFSET = "&offset=";
    private static final String URL_TICKER = "https://blockchain.info/ticker";
    private static final String URL_LATEST_BLOCK = "https://blockchain.info/latestblock";
    private static final int BLOCKCHAIN_INFO_MAX_TX_LIMIT = 50;

    // Communicates with SyncManager that an error occurred.
    public static boolean sInvalidTxJsonDataReceived = false;
    public static boolean sInvalidTickerJsonReceived = false;
    public static boolean sInvalidLatestBlockJsonReceived = false;

    public BlockchainInfo() {
        super();
    }

    //region EXCHANGE RATE -------------------------------------------------------------------------

    // Stores the USD exchange rate from last sync
    private static float sExchangeRateInUsd = 0;

    public static float getCurrentExchangeRate(Context context) {
        if (sExchangeRateInUsd == 0) {
            SharedPreferences sp = context.getSharedPreferences(Constants.SHARED_PREFERENCES,
                    Context.MODE_PRIVATE);
            return sp.getFloat(Constants.SP_EXCHANGE_RATE, 0);
        }
        return sExchangeRateInUsd;
    }

    // Current called whenever syncable activity is created
    public static void syncExchangeRate(Context context) {
        URL url = JsonHelper.buildUrlFromString(URL_TICKER + URL_JSON);
        JsonElement json = JsonHelper.getJsonElementFromUrl(url);
        BciTickerData tickerData = new Gson().fromJson(json, BciTickerData.class);
        if (tickerData == null) {
            sInvalidTickerJsonReceived = true;
        } else {
            BlockchainInfo.sExchangeRateInUsd = tickerData.USD.sell;
            // Save to shared pref in case activity loads before data pulled
            SharedPreferences sp = context.getSharedPreferences(Constants.SHARED_PREFERENCES,
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putFloat(Constants.SP_EXCHANGE_RATE, BlockchainInfo.sExchangeRateInUsd);
            editor.commit();
        }
    }

    //endregion
    //region LATEST BLOCK --------------------------------------------------------------------------

    // Stores the USD exchange rate from last sync
    private static long sLatestBlock = 0;

    // Current called whenever syncable activity is created
    public static void syncLatestBlock(Context context) {
        URL url = JsonHelper.buildUrlFromString(URL_LATEST_BLOCK + URL_JSON);
        JsonElement json = JsonHelper.getJsonElementFromUrl(url);
        BciLatestBlock latestBlock = new Gson().fromJson(json, BciLatestBlock.class);
        if (latestBlock == null) {
            sInvalidLatestBlockJsonReceived = true;
        } else {
            BlockchainInfo.sLatestBlock = latestBlock.height;
            // Save to shared pref in case activity loads before data pulled
            SharedPreferences sp = context.getSharedPreferences(Constants.SHARED_PREFERENCES,
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putLong(Constants.SP_LATEST_BLOCK, BlockchainInfo.sLatestBlock);
            editor.commit();
        }
    }

    public static long getLatestBlock(Context context) {
        if (sLatestBlock == 0) {
            SharedPreferences sp = context.getSharedPreferences(Constants.SHARED_PREFERENCES,
                    Context.MODE_PRIVATE);
            return sp.getLong(Constants.SP_LATEST_BLOCK, 0);
        }
        return sLatestBlock;
    }

    //endregion
    //region TXS
    //----------------------------------------------------------------------------------------------

    /**
     * Syncs new transactions associated with list of Walletx objects.
     *
     * @param wtxs List of Walletx objects to sync
     */
    public void syncTxsFor(List<Walletx> wtxs) {
        for (Walletx wtx : wtxs) {
            SingleAddressWallet saw = SingleAddressWallet.getByWalletx(wtx);
            if (saw == null)
                break;
            int numTxsToSync = getNumberOfTxsToSyncFor(saw);
            syncTxsForWallet(saw, numTxsToSync, wtx);
        }
    }

    public void syncTxsForNewWallet(Walletx wtx) {
        SingleAddressWallet saw = SingleAddressWallet.getByWalletx(wtx);
        int numTxsToSync = getNumberOfTxsToSyncFor(saw);
        syncTxsForWallet(saw, numTxsToSync, wtx);
    }

    /**
     * @param saw SingleAddressWallet to check for new txs
     * @return int number of txs to sync for this wallet
     */
    private int getNumberOfTxsToSyncFor(SingleAddressWallet saw) {
        // perform quick api call to check if there are new txs to sync
        String sUrl = URL_SINGLEADDR + saw.publicKey + URL_JSON + URL_APPLY_LIMIT + "0";
        JsonElement json = JsonHelper.getJsonElementFromUrl(JsonHelper.buildUrlFromString(sUrl));
        BciAddress bciAddress = new Gson().fromJson(json, BciAddress.class);
        if (bciAddress == null) {
            sInvalidTxJsonDataReceived = true;
            return 0;
        } else {
            int currentTxCount = SingleAddressWallet.getPublicKey(bciAddress.address).getTxCount();
            return bciAddress.n_tx - currentTxCount;
        }
    }

    // Builds URL with offset to pickup last txs from those to sync
    private URL buildSingleAddressApiCallUrlFor(SingleAddressWallet saw, int numToSync) {
        int offset = numToSync - 50;
        String sUrl = URL_SINGLEADDR + saw.publicKey + URL_JSON +
                URL_APPLY_OFFSET + offset;
        return JsonHelper.buildUrlFromString(sUrl);
    }

    private void syncTxsForWallet(SingleAddressWallet saw, int numTxsToSync, Walletx wtx) {
        // Blockchain limits the number of txs that can be pulled in a single api call
        // We need to track this to ensure that all new txs have been inserted
        while (numTxsToSync > 0) {
            URL url = buildSingleAddressApiCallUrlFor(saw, numTxsToSync);
            JsonElement json = JsonHelper.getJsonElementFromUrl(url);
            BciAddress bciAddress = new Gson().fromJson(json, BciAddress.class);
            if (bciAddress == null) {
                sInvalidTxJsonDataReceived = true;
            } else {
                // Loop through each transaction obtained from the api call
                for (BciTx bciTx : bciAddress.txs)
                    syncTx(saw, bciTx);
                numTxsToSync = numTxsToSync - BLOCKCHAIN_INFO_MAX_TX_LIMIT;
            }
        }
        Balance.clean(wtx);
    }

    private void syncTx(SingleAddressWallet saw, BciTx bciTx) {

        if (Tx.getTxByHash(bciTx.hash) == null) {
            Walletx wtx = Walletx.getBy(saw);

            // Create the tx to insert
            Tx tx = new Tx();
            tx.timestamp = new Date(bciTx.time * 1000L);
            tx.wtx = wtx;
            tx.block = bciTx.block_height;
            tx.category = null;
            tx.hash = bciTx.hash;

            // Sum inputs associated with this address
            long totalInputs = 0;
            for (BciTxInputs input : bciTx.inputs) {
                boolean matchesSaw = input.prev_out.addr.equals(saw.publicKey);
                if (matchesSaw)
                    totalInputs = totalInputs + input.prev_out.value;
            }

            // Sum outputs assoicated with this address
            long totalOutputs = 0;
            for (BciTxOutputs output : bciTx.out) {
                boolean matchesSaw = output.addr.equals(saw.publicKey);
                if (matchesSaw)
                    totalOutputs = totalOutputs + output.value;
            }

            // Calculate the Tx amount & write to the database
            tx.amountBTC = 0 - (totalInputs - totalOutputs);
            if (tx.amountBTC < 0)
                tx.type = Tx.SPEND;
            else
                tx.type = Tx.RECEIVE;

            // Calculate the associated balance and save
            tx.balance = Balance.createNewAssociatedWith(tx);
            tx.save();
        }

    } // syncTx

    //endregion

    //--------------------------------------------------//
    //  Blockchain.info json requests in object form.   //
    //  Classes only include data relevant to WalleTx.  //
    //--------------------------------------------------//

    // Single address wallet
    public class BciAddress {
        public String address;
        public int n_tx;
        public List<BciTx> txs;
    }

    public class BciTx {
        public String hash;
        public int block_height;
        public long time;
        public List<BciTxInputs> inputs;
        public List<BciTxOutputs> out;

    }

    public class BciTxInputs {
        public BciTxInputPrevOut prev_out;
    }

    public class BciTxOutputs {
        public String addr;
        public long value;
    }

    public class BciTxInputPrevOut {
        public String addr;
        public long value;
    }

    // Ticker data
    public class BciTickerData {
        public ExchangeRatesInUSD USD;
        public BciTickerData() {}
        public class ExchangeRatesInUSD {
            public float sell;
            public ExchangeRatesInUSD() {}
        }
    }

    // Latest block
    public class BciLatestBlock {
        public int height;
    }

} // BlockchainInfo
