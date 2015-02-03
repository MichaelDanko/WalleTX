package com.bitcoin.tracker.walletx.api;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

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

/**
 * Fetches Blockchain and wallet data from Blockchain.info using the Blockchain.info API.
 */
public class BlockchainInfo {

  private static class RetrieveBlockchainJSON extends AsyncTask<String, String, String> {

    private ProgressDialog QueryProgressDialog;

    private static final String logInfo = "Blockchain API";

    static class Transaction {
      String address;
      String final_balance;
    }


    @Override
    protected String doInBackground(String...strings) {
      // redditList = readRedditFeed();

      String json = "";
      //System.out.println("Hello");
      try {
        json = readUrl("https://blockchain.info/address/1E6QRQG9KR6WfxU4fmRLzjyHDkeDCtjGoR?format=json");
              Gson gson = new Gson();
      Transaction transaction = gson.fromJson(json, Transaction.class);

      Log.v(logInfo, transaction.address);
      } catch (Exception e) {
        Log.v(logInfo, "did not work");
      };
      // String json = readUrl("http://www.javascriptkit.com/"+ "dhtmltutors/javascriptkit.json");



      //for (Item item : page.items)
      //  System.out.println("    " + item.title);

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
