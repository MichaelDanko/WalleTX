package com.bitcoin.tracker.walletx.model;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * SingleAddressWallet table
 *
 */
@Table(name = "SingleAddressWallet")
public class SingleAddressWallet {

    @Column(name = "PublicKey")
    public String publicKey;

    @Column(name = "Walletx") // SAW belongs to one Walletx
    public Walletx wtx;

    public Walletx() {
        super();
    }

    public Walletx(Walletx wtx, String publicKey) {
        super();
        this.wtx = wtx;
        this.publicKey = publicKey;
    }

    // SingleAddressWallet Queries
    // ...

} // SingleAddressWallet
