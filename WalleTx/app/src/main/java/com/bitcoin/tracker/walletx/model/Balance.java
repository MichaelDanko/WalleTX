package com.bitcoin.tracker.walletx.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.Collections;
import java.util.Comparator;
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
    public long balance;

    // Belongs to a Walletx
    @Column(name = "Walletx")
    public Walletx wtx;

    public Balance() {
        super();
    }

    public Balance(Date timestamp, long balance, Walletx wtx) {
        super();
        this.timestamp = timestamp;
        this.balance = balance;
        this.wtx = wtx;
    }

    //endregion

    //region QUERIES
    //----------------------------------------------------------------------------------------------

    public static void clean(Walletx wtx) {

        List<Tx> txs = wtx.txs();

        // sort old to new
        Collections.sort(txs, new Comparator<Tx>() {
            @Override
            public int compare(Tx tx1, Tx tx2) {
                return tx1.timestamp.compareTo(tx2.timestamp);
            }
        });

        Tx prevTx = null;
        for (Tx tx : txs) {
            Balance balance = tx.balance;
            if (prevTx == null) {
                balance.balance = tx.amountBTC;
            } else {
                Balance prevBalance = prevTx.balance;
                balance.balance = prevBalance.balance + tx.amountBTC;
            }
            balance.save();
            prevTx = tx;
        }

    }

    // TXS ADDED OUT OF ORDER
    public static Balance createNewAssociatedWith(Tx tx) {
        Balance balance = new Balance(tx.timestamp, 0, tx.wtx);
        balance.save();
        return balance;
    }

    /**
     * @return latest balance selected by wtx
     */
    public static Balance getBalance(Walletx wtx) {
        return new Select()
            .from(Balance.class)
            .where("Walletx = ?", wtx.getId())
            .orderBy("timestamp DESC")
            .executeSingle();
    }

    public static long getBalanceAsLong(Walletx wtx) {
        Balance balance = new Select()
                .from(Balance.class)
                .where("Walletx = ?", wtx.getId())
                .orderBy("timestamp DESC")
                .executeSingle();
        return balance.balance;
    }

    /**
     * @return list of all balances
     */
    public static List<Balance> getAll() {
        return new Select()
                .from(Balance.class)
                .orderBy("timestamp DESC")
                .execute();
    }

    //endregion

    //region VALIDATION
    //----------------------------------------------------------------------------------------------



    //endregion




        //region DEBUG
    //----------------------------------------------------------------------------------------------

    public static void dump(){
        String dividerCol1 = "------------------------------";
        String dividerCol23 = "------------------";
        System.out.printf("%-32s %-20s %-20s\n", dividerCol1, dividerCol23, dividerCol23);
        System.out.printf("%-32s %-20s %-20s\n", "Timestamp", "Balance", "WalleTx");
        System.out.printf("%-32s %-20s %-20s\n", dividerCol1, dividerCol23, dividerCol23);
        List<Balance> balances = Balance.getAll();
        for (Balance balance : balances) {
            System.out.printf(
                    "%-32s %-20s %-16s\n",
                    balance.timestamp,
                    balance.balance,
                    balance.wtx);
        }
    }

    //endregion

} // Balance
