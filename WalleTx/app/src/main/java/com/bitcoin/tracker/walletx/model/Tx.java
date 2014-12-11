package com.bitcoin.tracker.walletx.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.text.ParseException;
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

    @Column(name = "TxNote") // Tx has zero/one note
    public TxNote note;

    public Tx() {
        super();
    }

    public Tx(
            Walletx wtx,
            String date,
            float amountBTC,
            float amountLC,
            int block,
            String hash,
            TxCategory category,
            TxNote note) {
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
