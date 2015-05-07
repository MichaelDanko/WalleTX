package com.bitcoin.tracker.walletx.helper;

import com.bitcoin.tracker.walletx.activity.Constants;

import java.text.DecimalFormat;

/**
 * Created by brianhowell on 5/5/15.
 */
public class Formatter {

    public static String btcToString(Long value) {
        double satoshis = Double.valueOf(Constants.SATOSHIS);
        double inBtc = Double.valueOf(value) / satoshis;
        DecimalFormat df = new DecimalFormat("0.########");
        return df.format(inBtc);
    }

    public static String usdToString(float value) {
        if (value == 0)
            return "0";
        return String.format("%.2f", value);
    }

}
