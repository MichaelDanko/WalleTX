package com.bitcoin.tracker.walletx.helper;

import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by brianhowell on 5/5/15.
 */
public class JsonHelper {

    // Error logging tag
    private static final String TAG = "JsonHelper";

    public static JsonElement getJsonElementFromUrl(URL url) {
        HttpURLConnection request = null;
        try {
            request = (HttpURLConnection) (url).openConnection();
            request.connect();
        } catch (MalformedURLException e) {
            Log.e(TAG, e.getLocalizedMessage());
        } catch (IOException e) {
            Log.e(TAG, e.getLocalizedMessage());
        }
        JsonParser jp = new JsonParser(); // gson lib
        JsonElement json = null; //convert the input stream to a json element
        try {
            InputStreamReader reader = new InputStreamReader((InputStream) request.getContent());
            json = jp.parse(reader).getAsJsonObject();
        } catch (IOException e) {
            Log.e(TAG, e.getLocalizedMessage());
        }
        return json;
    }

    public static URL buildUrlFromString(String sUrl) {
        URL url = null;
        try {
            url = new URL(sUrl);
        } catch (MalformedURLException e) {
            Log.e(TAG, e.getLocalizedMessage());
        }
        return url;
    }

}
