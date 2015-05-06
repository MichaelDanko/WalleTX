package com.bitcoin.tracker.walletx.model;

import android.content.Context;
import android.widget.Toast;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.bitcoin.tracker.walletx.helper.Formatter;

import java.util.Date;
import java.util.List;

/**
 * Tx represents a single transaction.
 */
@Table(name = "Tx")
public class Tx extends Model {

    // Tx type constants
    public static final int SPEND = -1;
    public static final int RECEIVE = 1;

    //region Table
    //----------------------------------------------------------------------------------------------

    @Column(name = "timestamp", index = true)
    public Date timestamp;

    @Column(name = "amountBTC")
    public long amountBTC;

    // Tx type - is tx a spend or receive?
    // -1 = Spend, 1 = Receive
    @Column(name = "type")
    public int type;

    @Column(name = "block")
    public long block;

    @Column(name = "hash")
    public String hash;

    // Belongs to one Walletx (mandatory)
    @Column(name = "Walletx")
    public Walletx wtx;

    @Column(name = "Balance")
    public Balance balance;

    // Belongs to one category (optional)
    @Column(name = "TxCategory")
    public Category category;

    // Has one note (optional)
    @Column(name = "TxNote")
    public Note note;

    public Tx() {
        super();
    }

    public Tx(Date date, Walletx wtx, long block, long amountBTC, String hash) {
        super();
        this.timestamp = date;
        this.amountBTC = amountBTC;
        this.block = block;
        this.hash = hash;
        this.wtx = wtx;
    }

    //endregion
    //region Queries -------------------------------------------------------------------------------

    /**
     * @return Single Tx selected by hash value
     */
    public static Tx getTxByHash(String hash){
        return new Select()
                .from(Tx.class)
                .where("hash = ?", hash)
                .executeSingle();
    }

    /**
     * @return All Txs linked to a Walletx
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

    public static List<Tx> getAllTxCategory(Category _category){
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


    //endregion

    //region VALIDATION
    //----------------------------------------------------------------------------------------------



    //endregion

    //region DEBUG
    //----------------------------------------------------------------------------------------------

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
                    tx.timestamp);
        }
    }

} // Tx
