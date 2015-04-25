/* Michael Danko
 * CEN4021 Software Engineering II
 * Pretige Worldwide
 * Blockchain API Source Code for Assignment 7
 * Created 03-20-2015
 * Copyright 2015
 */

/* Data structure to hold data from API call, combine with API call class */

package com.bitcoin.tracker.walletx.api;

import java.util.List;

/**
 * Created by michael on 3/25/15.
 */

public class BlockchainInfoGson {

    public String address;                 // wallet's public address
    public List<txGson> txs; // txs associated with this wallet
    public long final_balance = 0;
    public BlockchainInfoGson() {}

    static public class txGson {
        public String hash;  // tx hash
        public long block_height;  // used to calculate confimations
        public long time;  // tx time
        public long result;
        public long tx_index;
        public List<inputsGson> inputs;
        public List<outputsGson> out;
        public txGson() {}
    }

    static public class inputsGson {
        public prevOutGson prev_out;
    }

    static public class outputsGson {
        public String addr;
        public long value;
    }

    static public class prevOutGson {
        public String addr;
        public long value;
    }
}
