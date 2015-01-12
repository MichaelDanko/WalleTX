package com.bitcoin.tracker.walletx.api;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Fetches Blockchain and wallet data from Blockchain.info using the Blockchain.info API.
 */
public class BlockchainInfo {

  public long jsonFinalBalance = 0;
  private static final String TAG = "Blockchain.info API";
  public BlockchainInfo() throws JSONException {
    DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
    HttpPost httppost = new HttpPost("http://blockchain.info/rawaddr/19JbiksWxF7oJpEBRZrWH3FTNvaUBrrSqa");
    // Depends on your web service
    //httppost.setHeader("Content-type", "application/json");

    InputStream inputStream = null;
    String result = null;

    try {
      HttpResponse response = httpclient.execute(httppost);
      HttpEntity entity = response.getEntity();

      inputStream = entity.getContent();
      // json is UTF-8 by default
      BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
      StringBuilder sb = new StringBuilder();

      String line = null;
      while ((line = reader.readLine()) != null) {
        sb.append(line + "\n");
      }
      result = sb.toString();
    } catch (Exception e) {
      Log.v(TAG, "HTTP Failed");
    } finally {
      try {
        if (inputStream != null) inputStream.close();
      } catch (Exception squish) {
      }
    }

    JSONObject jObject = new JSONObject(result);
    jsonFinalBalance = jObject.getLong("final_balance");
  }
} // BlockchainInfo
