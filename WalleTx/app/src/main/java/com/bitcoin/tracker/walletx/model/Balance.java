package com.bitcoin.tracker.walletx.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Balance table
 *
 */
@Table(name = "Balance")
public class Balance extends Model {

    @Column(name = "Walletx") // Balance belongs to a Walletx
    public Walletx wtx;

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

    @Column(name = "ExchangeRate") // Balance has an exchange rate
    public ExchangeRate eRate;

    public Balance() {
        super();
    }

    public Balance(Walletx wtx, String date, float balance, ExchangeRate eRate) {
        super();
        this.wtx = wtx;
        this.setDateFromString(date);
        this.balance = balance;
        this.eRate = eRate;
    }

    /*
     * Balance Queries
     *
     */





} // Balance
