package com.bitcoin.tracker.walletx.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    // TODO need a way to pull balances so we can spend some btcs!





} // Balance
