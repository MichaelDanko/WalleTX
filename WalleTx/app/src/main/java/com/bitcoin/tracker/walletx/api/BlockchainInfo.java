package com.bitcoin.tracker.walletx.api;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.activeandroid.Model;
import com.activeandroid.annotation.Table;
import com.bitcoin.tracker.walletx.activity.MainActivity;
import com.bitcoin.tracker.walletx.model.SingleAddressWallet;
import com.bitcoin.tracker.walletx.model.WalletGroup;
import com.bitcoin.tracker.walletx.model.WalletType;
import com.bitcoin.tracker.walletx.model.Walletx;
import com.google.bitcoin.core.Wallet;
import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

/**
 * Fetches Blockchain and wallet data from Blockchain.info using the Blockchain.info API.
 */
public class BlockchainInfo {

  private static class RetrieveBlockchainJSON extends AsyncTask<String, String, String> {

    private ProgressDialog QueryProgressDialog;

    private static final String logInfo = "Blockchain API";
      private Context applicationContext;

      public Context getApplicationContext() {
          return applicationContext;
      }

      static class Transaction {
      String address;
      String final_balance;
    }


      @Override
      protected String doInBackground(String... strings) {

          String json = "";
          WalletGroup wtg = new WalletGroup();
          Walletx wtx = new Walletx();
          SingleAddressWallet saw = new SingleAddressWallet();

          //WalletGroup wtg2 = WalletGroup.load(WalletGroup.class, 2 );
          //Log.e("test", wtg2.name);
          //wtg2.displayOrder= 2;
          //wtg2.save();

          int foundGroup  = 0,
              foundWallet = 0,
              foundSAW = 0;

          // Search if WalletGroup exists, saving an existing wallet group fails?
          List<WalletGroup> groups = WalletGroup.getAllSortedByDisplayOrder();
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

          wtg.dump();
          wtx.dump();
          saw.dump();
          try {
              json = readUrl("https://blockchain.info/address/1E6QRQG9KR6WfxU4fmRLzjyHDkeDCtjGoR?format=json");
              Gson gson = new Gson();
              Transaction transaction = gson.fromJson(json, Transaction.class);


              Log.v(logInfo, transaction.address);
          } catch (Exception e) {
              Log.v(logInfo, "did not work");
          }
          ;
          // String json = readUrl("http://www.javascriptkit.com/"+ "dhtmltutors/javascriptkit.json");


          //for (Item item : page.items)
          //  System.out.println("    " + item.title);
          saw.dump();
          return "String";
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

  }

  public BlockchainInfo() throws JSONException {
    new RetrieveBlockchainJSON().execute();
  }
} // BlockchainInfo
