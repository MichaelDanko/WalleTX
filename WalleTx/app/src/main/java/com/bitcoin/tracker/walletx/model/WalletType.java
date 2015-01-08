package com.bitcoin.tracker.walletx.model;

/**
 * Supported wallet types
 *
 */
public enum WalletType {

    SINGLE_ADDRESS_WALLET;

    public String toString() {
        switch(this) {
            case SINGLE_ADDRESS_WALLET: return "Single Address Wallet";
            default: throw new IllegalArgumentException();
        }
    }

}
