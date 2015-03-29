package com.bitcoin.tracker.walletx.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Balance model.
 *
 * A Balance is a snapshot of a Walletx balance at a given point in time.
 * Each time there is a new transaction associated with a Walletx, a new balance should be added.
 * Balance detail should not go into detail finer than a daily balance.
 * If 2 balances occur on the same day the newest balance should overwrite the older balance.
 * All Balances are in BTC.
 *
 * TODO Add indexes & constraints to columns (if any)
 *
 */
@Table(name = "Balance")
public class Balance extends Model {

    @Column(name = "timestamp")
    private Date timestamp;

    public void setDateFromString(String date) {
        SimpleDateFormat sf = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy");
        sf.setLenient(true);
        try {
            this.timestamp = sf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Column(name = "Balance")
    public float balance;

    // Belongs to a Walletx
    @Column(name = "Walletx")
    public Walletx wtx;

    public Balance() {
        super();
    }

    public Balance(String date, float balance, Walletx wtx) {
        super();
        this.setDateFromString(date);
        this.balance = balance;
        this.wtx = wtx;
    }

    /*-------------------*
     *  Balance Queries  *
     *-------------------*/
    /**
    * @return Balance selected by wtx
    *
    */
    public static Balance getBalance(Walletx wtx){
    return new Select()
        .from(Balance.class)
        .where("Walletx = ?", wtx)
        .executeSingle();
    }

    /**
     *
     * @return list of balances (debug)
     */
    public static List<Balance> getAllBalances(){
        return new Select()
                .from(Balance.class)
                .orderBy("timestamp")
                .execute();

    }


    /**
     * dumps all the balances table to console
     * debug only
     */
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
                    "%-20s %-15s %-16s\n",
                    balance1.balance,
                    balance1.timestamp,
                    balance1.wtx);
        }
    }

} // Balance
