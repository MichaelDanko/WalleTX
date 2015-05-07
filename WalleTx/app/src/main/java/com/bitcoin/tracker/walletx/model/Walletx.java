package com.bitcoin.tracker.walletx.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.HashMap;
import java.util.List;

/**
 * Walletx model.

 * Walletx objects are central to the application as they represents
 * the different wallets added by the user.
 *
 * TODO Add indexes & constraints to columns (if any)
 *
 */
@Table(name = "Walletx")
public class Walletx extends Model {

    //region WALLETX MODEL

    @Column(name = "Name")
    public String name;

    @Column(name = "WalletType")
    public SupportedWalletType type;

    // Belongs to one WalletGroup (mandatory)
    @Column(name = "WalletGroup")
    public Group group;

    // Has many Txs
    public List<Tx> txs() {
        return getMany(Tx.class, "Walletx");
    }

    // Has many Balances
    public List<Balance> balances() {
        return getMany(Balance.class, "Walletx");
    }

    public Walletx() {
        super();
    }

    public Walletx(String name, SupportedWalletType type, Group group) {
        super();
        this.name = name;
        this.type = type;
        this.group = group;
    }

    //endregion

    //region QUERIES
    //----------------------------------------------------------------------------------------------

    public static void createTypeSingleAddressWallet(String name) {

    }

    public static void createTypeSingleAddressWallet(String name, Group group, String address) {
        Walletx wtx = new Walletx();
        wtx.name = name;
        wtx.type = SupportedWalletType.SINGLE_ADDRESS_WALLET;
        wtx.group = group;
        wtx.save();
        SingleAddressWallet.create(address, wtx);
    }

    /**
     * @return List of all Walletxs.
     */
    public static List<Walletx> getAll() {
        return new Select()
                .from(Walletx.class)
                .execute();
    }

    /**
     * @param name of the Walletx
     * @return Walletx selected by name
     */
    public static Walletx getBy(String name) {
        return new Select()
                .from(Walletx.class)
                .where("Name = ?", name)
                .executeSingle();
    }

    /**
     * @param saw SingleAddressWallet associated with the Walletx
     * @return Walletx selected by SingleAddressWallet
     */
    public static Walletx getBy(SingleAddressWallet saw) {
        return saw.wtx;
    }

    /**
     * @param publicKey
     * @return Walletx associated with public key
     */
    public static Walletx getByPublicKey(String publicKey) {
        SingleAddressWallet saw = SingleAddressWallet.getPublicKey(publicKey);
        return saw.wtx;
    }

    /**
     * @return true if Walletx table is empty
     */
    public static boolean isEmpty() {
        int count = new Select().from(Walletx.class).count();
        return (count <= 0) ? true : false;
    }

    /**
     * @return Tx count of a wallet
     */
    public int getTxCount() {
        return this.txs().size();
    }


    public static void delete(Walletx wtx) {
        // Delete tx's associated with this wtx
        List<Tx> txs = wtx.txs();
        for ( Tx tx : txs ) {
            tx.delete();
        }

        // Delete balances's associated with this wtx
        List<Balance> balances = wtx.balances();
        for ( Balance balance : balances ) {
            balance.delete();
        }

        // Delete SupportedWalletTypes associated with this wtx
        if (wtx.type == SupportedWalletType.SINGLE_ADDRESS_WALLET) {
            SingleAddressWallet saw = SingleAddressWallet.getByWalletx(wtx);
            saw.delete();
        }

        wtx.delete();
    }

    //endregion
    //region VALIDATE
    //----------------------------------------------------------------------------------------------

    /**
     * @param name Walletx name to validate
     * @return true if name is empty string
     */
    public static boolean isEmptyString(String name) {
        return name.equals("");
    }

    /**
     * @param name Walletx name to validate
     * @return true if Group name matches an existing name
     */
    public static boolean matchesExisting(String name) {
        Walletx existing = Walletx.getBy(name);
        return existing != null ? true : false;
    }

    /**
     * Validates that a Walletx name is unique, excluding comparison
     * to the Walletx being updated.
     *
     * @param name Walletx name to validate
     * @param updating Walletx being updating
     * @return true if Walletx name matches an existing name that is not self
     */
    public static boolean matchesExisting(String name, Walletx updating) {
        Walletx existing = Walletx.getBy(name);
        if (existing != null && existing != updating)
            return true;
        return false;
    }

    //endregion
    //region DEBUG
    //----------------------------------------------------------------------------------------------

    public static void dump() {
        String dividerCol1 = "------------------";
        String dividerCol23 = "---------------------------------";
        System.out.printf("%-20s %-35s %-36s\n", dividerCol1, dividerCol23, dividerCol23);
        System.out.printf("%-20s %-35s %-36s\n", "Name", "WalletType", "WalletGroup");
        System.out.printf("%-20s %-35s %-36s\n", dividerCol1, dividerCol23, dividerCol23);
        List<Walletx> wtxs = Walletx.getAll();
        for (Walletx wtx : wtxs) {
            System.out.printf(
                    "%-20s %-35s %-36s\n",
                    wtx.name,
                    wtx.type,
                    wtx.group);
        }
    }

    //endregion

} // Walletx
