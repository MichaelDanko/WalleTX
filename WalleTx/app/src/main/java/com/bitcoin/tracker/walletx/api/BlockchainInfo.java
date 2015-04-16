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
import com.bitcoin.tracker.walletx.model.Tx;
import com.bitcoin.tracker.walletx.model.TxCategory;
import com.bitcoin.tracker.walletx.model.TxNote;
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

    // The public address of the wallet to pull from blockchain.info
    private final String publicAddress;

    // User defined wallet (eg. Cash Wallet, Savings Wallet, etc.)
    private Walletx wtx = null;

    // Data will be pushed into an object that models the JSON received
    public static btcTransaction transaction = new btcTransaction();

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
        Log.v(logInfo + "Cons", this.publicAddress );
    }

    // Three parameter constructor, address, wallet, and object to dump data into.
    public BlockchainInfo(String publicAddress, Walletx wtx, btcTransaction incomingTransaction) {
        super();
        this.publicAddress = new String(publicAddress);
        this.wtx = wtx;
        this.transaction = incomingTransaction;
    }

      // Asynchronous call to complete web api pull in background.
      @Override
      protected Boolean doInBackground(Void...nothing) {

          String json = null;

          Log.v(logInfo, this.publicAddress);

          // -- Debugging
          //WalletGroup.dump();
          //Walletx.dump();
          //SingleAddressWallet.dump();

          try {
              long currentBlockHeight = getBlockHeight();
              json = readUrl("https://blockchain.info/address/" + publicAddress + "?format=json");
              Gson gson = new Gson();
              btcTransaction newTransaction = gson.fromJson(json, btcTransaction.class);

              // Copy downloaded values to original object (for testing, don't ask)
              transaction.hash160 = newTransaction.hash160;
              transaction.n_tx = newTransaction.n_tx;
              transaction.total_received = newTransaction.total_received;
              transaction.total_sent = newTransaction.total_sent;
              transaction.address = newTransaction.address;
              transaction.final_balance = newTransaction.final_balance;
              transaction.txs = newTransaction.txs;

              Tx newTx = null;
              Tx insertTx = null;
//              Tx checkTx = Tx.getTxWalleTx(transaction.hash);
//              if (checkTx == null) {

              // Loop through array of transaction data provided from JSON -> GSON conversion
              // Loop thorugh inputs first, if the public wallet address is found in the inputs
              // of a transaction that is considered bitcoin moving out of the wallet, if the public
              // wallet is found in the outputs (out) then bitcoin is moving into the wallet.
              for(int i=0; i < transaction.txs.size();i++){
                  for (int j=0; j < transaction.txs.get(i).inputs.size(); j++) {
                      if ((transaction.txs.get(i).inputs.get(j).prev_out.addr.equals(publicAddress))
                         && (Tx.getTxIndex(transaction.txs.get(i).tx_index) == null)) {
                          insertTx = new Tx(new Date(transaction.txs.get(i).time * 1000L),
                                            wtx,
                                            transaction.txs.get(i).block_height,
                                            currentBlockHeight - transaction.txs.get(i).block_height,
                                            transaction.txs.get(i).tx_index,
                                            new TxCategory("Uncategorized"),
                                            new TxNote(newTx, "Note"),
                                            0 - transaction.txs.get(i).inputs.get(j).prev_out.value,
                                            100,
                                            transaction.txs.get(i).hash);
                                        System.out.println(Long.toString(currentBlockHeight));
                                        System.out.println(Long.toString(currentBlockHeight - transaction.txs.get(i).block_height));
                          insertTx.save();
                      }
                  }
                  for (int j=0; j < transaction.txs.get(i).out.size(); j++){
                    if (transaction.txs.get(i).out.get(j).addr.equals(publicAddress)
                         && (Tx.getTxIndex(transaction.txs.get(i).tx_index) == null)) {
                      insertTx = new Tx (new Date(transaction.txs.get(i).time * 1000L),
                                         wtx,
                                         transaction.txs.get(i).block_height,
                                         currentBlockHeight - transaction.txs.get(i).block_height,
                                         transaction.txs.get(i).tx_index,
                                         new TxCategory("Uncategorized"),
                                         new TxNote(newTx, "Default Note"),
                                         transaction.txs.get(i).out.get(j).value,
                                         100,
                                         transaction.txs.get(i).hash);
                      insertTx.save();
                    }
                  }
              }
             Tx.dump();


              // TODO: Does wallet exist? check for updates
              //Walletx testThisWallet = null;
              //testThisWallet.getBy(transaction.address);
              //if (testThisWallet != null) {
              //  Log.v(logInfo, "Address Found, Updating Records");
              //}
              //else{
              //    Log.v(logInfo, "could not find address");
              // }

              // -- Debugging
              Log.v("btc_api: ---Debugging--", " --");
              Log.v("btc_api: hash160", transaction.hash160);
              Log.v("btc_api: address", transaction.address);
              Log.v("btc_api: n_tx", transaction.n_tx);
              Log.v("btc_api: total received", transaction.total_received);
              Log.v("btc_api: total send", transaction.total_sent);
              String finalBalance = Long.toString(transaction.final_balance);
              Log.v("btc_api: final balance", finalBalance);
              for (int i=0; i < transaction.txs.size(); i++) {
                  Log.v("btc_api:", "txs(" + i + ") -------------------" );
                  Log.v("btc_api:", "txs(" + i + ")" + " ver " + transaction.txs.get(i).ver);
                  for (int j=0; j < transaction.txs.get(i).inputs.size(); j++) {
                      Log.v("btc_api:" , "txs(" + i + ")" + "inputs(" + j + ")" + "sequence:" + transaction.txs.get(i).inputs.get(j).sequence);
                      Log.v("btc_api:" , "txs(" + i + ")" + "inputs(" + j + ")" + "spent:" + transaction.txs.get(i).inputs.get(j).prev_out.spent);
                      Log.v("btc_api:" , "txs(" + i + ")" + "inputs(" + j + ")" + "tx_index:" + transaction.txs.get(i).inputs.get(j).prev_out.tx_index);
                      Log.v("btc_api:" , "txs(" + i + ")" + "inputs(" + j + ")" + "addr:" + transaction.txs.get(i).inputs.get(j).prev_out.addr);
                      Log.v("btc_api:" , "txs(" + i + ")" + "inputs(" + j + ")" + "value:" + transaction.txs.get(i).inputs.get(j).prev_out.value);
                      Log.v("btc_api:" , "-----------------------------");
                  }
                  for (int j=0; j < transaction.txs.get(i).out.size(); j++) {
                      Log.v("btc_api:" , "txs(" + i + ")" + "out(" + j + ")" + "spent:" + transaction.txs.get(i).out.get(j).spent);
                      Log.v("btc_api:" , "txs(" + i + ")" + "out(" + j + ")" + "tx_index:" + transaction.txs.get(i).out.get(j).tx_index);
                      Log.v("btc_api:" , "txs(" + i + ")" + "out(" + j + ")" + "addr:" + transaction.txs.get(i).out.get(j).addr);
                      Log.v("btc_api:" , "txs(" + i + ")" + "out(" + j + ")" + "value:" + transaction.txs.get(i).out.get(j).value);
                      Log.v("btc_api:" , "-----------------------------");
                  }
              }
//               Log.v(logInfo, transaction.wtx);
//               Log.v(logInfo, transaction.address);
               //Tx tx = new Tx(transaction.timestamp, transaction.wtx, transaction.block,
                //             transaction.hash, transaction.category, transaction.note,
                //           transaction.amountBTC, transaction.amountLC, transaction.final_balance );
             return true;
          } catch (Exception e) {
              Log.e(e.getClass().getName(), "ERROR:" + e.getMessage(), e);
            return false;
          }


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

      private static Long getBlockHeight() throws Exception {
          BufferedReader reader = null;
          try {
              URL url = new URL("https://blockchain.info/q/getblockcount");
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

              return Long.parseLong(buffer.toString());
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
    protected void onProgressUpdate(Void...argument) {
    }

    @Override
    protected void onPostExecute(Boolean result) {
       //super.onPostExecute(result);
    }

    @Override
    protected void onPreExecute() {
    }

  } // BlockchainInfo
