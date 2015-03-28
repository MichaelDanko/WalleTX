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
import com.bitcoin.tracker.walletx.model.SingleAddressWallet;
import com.bitcoin.tracker.walletx.model.WalletGroup;
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
import java.util.List;

// Used to determine when an asynchrnous call has been completed
import java.util.concurrent.CountDownLatch;

/*
 * Fetches Blockchain and wallet data from Blockchain.info usin*g the Blockchain.info API.
 */

  public class BlockchainInfo extends AsyncTask<String, String, BlockchainInfo> {

    // The public address of the wallet to pull from blockchain.info
    private final String publicAddress;

    // User defined wallet (eg. Cash Wallet, Savings Wallet, etc.)
    private Walletx wtx = null;

    // Data will be pushed into an object that models the JSON received
    public static btcTransactionJSON transaction = null;

    // Logging
    private static final String logInfo = "Blockchain API";

    // CountDownLatch is used to determine when an asynchronous call is complete, otherwise
    // tests may fail if the data asynchronous call is slow.
    public static final CountDownLatch signal = new CountDownLatch(1);

    // Two parameter constructor, address and wallet
    public BlockchainInfo(String publicAddress, Walletx wtx) {
        super();
        this.publicAddress = new String(publicAddress);
        this.wtx = wtx;
    }

    // Three parameter constructor, address, wallet, and object to dump data into.
    public BlockchainInfo(String publicAddress, Walletx wtx, btcTransactionJSON incomingTransaction) {
        super();
        this.publicAddress = new String(publicAddress);
        this.wtx = wtx;
        this.transaction = incomingTransaction;
    }

      // Asynchronous call to complete web api pull in background.
      @Override
      protected BlockchainInfo doInBackground(String... strings) {

          String json = null;

          // -- Debugging
          //WalletGroup.dump();
          //Walletx.dump();
          //SingleAddressWallet.dump();
          try {
              json = readUrl("https://blockchain.info/address/" + publicAddress + "?format=json");
              Gson gson = new Gson();
              btcTransactionJSON newTransaction = gson.fromJson(json, btcTransactionJSON.class);

              // Copy downloaded values to original object
              transaction.hash160 = newTransaction.hash160;
              transaction.n_tx = newTransaction.n_tx;
              transaction.total_received = newTransaction.total_received;
              transaction.total_sent = newTransaction.total_sent;
              transaction.address = newTransaction.address;
              transaction.final_balance = newTransaction.final_balance;
              transaction.txs = newTransaction.txs;

              // TODO: Does wallet exist? check for updates
              // try {
              //   Walletx.getBy(transaction.address);
              // } catch (Exception e) {
              //    Log.v(logInfo, "could not find address");
              // }

              // -- Debugging
              //Log.v("hash160", transaction.hash160);
              //Log.v("address", transaction.address);
              //Log.v("n_tx", transaction.n_tx);
              //Log.v("total received", transaction.total_received);
              //Log.v("total send", transaction.total_sent);
              //Log.v("final balance", transaction.final_balance);
              //for (int i=0; i < transaction.txs.size(); i++) {
              //    Log.v("txs " + i, "" + transaction.txs.get(i).ver);
              //    for (int j=0; j < transaction.txs.get(i).inputs.size(); j++) {
              //        Log.v("sequence:" + j + ":", "" + transaction.txs.get(i).inputs.get(j).sequence);
              //    }
              //}
              // Log.v(logInfo, transaction.wtx);
              // Log.v(logInfo, transaction.address);
              // Tx tx = new Tx(transaction.timestamp, transaction.wtx, transaction.block,
              //               transaction.hash, transaction.category, transaction.note,
              //             transaction.amountBTC, transaction.amountLC, transaction.final_balance );

          } catch (Exception e) {
              Log.v(logInfo, "ERROR:" + e);
          }

         return this;
      }

      private static String readUrl (String urlString) throws Exception {
        BufferedReader reader = null;
        try {
          Log.v("Blockchain API", urlString);
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

          System.out.println(buffer.toString());
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
    protected void onProgressUpdate(String... values) {
    }

    @Override
    protected void onPostExecute(BlockchainInfo result) {
       super.onPostExecute(result);
       signal.countDown();
    }

    @Override
    protected void onPreExecute() {
    }

  } // BlockchainInfo
