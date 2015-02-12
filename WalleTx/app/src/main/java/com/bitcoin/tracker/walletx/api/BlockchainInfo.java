package com.bitcoin.tracker.walletx.api;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.bitcoin.tracker.walletx.model.Balance;
import com.bitcoin.tracker.walletx.model.SingleAddressWallet;
import com.bitcoin.tracker.walletx.model.Tx;
import com.bitcoin.tracker.walletx.model.WalletGroup;
import com.bitcoin.tracker.walletx.model.WalletType;
import com.bitcoin.tracker.walletx.model.Walletx;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/*
 * Fetches Blockchain and wallet data from Blockchain.info usin*g the Blockchain.info API.
 */
  public class BlockchainInfo extends AsyncTask<String, String, String> {

      private final Walletx wtx;
      private final String publicAddress;


    private static final String logInfo = "Blockchain API";

    static public class jsonInputs {
        String sequence;
    }

    static class jsonOutputs {

    }

    static public class jsonTxs {
        public int ver;
        List<jsonInputs> inputs;
    }

    static class jsonTransaction {
      @SerializedName("hash160")
      String hash160;
      @SerializedName("n_tx")
      String n_tx;
      @SerializedName("total_received")
      String total_received;
      @SerializedName("total_sent")
      String total_sent;
      @SerializedName("address")
      String address;
      @SerializedName("final_balance")
      String final_balance;
      //@SerializedName("txs")
      List<jsonTxs> txs;

      // List<jsonTxs> txs;

      String time;
      String amountBTC;
      String amountLC;
      String block;
      String hash;
      String wtx;
      String category;
      String note;

    }

    public BlockchainInfo(String publicAddress, Walletx wtx) {
        super();
        this.publicAddress = new String(publicAddress);
        this.wtx = wtx;
    }

      @Override
      protected String doInBackground(String... strings) {

          String json = null;

          WalletGroup.dump();
          Walletx.dump();
          SingleAddressWallet.dump();

          try {
              json = readUrl("https://blockchain.info/address/" + publicAddress + "?format=json");
              Gson gson = new Gson();
              jsonTransaction transaction = gson.fromJson(json, jsonTransaction.class);

              // try {
              //   Walletx.getBy(transaction.address);
              // } catch (Exception e) {
              //    Log.v(logInfo, "could not find address");
              // }

//               Log.v(logInfo, transaction.time + '\n' +
  //                           transaction.address);
              Log.v("hasher160", transaction.hash160);
              Log.v("address", transaction.address);
              Log.v("n_tx", transaction.n_tx);
              Log.v("total received", transaction.total_received);
              Log.v("total send", transaction.total_sent);
              Log.v("final balance", transaction.final_balance);
              for (int i=0; i < transaction.txs.size(); i++) {
                  Log.v("txs " + i, "" + transaction.txs.get(i).ver);
                  for (int j=0; j < transaction.txs.get(i).inputs.size(); j++) {
                      Log.v("sequence:" + j + ":", "" + transaction.txs.get(i).inputs.get(j).sequence);
                  }
              }
              // Log.v(logInfo, transaction.wtx);
              // Log.v(logInfo, transaction.address);
              // Tx tx = new Tx(transaction.timestamp, transaction.wtx, transaction.block,
              //               transaction.hash, transaction.category, transaction.note,
              //             transaction.amountBTC, transaction.amountLC, transaction.final_balance );

          } catch (Exception e) {
              Log.v(logInfo, "did not work");
              Log.v(logInfo, "" + e);
          }

          return "String";
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
    protected void onPostExecute(String result) {
      //pdia.dismiss();
      //adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, redditList);
      //redditView.setAdapter(adapter);
    }

    @Override
    protected void onPreExecute() {
      // super.onPreExecute();
      //ProgressDialog pdia = new ProgressDialog(MainActivity.this);
      //pdia.setMessage("Doing background..");
      //pdia.show();
    }

  } // BlockchainInfo
