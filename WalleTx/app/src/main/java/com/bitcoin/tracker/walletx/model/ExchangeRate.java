package com.bitcoin.tracker.walletx.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.activeandroid.util.SQLiteUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * ExchangeRate model.
 *
 * ExchangeRate table will hold daily exchange rates from BTC to USD, EUR, and GBP.
 *
 * TODO Add indexes & constraints to columns (if any)
 * TODO Investigate coindesk API to see how far back data goes. Will impact charts.
 *
 */
@Table(name = "ExchangeRate")
public class ExchangeRate extends Model {

    public static float EXCHANGE_RATE_IN_USD = 0;

    @Column(name = "timestamp")
    public Date timestamp;

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
    //private float usd;
    public float usd;

    @Column(name = "EUR")
    //private float eur;
    public float eur;

    @Column(name = "GBP")
    //private float gbp;
    public float gbp;

    public ExchangeRate() {
        super();
    }

    public ExchangeRate(Date timestamp, float usd, float eur, float gbp) {
        super();
        //this.setDateFromString(date);
        this.timestamp = timestamp;
        this.usd = usd;
        this.eur = eur;
        this.gbp = gbp;
    }

    /*------------------------*
     *  ExchangeRate Queries  *
     *------------------------*/

    /**
     * @return USD with specific date
     */

    public static ExchangeRate fromDateUSD(Date date){
        return new Select("USD")
                .from(ExchangeRate.class)
                .where("timestamp = ?", date)
                .executeSingle();
    }


    /**
     * @return EUR with specific date
     * @param date
     */

    public static ExchangeRate fromDateEUR(String date){
        return new Select("EUR")
                .from(ExchangeRate.class)
                .where("timestamp = ?", date)
                .executeSingle();
    }

    /**
     * @return GBP with specific date
     */

    public static ExchangeRate fromDateGBP(Date date){
        return new Select("GBP")
                .from(ExchangeRate.class)
                .where("timestamp = ?", date)
                .executeSingle();
    }



    /**
     * @return  most recent USD by timestamp
     */

    public static Float getUSD(){
        //raw query to get one item from column
        List<ExchangeRate> USDrates = SQLiteUtils.rawQuery(ExchangeRate.class, "Select USD from ExchangeRate Order by timestamp DESC Limit 1", new String[]{"null"} );
        //returns pull list item
        return USDrates.get(0).usd;
    }


    /**
     * @return returns most recent EUR by timestamp
     */

    public static Float getEUR(){
        //raw query to get one item from column
        List<ExchangeRate> EURrates = SQLiteUtils.rawQuery(ExchangeRate.class, "Select EUR from ExchangeRate Order by timestamp DESC Limit 1", new String[]{"null"} );
        //returns pull list item
        return EURrates.get(0).eur;
    }


    /**
     * @return returns most recent GBR by timestamp
     */

    public static Float getGBR(){
        //raw query to get one item from column
        List<ExchangeRate> GBRrates = SQLiteUtils.rawQuery(ExchangeRate.class, "Select GBR from ExchangeRate Order by timestamp DESC Limit 1", new String[]{"null"} );
        //returns pull list item
        return GBRrates.get(0).gbp;
    }

    /**
     * debug get all of ExchangeRate
     */

    public static List<ExchangeRate> getAllSortedTime(){
        return new Select()
                .from(ExchangeRate.class)
                .orderBy("Timestamp DESC")
                .execute();
    }


    /**
     * Dumps the ExchangeRate table to console.
     * For debugging purposes only.
     * if it works for USD and EUR no need to test all 3
     */
    public static void dump() {
        String dividerCol1 = "------------------";
        String dividerCol23 = "-------------";
        System.out.printf("%-20s %-15s %-16s\n", dividerCol1, dividerCol23, dividerCol23);
        System.out.printf("%-20s %-15s %-16s\n", "Timestamp", "USD", "EUR");
        System.out.printf("%-20s %-15s %-16s\n", dividerCol1, dividerCol23, dividerCol23);
        List<ExchangeRate> Rates = ExchangeRate.getAllSortedTime();
        for (ExchangeRate rate : Rates) {
            System.out.printf(
                    "%-20s %-15s %-16s\n",
                    rate.timestamp,
                    rate.usd,
                    rate.eur);
        }
    }

} // ExchangeRate
