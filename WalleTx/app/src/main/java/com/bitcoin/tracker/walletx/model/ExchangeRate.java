package com.bitcoin.tracker.walletx.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ExchangeRate model.
 *
 * ExchangeRate table will hold daily exchange rates from BTC to USD, EUR, and GBP.
 *
 * TODO Add indexes & constraints to columns (if any)
 * TODO Investigate coindesk API to see how far back data goes. Will impact charts.
 *
 */
@Table(name = "ExhangeRate")
public class ExchangeRate extends Model {

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

    @Column(name = "USD")
    private float usd;

    @Column(name = "EUR")
    private float eur;

    @Column(name = "GBP")
    private float gbp;

    public ExchangeRate() {
        super();
    }

    public ExchangeRate(String date, float usd, float eur, float gbp) {
        super();
        this.setDateFromString(date);
        this.usd = usd;
        this.eur = eur;
        this.gbp = gbp;
    }

    /*------------------------*
     *  ExchangeRate Queries  *
     *------------------------*/





} // ExchangeRate
