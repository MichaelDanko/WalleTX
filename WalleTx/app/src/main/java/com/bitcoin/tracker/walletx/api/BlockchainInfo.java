package com.bitcoin.tracker.walletx.api;

import com.bitcoin.tracker.walletx.model.SingleAddressWallet;

import com.bitcoin.tracker.walletx.model.Tx;
import com.bitcoin.tracker.walletx.model.Walletx;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import java.io.IOException;

import java.util.Date;
import java.util.List;

/**
 * BlockchainInfo handles Blockchain.info API calls
 * and saves results to the database.
 *
 */
public class BlockchainInfo {

    // Strings to build url's for Blockchain.info API calls
    private final String URL_SINGLEADDR = "https://blockchain.info/address/";
    private final String URL_JSON = "?format=json";
    private final String URL_APPLY_LIMIT = "&limit=";
    private final int BLOCKCHAIN_INFO_MAX_TX_LIMIT = 50;

    // Latest block number - for calculating confirmations
    public static int sLatestBlock;

    public BlockchainInfo() {
        super();
    }

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
            syncTxsForWallet(saw, numTxsToSync);
        }
    }

    public void syncTxsForNewWallet(Walletx wtx) {
        SingleAddressWallet saw = SingleAddressWallet.getByWalletx(wtx);
        int numTxsToSync = getNumberOfTxsToSyncFor(saw);
        syncTxsForWallet(saw, numTxsToSync);
    }

    /**
     * @param saw SingleAddressWallet to check for new txs
     * @return int number of txs to sync for this wallet
     */
    private int getNumberOfTxsToSyncFor(SingleAddressWallet saw) {
        // perform quick api call to check if there are new txs to sync
        String sUrl = URL_SINGLEADDR + saw.publicKey + URL_JSON + URL_APPLY_LIMIT + "0";
        JsonElement json = getJsonElementFromUrl(buildUrlFromString(sUrl));
        BciAddress bciAddress = new Gson().fromJson(json, BciAddress.class);
        int currentTxCount = SingleAddressWallet.getPublicKey(bciAddress.address).getTxCount();
        return bciAddress.n_tx - currentTxCount;
    }

    private URL buildSingleAddressApiCallUrlFor(SingleAddressWallet saw, int limit) {
        String sUrl = URL_SINGLEADDR + saw.publicKey + URL_JSON +
                URL_APPLY_LIMIT + Integer.toString(limit);
        return buildUrlFromString(sUrl);
    }

    private URL buildUrlFromString(String sUrl) {
        URL url = null;
        try {
            url = new URL(sUrl);
        } catch (MalformedURLException e) {
            // TODO Handle MalformedURLException
        }
        return url;
    }

    private JsonElement getJsonElementFromUrl(URL url) {
        HttpURLConnection request = null;
        try {
            request = (HttpURLConnection) (url).openConnection();
            request.connect();
        } catch (MalformedURLException e) {
            // TODO Handle url error
        } catch (IOException e) {
            // TODO Handle connection error
        }
        JsonParser jp = new JsonParser(); // gson lib
        JsonElement json = null; //convert the input stream to a json element
        try {
            InputStreamReader reader = new InputStreamReader((InputStream) request.getContent());
            json = jp.parse(reader).getAsJsonObject();
        } catch (IOException e) {
            // TODO Handle getContent
        }
        return json;
    }

    private void syncTxsForWallet(SingleAddressWallet saw, int numTxsToSync) {
        // Blockchain limits the number of txs that can be pulled in a single api call
        // We need to track this to ensure that all new txs have been inserted
        while (numTxsToSync > 0) {
            URL url = buildSingleAddressApiCallUrlFor(saw, numTxsToSync);
            JsonElement json = getJsonElementFromUrl(url);
            BciAddress bciAddress = new Gson().fromJson(json, BciAddress.class);
            // Loop through each transaction obtained from the api call
            for (BciTx bciTx : bciAddress.txs)
                syncTx(saw, bciTx);
            numTxsToSync = numTxsToSync - BLOCKCHAIN_INFO_MAX_TX_LIMIT;
        }
    }

    private void syncTx(SingleAddressWallet saw, BciTx bciTx) {

        // Create the tx to insert
        Tx tx = new Tx();
        tx.timestamp = new Date(bciTx.time * 1000L);
        tx.wtx = Walletx.getBy(saw);
        tx.block = bciTx.block_height;
        tx.confirmations = 1; // TODO Delete (I want to calculate confs in real time)
        tx.category = null;
        tx.note = null; // TODO Delete (I want to remove this functionality)
        tx.hash = bciTx.hash;
        tx.amountLC = 0; // TODO Delete (This should also be calculated real time using balance table / exchange rate table

        // TODO Determine the tx amount #dankoMagic
        // TODO This is buggy FIX IT
        // TODO Simple test with https://blockchain.info/address/1GCq7aca1gn61VPbWAnPmtQafD7mbaSUJ4
        for (BciTxInputs input : bciTx.inputs) {
            // this tx is a spend
            boolean matchesSaw = input.prev_out.addr.equals(saw.publicKey);
            if (matchesSaw && Tx.getTxIndex(bciTx.tx_index) == null) {
                tx.amountBTC = input.prev_out.value;
                if (tx.amountBTC > 0)
                    tx.amountBTC = tx.amountBTC * -1;
                tx.tx_index = bciTx.tx_index;
                break;
            }
        }

        for (BciTxOutputs output : bciTx.out) {
            // this tx is a receive
            boolean matchesSaw = output.addr.equals(saw.publicKey);
            if (matchesSaw && Tx.getTxIndex(bciTx.tx_index) == null) {
                tx.amountBTC = output.value;
                if (tx.amountBTC < 0)
                    tx.amountBTC = tx.amountBTC * -1;
                tx.tx_index = bciTx.tx_index;
                break;
            }
        }
        tx.save();
    }

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
        public long tx_index;
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

} // BlockchainInfo
