package com.bitcoin.tracker.walletx.model;

import android.content.Context;
import android.widget.Toast;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

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

    /**
     *
     * @return formatted string of BTC value
      */
    public static String formattedBTCValue(String _hash) {
        Tx tx = new Select()
                .from(Tx.class)
                .where("hash = ?", _hash)
                .orderBy("timestamp DESC")
                .executeSingle();

        return actualStringFormatter(Long.toString(tx.amountBTC));
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


    // TODO ???? This doesn't belong here logically ------------------------------------------------

    public static String formattedBTCValue(Long formatThisLong) {
       return actualStringFormatter(Long.toString(formatThisLong));
    }

    private static String actualStringFormatter (String editString) {
        String formattedString = null;
        StringBuilder buffer = new StringBuilder(20);
        int j = editString.length();

        int k = j-1;
        boolean isNeg = false;
        for (int i=16; i > 0 ; i--) {

            if ((16 - i) <= k) {
               if (editString.charAt(j-1) == '-') {
                   isNeg = true;
                   buffer.insert(0, '0');
               }
               else {
                   buffer.insert(0, (editString.charAt(j - 1)));
               }
               j--;
            }
            else
                buffer.insert(0, '0');
        }
        buffer.insert(8, '.');

        while ((buffer.charAt(0) == '0') && (buffer.length() > 10)) {
           buffer.delete(0,1);
        }

        while ((buffer.charAt(buffer.length() - 1) == '0') && (buffer.charAt(buffer.length() - 2)) != '.') {
           buffer.delete(buffer.length() - 1,buffer.length());
        }

        if (isNeg) {
            buffer.insert(0, '-');
        }

        formattedString = new String(buffer);
        return formattedString;
    }

} // Tx
