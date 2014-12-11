package com.bitcoin.tracker.walletx.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Tx table
 *
 *
 */
@Table(name = "Tx")
public class Tx extends Model {

    @Column(name = "Walletx") // Tx belongs to one Walletx
    public Walletx wtx;

    @Column(name = "timestamp", index = true)
    private Date timestamp;

    public void setDateFromString(String date) {
        SimpleDateFormat sf = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy");
        sf.setLenient(true);
        this.timestamp = sf.parse(date);
    }

    @Column(name = "amountBTC")
    private float amountBTC;

    @Column(name = "amountLC")
    private float amountLC;

    @Column(name = "block")
    private int block;

    @Column(name = "hash")
    private String hash;

    @Column(name = "TxCategory") // Tx belongs to zero/one category (tag)
    public TxCategory category;

    public Tx() {
        super();
    }

    public Tx(Walletx wtx, String date, float amountBTC, float amountLC, int block, String hash, TxCategory category) {
        super();
        this.wtx = wtx;
        this.setDateFromString(date);
        this.amountBTC = amountBTC;
        this.amountLC = amountLC;
        this.block = block;
        this.hash = hash;
        this.category = category;
    }

    /*
     * Tx Queries
     *
     */





} // Tx
