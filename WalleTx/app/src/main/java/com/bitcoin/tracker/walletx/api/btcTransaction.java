/* Michael Danko
 * CEN4021 Software Engineering II
 * Pretige Worldwide
 * Blockchain API Source Code for Assignment 7
 * Created 03-20-2015
 * Copyright 2015
 */

/* Data structure to hold data from API call, combine with API call class */

package com.bitcoin.tracker.walletx.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by michael on 3/25/15.
 */

public class btcTransaction {

    static public class jsonInputs {
        public String sequence;
    }

    static class jsonOutputs {

    }

    static public class jsonTxs {
        public int ver;
        public List<jsonInputs> inputs;
    }

    public btcTransaction()
    {
    }

    @SerializedName("hash160")
    public String hash160 = "default";
    @SerializedName("n_tx")
    public String n_tx = "";
    @SerializedName("total_received")
    public String total_received = "";
    @SerializedName("total_sent")
    public String total_sent = "";
    @SerializedName("address")
    public String address = "";
    @SerializedName("final_balance")
    public String final_balance = "";
    //@SerializedName("txs")
    public List<jsonTxs> txs;

    // Additional Fields
    String time;
    String amountBTC;
    String amountLC;
    String block;
    String hash;
    String wtx;
    String category;
    String note;
}
