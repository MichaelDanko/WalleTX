package com.bitcoin.tracker.walletx.api;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import com.bitcoin.tracker.walletx.model.Balance;
import com.bitcoin.tracker.walletx.model.SingleAddressWallet;
import com.bitcoin.tracker.walletx.model.WalletGroup;
import com.bitcoin.tracker.walletx.model.WalletType;
import com.bitcoin.tracker.walletx.model.Walletx;
import com.google.gson.Gson;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

/**
 * Fetches Blockchain and wallet data from Blockchain.info using the Blockchain.info API.
 */
  public class BlockchainInfo extends AsyncTask<String, String, String> {

      private final Walletx wtx;
      private final String publicAddress;
      private ProgressDialog QueryProgressDialog;

    private static final String logInfo = "Blockchain API";

    static class Transaction {
      String address;
      String final_balance;
    }

    public BlockchainInfo(String publicAddress, Walletx wtx) {
        super();
        this.publicAddress = new String(publicAddress);
        this.wtx = wtx;
    }

      @Override
      protected String doInBackground(String... strings) {

          String json = null;
          /* WalletGroup wtg = new WalletGroup();
          Walletx wtx = new Walletx();
          SingleAddressWallet saw = new SingleAddressWallet(); */

          //WalletGroup wtg2 = WalletGroup.load(WalletGroup.class, 2 );
          //Log.e("test", wtg2.name);
          //wtg2.displayOrder= 2;
          //wtg2.save();

          int foundGroup  = 0,
              foundWallet = 0,
              foundSAW = 0;

          // Search if WalletGroup exists, saving an existing wallet group fails?
          /* List<WalletGroup> groups = WalletGroup.getAllSortedByDisplayOrder();
          for (WalletGroup group : groups) {
              Log.e("WalletG", group.name);
              if (group.name.equals("Test Group")) {
                  wtg.name = group.name;
                  wtg.displayOrder = group.displayOrder;
//                  wtg.save();
                  //wtg.displayOrder = group.displayOrder;
                  Log.e("WalletG", "Group Exists");
                  foundGroup = 1;
                  break;
              }
              else
                  Log.e("WalletG", "Group Does Not Exist...yet");
          }
          if (foundGroup == 0){
            wtg = new WalletGroup("Unsorted", 0);
            wtg.save();
          }

          List<Walletx> wallets = Walletx.getAll();
          for (Walletx wallet : wallets) {
              Log.e("WalletX", wallet.name);
              if (wallet.name.equals("First Wallet")) {
                  wtx.name = wallet.name;
                  wtx.type = wallet.type;
                  wtx.group = wallet.group;
                  Log.e("WalletX", "Wallet Exists");
                  foundWallet = 1;
                  break;
              } else
                  Log.e("WalletX", "Wallet Does Not Exist...yet");
          }
          if (foundWallet == 0) {
              wtx = new Walletx("First Wallet", WalletType.SINGLE_ADDRESS_WALLET, wtg);
              wtx.save();
          }

          List<SingleAddressWallet> listOfIndividualSAW = SingleAddressWallet.getAll();
          for (SingleAddressWallet individualSAW : listOfIndividualSAW) {
              Log.e("Saw", individualSAW.publicKey);
              if (individualSAW.publicKey.equals("1E6QRQG9KR6WfxU4fmRLzjyHDkeDCtjGoR")) {
                 saw.publicKey = individualSAW.publicKey;
                 saw.wtx = individualSAW.wtx;
                 Log.e("SAW", "SAW Exists!");
                 foundSAW = 1;
                 break;
              } else
                  Log.e("SAW", "SAW Does Not Exist...yet");
          }
          if (foundSAW == 0) {
              saw = new SingleAddressWallet(wtx, "1E6QRQG9KR6WfxU4fmRLzjyHDkeDCtjGoR");
              saw.save();
          }
*/
          WalletGroup.dump();
          Walletx.dump();
          SingleAddressWallet.dump();

          try {
              json = readUrl("https://blockchain.info/address/" + publicAddress + "?format=json");
              Gson gson = new Gson();
              Transaction transaction = gson.fromJson(json, Transaction.class);

              Log.v(logInfo, transaction.address);
          } catch (Exception e) {
              Log.v(logInfo, "did not work");
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
