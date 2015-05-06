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

    /*
    public static String btcToString(String string) {
        String formattedString = null;
        StringBuilder buffer = new StringBuilder(20);
        int j = string.length();

        int k = j-1;
        boolean isNeg = false;
        for (int i=16; i > 0 ; i--) {

            if ((16 - i) <= k) {
                if (string.charAt(j-1) == '-') {
                    isNeg = true;
                    buffer.insert(0, '0');
                }
                else {
                    buffer.insert(0, (string.charAt(j - 1)));
                }
                j--;
            }
            else
                buffer.insert(0, '0');
        }
        buffer.insert(8, '.');

        while ((buffer.charAt(0) == '0') && (buffer.length() > 10)) {
            buffer.delete(0,1);
        }

        while ((buffer.charAt(buffer.length() - 1) == '0') && (buffer.charAt(buffer.length() - 2)) != '.') {
            buffer.delete(buffer.length() - 1,buffer.length());
        }

        if (isNeg) {
            buffer.insert(0, '-');
        }

        formattedString = new String(buffer);
        return formattedString;
    }
    */



}
