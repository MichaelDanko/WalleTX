package com.bitcoin.tracker.walletx.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * SingleAddressWallet table
 * SingleAddressWallet is a supported WalletType that consist of a public key.
 *
 */
@Table(name = "SingleAddressWallet")
public class SingleAddressWallet extends Model {

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

    /*
     * SingleAddressWallet Queries
     *
     */





} // SingleAddressWallet
