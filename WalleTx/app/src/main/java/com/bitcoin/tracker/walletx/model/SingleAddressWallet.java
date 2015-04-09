package com.bitcoin.tracker.walletx.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.google.bitcoin.core.Address;
import com.google.bitcoin.core.AddressFormatException;

import java.util.List;

/**
 * SingleAddressWallet model.
 *
 * SingleAddressWallet is a supported WalletType that consist of a public key.
 *
 * TODO Add indexes & constraints to columns (if any)
 *
 * TODO technically the public key is not the public address, the public address is a hash of the
 * TODO public key, we should clean up the terminology if time permits.
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
     * Dumps the SingleAddressWallet table to console.
     * For debugging purposes only.
     */
    public static void dump() {
        String dividerCol1 = "--------------------------------------";
        String dividerCol2 = "-----------------------";
        System.out.printf("%-40s %-15s\n", dividerCol1, dividerCol2, dividerCol2);
        System.out.printf("%-40s %-15s\n", "Public Key", "Walletx Name");
        System.out.printf("%-40s %-15s\n", dividerCol1, dividerCol2, dividerCol2);
        List<SingleAddressWallet> saws = SingleAddressWallet.getAll();
        for (SingleAddressWallet saw : saws) {
            System.out.printf(
                    "%-40s %-15s\n",
                    saw.publicKey,
                    saw.wtx.name);
        }
    }

    /**
     * @return List of all SingleAddressWallets.
     */
    public static List<SingleAddressWallet> getAll() {
        return new Select()
                .from(SingleAddressWallet.class)
                .execute();
    }

    /**
     * @param wtx Walletx
     * @return SingleAddressWallet associated with the Walletx.
     */
    public static SingleAddressWallet getByWalletx(Walletx wtx) {
        return new Select()
                .from(SingleAddressWallet.class)
                .where("Walletx = ?", wtx.getId())
                .executeSingle();
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
