package com.bitcoin.tracker.walletx.model;

import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Tx model.
 *
 * Tx table stores all transactions associated with wallets added by the user.
 *
 * TODO Add indexes & constraints to columns (if any)
 *
 */
@Table(name = "Tx")
public class Tx extends Model {

    @Column(name = "timestamp", index = true)
    private Date timestamp;

    // TODO can we get the time zone from the system and append it to our date?
//    public void setDateFromString(Date date) {
//        SimpleDateFormat sf = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy");
//        // TODO does this set the time zone appropriately?
//        sf.setTimeZone(TimeZone.getDefault());
//        sf.setLenient(true);
//        try {
//            this.timestamp = sf.format(date);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//    }

    @Column(name = "amountBTC")
    private long amountBTC;

    @Column(name = "amountLC")
    private long amountLC;

    @Column(name = "block")
    private String block;

    @Column(name = "tx_index")
    private long tx_index;

    // Belongs to one Walletx (mandatory)
    @Column(name = "Walletx")
    public Walletx wtx;

    // Belongs to one category (optional)
    @Column(name = "TxCategory")
    public TxCategory category;

    // Has one note (optional)
    @Column(name = "TxNote")
    public TxNote note;

    public Tx() {
        super();
    }

    public Tx(Date date, Walletx wtx, String block, long tx_index, TxCategory category,
              TxNote note, long amountBTC, long amountLC) {
        super();
        //this.setDateFromString(date);
        this.timestamp = date;
        this.amountBTC = amountBTC;
        this.amountLC = amountLC;
        this.block = block;
        this.tx_index = tx_index;
        this.wtx = wtx;
        this.category = category;
        this.note = note;
    }

    /*--------------*
     *  Tx Queries  *
     *--------------*/

    /**
     * @return a specific row with query of WalleTx
     */

    public static Tx getTxIndex(Long _tx_index){
        return new Select()
                .from(Tx.class)
                .where("tx_index = ?", _tx_index)
                .orderBy("timestamp DESC")
                .executeSingle();
    }

    /**
     * @return all rows with query of WalleTx
     */

    public static List<Tx> getAllTxWalleTx(Walletx _wtx){
        return new Select()
                .from(Tx.class)
                .where("Walletx = ?", _wtx)
                .orderBy("timestamp DESC")
                .execute();
    }

    /**
     *
     * @param _category
     * @return all transactions with param of catagory
     */

    public static List<Tx> getAllTxCategory(TxCategory _category){
        return new Select()
                .from(Tx.class)
                .where("TxCategory = ?", _category)
                .orderBy("timestamp DESC")
                .execute();

    }

    /**
     *
     * @return queries entire table for dump method
     */

    public static List<Tx> getAllTxTest(){
        return new Select()
                .from(Tx.class)
                .orderBy("timestamp DESC")
                .execute();


    }

    /**
     * dump method for testing
     */

    public static void dump() {
        String dividerCol1 = "------------------";
        String dividerCol23 = "-------------";
        System.out.printf("%-20s %-29s %-16s\n", dividerCol1, dividerCol23, dividerCol23);
        System.out.printf("%-20s %-29s %-16s\n", "Amount", "timestamp", "tx_index");
        System.out.printf("%-20s %-29s %-16s\n", dividerCol1, dividerCol23, dividerCol23);
        List<Tx> txs = Tx.getAllTxTest();
        for (Tx tx : txs) {
            System.out.printf(
                    "%-20s %-29s %-16s\n",
                    tx.amountBTC,
                    tx.timestamp,
                    tx.tx_index);
        }
    }
} // Tx
