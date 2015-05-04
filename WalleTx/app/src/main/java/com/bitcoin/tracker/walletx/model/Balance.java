package com.bitcoin.tracker.walletx.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.Date;
import java.util.List;

/**
 * Balance is a snapshot of a Walletx balance at a given point in time.
 * Each time there is a new Tx associated with a Walletx, a new balance should be added.
 * Balance detail should not go into detail finer than a daily balance.
 * If 2 balances occur on the same day the newest balance should overwrite the older balance.
 * All Balances are in BTC.
 */
@Table(name = "Balance")
public class Balance extends Model {

    //region Table
    //----------------------------------------------------------------------------------------------

    @Column(name = "timestamp", index = true)
    public Date timestamp;

    @Column(name = "Balance")
    public float balance;

    // Belongs to a Walletx
    @Column(name = "Walletx")
    public Walletx wtx;

    public Balance() {
        super();
    }

    public Balance(Date timestamp, float balance, Walletx wtx) {
        super();
        this.timestamp = timestamp;
        this.balance = balance;
        this.wtx = wtx;
    }

    //endregion

    //region QUERIES
    //----------------------------------------------------------------------------------------------

    /**
     * @return latest balance selected by wtx
     */
    public static Balance getBalance(Walletx wtx){
    return new Select()
        .from(Balance.class)
        .where("Walletx = ?", wtx)
        .orderBy("timestamp DESC")
        .executeSingle();
    }

    /**
     * @return list of all balances
     */
    public static List<Balance> getAllBalances() {
        return new Select()
                .from(Balance.class)
                .orderBy("timestamp")
                .execute();
    }

    //endregion

    //region VALIDATION
    //----------------------------------------------------------------------------------------------



    //endregion

    //region DEBUG
    //----------------------------------------------------------------------------------------------

    public static void dump(){
        String dividerCol1 = "------------------";
        String dividerCol23 = "-------------";
        System.out.printf("%-20s %-15s %-16s\n", dividerCol1, dividerCol23, dividerCol23);
        System.out.printf("%-20s %-15s %-16s\n", "Timestamp", "Balance", "WalleTx");
        System.out.printf("%-20s %-15s %-16s\n", dividerCol1, dividerCol23, dividerCol23);
        //List<WalletGroup> groups = WalletGroup.getAllSortedByDisplayOrder();
        List<Balance> balances = Balance.getAllBalances();
        for (Balance balance1 : balances) {
            System.out.printf(
                    "%-10s %-15s %-16s %-10s\n",
                    balance1.balance,
                    balance1.timestamp,
                    balance1.wtx);
        }
    }

    //endregion

} // Balance
