package com.bitcoin.tracker.walletx.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by michael on 3/25/15.
 */

public class btcTransactionJSON {

    static public class jsonInputs {
        String sequence;
    }

    static class jsonOutputs {

    }

    static public class jsonTxs {
        public int ver;
        List<jsonInputs> inputs;
    }

      @SerializedName("hash160")
      public String hash160 = "default";
      @SerializedName("n_tx")
      public String n_tx;
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
