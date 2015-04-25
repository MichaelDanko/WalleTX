package com.bitcoin.tracker.walletx.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

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

    // Sum of Spending Records
    @Column(name = "TotalSpend")
    public long totalSpend = 0;

    // Sum of Receive Records
    @Column(name = "TotalReceive")
    public long totalReceive = 0;

    // Final Balance of the Wallet
    @Column(name = "FinalBalance")
    public long finalBalance = 0;

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

    /**
     * Dumps the Walletx table to console.
     * For debugging purposes only.
     */
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
    //region WALLETX QUERIES

    /**
     * @return List of all Walletxs.
     */
    public static List<Walletx> getAll() {
        return new Select()
                .from(Walletx.class)
                .execute();
    }

    /**
     * @return Walletx selected by name
     */
    public static Walletx getBy(String name) {
        return new Select()
                .from(Walletx.class)
                .where("Name = ?", name)
                .executeSingle();
    }


    //endregion



} // Walletx
