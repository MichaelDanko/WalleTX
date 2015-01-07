package com.bitcoin.tracker.walletx.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.google.bitcoin.core.Address;
import com.google.bitcoin.core.AddressFormatException;

/**
 * SingleAddressWallet model.
 *
 * SingleAddressWallet is a supported WalletType that consist of a public key.
 *
 * TODO Add indexes & constraints to columns (if any)
 *
 */
@Table(name = "SingleAddressWallet")
public class SingleAddressWallet extends Model implements WalletxBlockchainInterface {

    @Column(name = "PublicKey")
    public String publicKey;

    // SAW belongs to one Walletx (mandatory)
    @Column(name = "Walletx")
    public Walletx wtx;

    public SingleAddressWallet() {
        super();
    }

    public SingleAddressWallet(Walletx wtx, String publicKey) {
        super();
        this.wtx = wtx;
        this.publicKey = publicKey;
    }

    /**
     * Validates if a string is a valid public key.
     */
    public static boolean isValidAddress(String address) {
        try {
            new Address(null, address);
            return true;
        } catch(AddressFormatException e) {
            return false;
        }
    }

} // SingleAddressWallet
